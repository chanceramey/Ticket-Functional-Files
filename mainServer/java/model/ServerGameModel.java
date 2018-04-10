package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import command.Command;
import communication.ClientProxy;
import interfaces.IGame;
import model.playerStates.NotTurnState;
import model.playerStates.Player;
import model.playerStates.TurnState;

/**
 * Created by tjense25 on 2/24/18.
 */

public class ServerGameModel implements IGame {

    private ClientProxy clientProxy = new ClientProxy();
    private Game waitingGame;
    private String gameID;

    private int currentPlayer;
    private List<Player> players;
    private Map<String, Integer> userIndexMap;

    private TrainCardDeck trainCardDeck;
    private TrainCard[] faceUpTrainCards;

    private DestCardDeck destCardDeck;
    private List<Command> gameHistoryCommands;
    private int lastPlayer;
    private Map<Integer, FinalGamePoints> allPlayersPoints = new HashMap<>();

    public ServerGameModel(Game game) {
        this.waitingGame = game;
        this.gameID = game.getID();
        initializePlayersList(game);
        trainCardDeck = new TrainCardDeck();
        faceUpTrainCards = new TrainCard[5];
        for (int i = 0;  i < faceUpTrainCards.length; i++) {
            faceUpTrainCards[i] = null;
        }
        destCardDeck = new DestCardDeck();
        gameHistoryCommands = new ArrayList<>();
    }

    private void initializePlayersList(Game game) {
        this.players = new ArrayList<>();
        this.userIndexMap = new HashMap<>();
        Color[] colors = Color.values();
        int playerNumber = 0;
        for (String playerName : game.getPlayers()) {
            Player player = new Player(playerName, colors[playerNumber], playerNumber);
            players.add(player);
            userIndexMap.put(playerName, playerNumber);
            playerNumber++;
        }
    }

    public void startGame() {
        for (Player p : players) {
            TrainCard[] drawnTrainCards = trainCardDeck.drawCards(4);
            p.addTrainCards(drawnTrainCards);

            clientProxy.drawTrainCards(p.getId(), 4, drawnTrainCards, trainCardDeck.size());
            gameHistoryCommands.add(clientProxy.getCommand());

            DestinationCard[] drawnDestCards = destCardDeck.drawCards(3);
            p.addDestCards(drawnDestCards);

            clientProxy.drawDestCards(p.getId(), 3, drawnDestCards, destCardDeck.size());
            gameHistoryCommands.add(clientProxy.getCommand());
        }

        faceUpTrainCards = trainCardDeck.drawCards(5);
        while(hasWilds(3, faceUpTrainCards)) {
            trainCardDeck.discard(faceUpTrainCards);
            trainCardDeck.drawCards(5);
        }

        clientProxy.swapFaceUpCards(new int[] {0,1,2,3,4}, faceUpTrainCards, trainCardDeck.size());
        gameHistoryCommands.add(clientProxy.getCommand());

        lastPlayer = -1;

        currentPlayer = 0;
        players.get(currentPlayer).setState(new TurnState(players.get(currentPlayer)));
        clientProxy.setTurn(currentPlayer);
        gameHistoryCommands.add(clientProxy.getCommand());
    }

    public Player getPlayerFromUsername(String user) {
        for (Player p : players) {
            if (p.getUser().equals(user)) {
                return p;
            }
        }
        return null;
    }


    public void sendMessage(GameHistory gameHistory) {
        clientProxy.receiveMessage(gameHistory);
        gameHistoryCommands.add(clientProxy.getCommand());
    }
      
    public Command[] getGameCommands(int gameHistoryPosition) {
        int numNewCommands = gameHistoryCommands.size() - gameHistoryPosition;
        Command[] commands = new Command[numNewCommands];
        for (int i = 0; i < numNewCommands; i++) {
            commands[i] = gameHistoryCommands.get(i + gameHistoryPosition);
        }
        return commands;
    }

    public boolean claimRoute(String user, String routeID, int length, int[] cardPos) {
        Player p = getPlayerFromUsername(user);

        TrainCard[] discarded = p.claimRoute(routeID, cardPos);
        if(discarded == null) return false;

        trainCardDeck.discard(discarded);
        clientProxy.discardTrainCards(p.getId(), cardPos.length, cardPos, trainCardDeck.size());
        gameHistoryCommands.add(clientProxy.getCommand());


        if (getFaceUpNum() != 5) {
            fillFaceUp(trainCardDeck.drawCards(5 - getFaceUpNum()));
            clientProxy.swapFaceUpCards(new int[] {0,1,2,3,4}, faceUpTrainCards, trainCardDeck.size());
            gameHistoryCommands.add(clientProxy.getCommand());
        }

        clientProxy.claimedRoute(p.getId(), routeID);
        gameHistoryCommands.add(clientProxy.getCommand());

        if (!p.isTurn()) {
            setNextTurn();
        }

        return true;
    }

    public boolean drawFaceUp(String username, Integer i) {
        if(faceUpTrainCards[i] == null) return false;

        Player p = getPlayerFromUsername(username);
        if(!p.addFaceUpCard(faceUpTrainCards[i])) {
            return false;
        }

        clientProxy.drawTrainCards(p.getId(), 1, new TrainCard[] {faceUpTrainCards[i]}, trainCardDeck.size());
        gameHistoryCommands.add(clientProxy.getCommand());

        faceUpTrainCards[i] = trainCardDeck.drawCard();
        if (hasWilds(3, faceUpTrainCards) && trainCardDeck.size() != 0) {
            while(hasWilds(3, faceUpTrainCards) && trainCardDeck.hasEnoughNonWilds()) {
                trainCardDeck.discard(faceUpTrainCards);
                faceUpTrainCards = trainCardDeck.drawCards(5);
            }
            clientProxy.swapFaceUpCards(new int[] {0,1,2,3,4}, faceUpTrainCards,trainCardDeck.size());
        }
        else clientProxy.swapFaceUpCards(new int[] {i}, new TrainCard[] {faceUpTrainCards[i]}, trainCardDeck.size());
        gameHistoryCommands.add(clientProxy.getCommand());

        if(getFaceUpNum() == 0 || (hasWilds(getFaceUpNum(), faceUpTrainCards))) p.setState(new NotTurnState(p));
        if (!p.isTurn()) {
            setNextTurn();
        }

        return true;
    }

    private void fillFaceUp(TrainCard[] cards) {
        int cardsPos = 0;
        for(int i = 0; i < faceUpTrainCards.length; i++) {
            if (faceUpTrainCards[i] == null && cardsPos < cards.length) faceUpTrainCards[i] = cards[cardsPos++];
        }
    }

    private boolean hasWilds(int num, TrainCard[] faceUpTrainCards) {
        int count = 0;
        for (TrainCard trainCard : faceUpTrainCards) {
            if (trainCard == TrainCard.WILD) count++;
        }
        if (count >= num) return true;
        else return false;
    }

    public void returnDestinationCards(String username, int[] rejectedCardPositions) {
        Player p = getPlayerFromUsername(username);

        destCardDeck.discard(p.removeDestCards(rejectedCardPositions));

        clientProxy.discardDestCards(p.getId(), rejectedCardPositions.length, rejectedCardPositions, destCardDeck.size());
        gameHistoryCommands.add(clientProxy.getCommand());
    }

    public boolean drawDestCards(String username) {

        if(destCardDeck.size() == 0) return false;

        Player p = getPlayerFromUsername(username);
        DestinationCard[] drawnCards = destCardDeck.drawCards(3);
        if (!p.addDestCards(drawnCards)) {
            destCardDeck.discard(drawnCards);
            return false;
        }

        clientProxy.drawDestCards(p.getId(), drawnCards.length, drawnCards, destCardDeck.size());
        gameHistoryCommands.add(clientProxy.getCommand());


        if (!p.isTurn()) {
            setNextTurn();
        }
        return true;
    }

    public boolean drawDeckTrainCard(String username) {

        if(trainCardDeck.size() == 0) return false;

        Player p = getPlayerFromUsername(username);
        TrainCard card = trainCardDeck.drawCard();
        if (!p.addTrainDeckCard(card)) {
            trainCardDeck.discard(new TrainCard[] {card});
            return false;
        }

        clientProxy.drawTrainCards(p.getId(), 1, new TrainCard[] {card}, trainCardDeck.size());
        gameHistoryCommands.add(clientProxy.getCommand());


        if (!p.isTurn()) {
            setNextTurn();
        }
        return true;
    }

    public void setNextTurn(){
        if (lastPlayer == currentPlayer) {
            clientProxy.onGameEnded();
            gameHistoryCommands.add(clientProxy.getCommand());
            return;
        }
        if (lastPlayer == -1 && players.get(currentPlayer).getNumTrains() <= 2) {
            lastPlayer = currentPlayer;
            clientProxy.setLastTurn();
            gameHistoryCommands.add(clientProxy.getCommand());
        }
        currentPlayer = (currentPlayer + 1) % players.size();
        players.get(currentPlayer).setState(new TurnState(players.get(currentPlayer)));
        clientProxy.setTurn(currentPlayer);
        gameHistoryCommands.add(clientProxy.getCommand());
    }

    public int getFaceUpNum() {
        int count = 0;
        for(TrainCard card : faceUpTrainCards) {
            if (card != null) count++;
        }
        return count;
    }

    public void calculateTotalPointsAndLongestPath(FinalGamePoints[] finalGamePointsArray){
        int winnerPlayerNumber = getLongestPathWinner(finalGamePointsArray);
        for (int i = 0; i < finalGamePointsArray.length; i++){
            FinalGamePoints fgp = finalGamePointsArray[i];
            if (fgp.getPlayerNumber() == winnerPlayerNumber) {
                fgp.setLongestPathPoints(10);
            }
            fgp.setTotalPoints();
        }
    }

    public int getLongestPathWinner(FinalGamePoints[] finalGamePointsArray){
        int longestRoute = 0;
        int winner = 0;
        for (int i = 0; i < finalGamePointsArray.length; i++){
            FinalGamePoints fgp = finalGamePointsArray[i];
            if (fgp.getLengthOfLongestPath() > longestRoute) {
                longestRoute = fgp.getLengthOfLongestPath();
                winner = fgp.getPlayerNumber();
            }

        }
        return winner;
    }

    public boolean addToAllPlayersPoints(FinalGamePoints finalGamePoints) {
        if (allPlayersPoints.containsKey(finalGamePoints.getPlayerNumber())) {
            return false;
        } else {
            allPlayersPoints.put(finalGamePoints.getPlayerNumber(), finalGamePoints);
            System.out.println("adding points for player" + finalGamePoints.getPlayerNumber());
        }

        int numberOfPlayers = players.size();
        int numberOfPointsRecorded = allPlayersPoints.values().size();
        if (numberOfPlayers == numberOfPointsRecorded){
            List<FinalGamePoints> finalGamePointsList = new ArrayList<>();
            for (Map.Entry<Integer, FinalGamePoints> onePlayersEntry : allPlayersPoints.entrySet()){
                finalGamePointsList.add(onePlayersEntry.getValue());
            }
            Collections.sort(finalGamePointsList);
            FinalGamePoints [] finalGamePointsArray = new FinalGamePoints[players.size()];
            int counter = 0;
            for (FinalGamePoints onePlayersPoints : finalGamePointsList) {
                finalGamePointsArray[counter] = onePlayersPoints;
                counter++;
            }
            calculateTotalPointsAndLongestPath(finalGamePointsArray);
            System.out.println("sending game points from ServerGameModel");
            clientProxy.updateAllPlayerFinalPoints(finalGamePointsArray);
            gameHistoryCommands.add(clientProxy.getCommand());

        }
        return true;
    }


    public Game convertToGame() {
        return this.waitingGame;
    }

    @Override
    public String getID() {
        return gameID;
    }
}
