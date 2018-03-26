package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import command.Command;
import communication.ClientProxy;

/**
 * Created by tjense25 on 2/24/18.
 */

public class ServerGameModel {

    private ClientProxy clientProxy = new ClientProxy();

    private List<Player> players;
    private Map<String, Integer> userIndexMap;
    private int currentPlayer;

    private TrainCardDeck trainCardDeck;
    private TrainCard[] faceUpTrainCards;

    List<GameHistory> gameHistory;
    private DestCardDeck destCardDeck;
    private List<Command> gameHistoryCommands;
    private boolean lastTurn = false;

    public ServerGameModel(Game game) {
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
            if(playerNumber == 0){
                player.setState(StateType.TURN_STATE);
                player.setTurn(true);
            }
            playerNumber++;
        }
    }

    public void startGame() {
        this.currentPlayer = 0; //set current player to be the first
        for (Player p : players) {
            //Draw 4 train cards from the deck
            TrainCard[] drawnTrainCards = trainCardDeck.drawCards(4);
            p.addTrainCards(drawnTrainCards);

            //Create command for player drawing 4 train cards and add to game history
            clientProxy.drawTrainCards(p.getId(), 4, drawnTrainCards, trainCardDeck.size());
            gameHistoryCommands.add(clientProxy.getCommand());

            //Draw 3 destination cards from deck
            DestinationCard[] drawnDestCards = destCardDeck.drawCards(3);
            p.addDestCards(drawnDestCards);

            //Create command for player drawing 3 train cards and add to game history
            clientProxy.drawDestCards(p.getId(), 3, drawnDestCards, destCardDeck.size());
            gameHistoryCommands.add(clientProxy.getCommand());
        }

        //Draw 5 cards from TrainCard deck and update the faceup cards with the new cards
        TrainCard[] newCards = trainCardDeck.drawCards(5); //draw 5 cards to be face up cards
        int[] pos = new int[newCards.length];
        for (int i = 0; i < newCards.length; i++) {
            faceUpTrainCards[i] = newCards[i];
            pos[i] = i;
        }

        //add the swappingFaceUpTrainCards to the gameHistoryCommand list
        clientProxy.swapFaceUpCards(pos, faceUpTrainCards, trainCardDeck.size());
        gameHistoryCommands.add(clientProxy.getCommand());

        clientProxy.updateState(players.get(0).getId(), StateType.TURN_STATE);
        gameHistoryCommands.add(clientProxy.getCommand());
    }

    public String[] getPlayers() {
        String[] playerStrings = new String[players.size()];
        for (int i = 0; i < playerStrings.length; i++) {
            playerStrings[i] = players.get(i).getUser();
        }
        return playerStrings;
    }

    public Player getPlayerFromUsername(String user) {
        for (Player p : players) {
            if (p.getUser().equals(user)) {
                return p;
            }
        }
        return null;
    }

    public Color[] getPlayerColors() {
        Color[] playerColors = new Color[players.size()];
        for (int i = 0; i < playerColors.length; i++) {
            playerColors[i] = players.get(i).getColor();
        }
        return playerColors;
    }

    public void addGameHistory(GameHistory historyObj) {
        gameHistory.add(historyObj);
    }

    public List<GameHistory> getGameHistory() {
        return gameHistory;
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
        Player userPlayer = getPlayerFromUsername(user);
        trainCardDeck.discard(userPlayer.removeTrainCards(cardPos));
        clientProxy.discardTrainCards(userPlayer.getId(), cardPos.length, cardPos, trainCardDeck.size());
        gameHistoryCommands.add(clientProxy.getCommand());

        if(!userPlayer.addRoute(routeID, length)) {
            return false;
        }

        if (userPlayer.getNumTrains() <= 2) lastTurn = true;
        clientProxy.claimedRoute(userPlayer.getId(), routeID);
        gameHistoryCommands.add(clientProxy.getCommand());

        setNextTurn();
        return true;
    }

    public DestCardDeck getDestCardDeck(){
        return destCardDeck;
    }
    public TrainCardDeck getTrainCardDeck(){
        return trainCardDeck;
    }

    public boolean drawFaceUp(String username, Integer i) {
        if(faceUpTrainCards[i] == null) return false;

        Player p = getPlayerFromUsername(username);
        p.addTrainCard(faceUpTrainCards[i]);

        clientProxy.drawTrainCards(p.getId(), 1, new TrainCard[] {faceUpTrainCards[i]}, trainCardDeck.size());
        gameHistoryCommands.add(clientProxy.getCommand());

        if(faceUpTrainCards[i] == TrainCard.WILD || p.getState() == StateType.ONE_TRAIN_PICKED_STATE){
            setNextTurn();
        }
        else{
            p.setState(StateType.ONE_TRAIN_PICKED_STATE);
        }

        faceUpTrainCards[i] = trainCardDeck.drawCard();

        clientProxy.swapFaceUpCards(new int[] {i}, new TrainCard[] {faceUpTrainCards[i]}, trainCardDeck.size());
        gameHistoryCommands.add(clientProxy.getCommand());

        return true;
    }

    public void returnDestinationCards(String username, int[] rejectedCardPositions) {
        Player p = getPlayerFromUsername(username);
        if(!p.isFirstDestPick()){
            setNextTurn();
        }
        else {
            p.setFirstDestPick();
        }

        if (rejectedCardPositions.length == 0) return;

        destCardDeck.discard(p.removeDestCards(rejectedCardPositions));

        clientProxy.discardDestCards(p.getId(), rejectedCardPositions.length, rejectedCardPositions, destCardDeck.size());
        gameHistoryCommands.add(clientProxy.getCommand());
    }

    public boolean drawDestCards(String username) {

        if(destCardDeck.size() == 0) return false;

        Player p = getPlayerFromUsername(username);
        DestinationCard[] drawnCards = destCardDeck.drawCards(3);

        p.addDestCards(drawnCards);

        clientProxy.drawDestCards(p.getId(), drawnCards.length, drawnCards, destCardDeck.size());
        gameHistoryCommands.add(clientProxy.getCommand());

        return true;
    }

    public boolean drawTrainCards(String username, Integer numberCards) {

        if(trainCardDeck.size() == 0) return false;

        Player p = getPlayerFromUsername(username);
        TrainCard[] drawnTrainCards = trainCardDeck.drawCards(1);

        p.addTrainCards(drawnTrainCards);

        if(p.getState() == StateType.ONE_TRAIN_PICKED_STATE){
            setNextTurn();
        }
        else{
            p.setState(StateType.ONE_TRAIN_PICKED_STATE);
        }

        clientProxy.drawTrainCards(p.getId(), drawnTrainCards.length, drawnTrainCards, trainCardDeck.size());
        gameHistoryCommands.add(clientProxy.getCommand());

        return true;
    }

    public void setNextTurn(){

        Player p;
        for(int i = 0; i < players.size(); i++){

            p = players.get(i);
            if(p.isTurn()){
                p.setTurn(false);
                p.setState(StateType.NOT_TURN_STATE);

                if(i == players.size()-1){
                    if (lastTurn) {
                        clientProxy.onGameEnded();
                        gameHistoryCommands.add(clientProxy.getCommand());
                        return;
                    }
                    players.get(0).setTurn(true);
                    players.get(0).setState(StateType.TURN_STATE);

                    clientProxy.updateState(players.get(0).getId(), StateType.TURN_STATE);
                    gameHistoryCommands.add(clientProxy.getCommand());
                }
                else{
                    players.get(i+1).setTurn(true);
                    players.get(i+1).setState(StateType.TURN_STATE);

                    clientProxy.updateState(players.get(i+1).getId(), StateType.TURN_STATE);
                    gameHistoryCommands.add(clientProxy.getCommand());
                }
                return;
            }
        }
    }
}
