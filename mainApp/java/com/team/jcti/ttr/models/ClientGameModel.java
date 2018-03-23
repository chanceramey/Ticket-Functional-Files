package com.team.jcti.ttr.models;

import com.team.jcti.ttr.IGamePresenter;
import com.team.jcti.ttr.IPresenter;
import com.team.jcti.ttr.game.GamePresenter;
import com.team.jcti.ttr.message.MessagePresenter;
import com.team.jcti.ttr.state.NotTurnState;
import com.team.jcti.ttr.state.OneTrainPickedState;
import com.team.jcti.ttr.state.State;
import com.team.jcti.ttr.state.TurnState;
import com.team.jcti.ttr.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import model.Color;
import model.DestinationCard;
import model.GameHistory;

import model.Game;
import model.Player;
import model.StateType;
import model.TrainCard;

/**
 * Created by Jeff on 2/2/2018.
 */

public class ClientGameModel extends Observable {

    private static ClientGameModel SINGLETON;
    public static ClientGameModel getInstance() {
        if(SINGLETON == null) {
            SINGLETON = new ClientGameModel();
        }
        return SINGLETON;
    }

    boolean active = false;

    private Board board = null;
    private List<Player> players;
    private String gameId;
    private int userPlayer;
    private int gameHistoryPosition;
    private List<GameHistory> gameHistoryArr = new ArrayList<>();
    private IGamePresenter activePresenter;
    private TrainCard[] faceUpCards;
    private Player currentPlayer;
    private int destDeckSize;
    private int trainDeckSize;
    private State state;

    public void startGame(Game game) {
        this.gameId = game.getID();
        List<String> playerStrings = game.getPlayers();
        this.players = new ArrayList<>();
        faceUpCards = new TrainCard[5];
        for (int i = 0; i < faceUpCards.length; i++){
            faceUpCards[i] = TrainCard.WILD;
        }
        Color[] colors = Color.values();
        for (int i = 0; i < playerStrings.size(); i++) {
            if (playerStrings.get(i).equals(ClientModel.getInstance().getUsername())) userPlayer = i;
            Player player = new Player(playerStrings.get(i), colors[i], i);
            players.add(player);
        }
        this.active = true;
        gameHistoryPosition = 0;
        currentPlayer = players.get(userPlayer);

        trainDeckSize = 240;
        destDeckSize = 30;
    }

    public IGamePresenter getActivePresenter() {
        return activePresenter;
    }

    public List<GameHistory> getGameHistory() {
        return gameHistoryArr;
    }

    public void addGameHistoryObj (GameHistory gameHistory) {
        gameHistoryArr.add(gameHistory);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public boolean isActive() {
        return active;
    }

    public int getGameHistoryPosition() {
        return gameHistoryPosition;
    }

    public String getGameID() {
        return gameId;
    }

    public void increment(int numCommands) {
        gameHistoryPosition += numCommands;
    }

    public List<TrainCard> getPlayersTrainCards() {
        return players.get(userPlayer).getTrainCards();
    }

    public List<DestinationCard> getPlayersDestCards() {
        return players.get(userPlayer).getDestCards();
    }

    public boolean isFirstTurn(){
        return players.get(userPlayer).isFirstDestPick();
    }

    public Player getPlayerById(int id){
        for(Player player : players){
            if(player.getId() == id){

                return player;
            }
        }
        return null;
    }

    public TrainCard[] getFaceUpCards() {
        return faceUpCards;
    }

    public void drawDestCards(Integer playerIndex, Integer numCards, DestinationCard[] cards, Integer deckSize) {
        Player player = players.get(playerIndex);
        if (playerIndex == userPlayer) {
            player.addDestCards(cards);
            activePresenter.drawDestCards();
        }
        else {
            player.addDestCards(numCards);
        }

        String user = player.getUser();
        String message = String.format("***%s drew %d destination cards", user, numCards);
        gameHistoryArr.add(new GameHistory(user, message));

        destDeckSize = deckSize;
        activePresenter.update();
    }

    public void setActivePresenter(IGamePresenter gamePresenter) {
        this.activePresenter = gamePresenter;
    }

    public List<DestinationCard> getUsersDestCard() {
       List<DestinationCard> destCards = players.get(userPlayer).getDestCards();
        return destCards.subList(destCards.size() - 3, destCards.size());
    }

    public Player getUserPlayer() {
       return players.get(userPlayer);
    }

    public void receiveMessage(GameHistory gameHistory) {
        gameHistory.setChat(true);
        gameHistoryArr.add(gameHistory);
        if(activePresenter.getClass() == MessagePresenter.class) {
            activePresenter.update();
        }
        else activePresenter.displayError(gameHistory.getUser() + " sent a message!");
    }

    public void drawTrainCards(Integer player, Integer numberCards, TrainCard[] cards, Integer deckSize) {
        Player p = players.get(player);
        p.addTrainCards(cards);
        String user = p.getUser();

        String message = String.format("***%s drew %d Train card***", user, numberCards);
        GameHistory drewCard = new GameHistory(user, message);
        addGameHistoryObj(drewCard);
        trainDeckSize = deckSize;

        activePresenter.update();
    }

    public void discardTrainCards(Integer player, Integer numberCards, int[] pos, Integer deckSize) {
        Player p = players.get(player);
        p.removeTrainCards(pos);
        String user = p.getUser();

        String message = String.format("***%s discarded %d Train Cards***", user, numberCards);
        GameHistory discarded = new GameHistory(user, message);
        addGameHistoryObj(discarded);

        trainDeckSize = deckSize;

        activePresenter.update();
    }


    public void discardDestCards(Integer player, Integer numCards, int[] pos, Integer deckSize) {
        if(numCards == 0) return; //don't do anything if they didn't discard any cards

        Player p = players.get(player);
        if (player == userPlayer) {
            p.removeDestCards(pos);
        }
        else {
            p.removeDestCards(numCards);
        }

        if(p.isFirstDestPick()){
            p.setFirstDestPick(); //to false
        }

        String user = p.getUser();
        String message = String.format("***%s discarded %d Destination Cards***", user, numCards);
        gameHistoryArr.add(new GameHistory(user, message));

        this.destDeckSize = deckSize;

        activePresenter.update();
    }

    public int getDestDeckSize() {
        return destDeckSize;
    }

    public int getTrainDeckSize() { return trainDeckSize; }

    public void removeDestCardsFromDeck(int i) {
        destDeckSize -= i;
        activePresenter.update();
    }

    public void swapFaceUpCards(int[] pos, TrainCard[] cards, Integer deckSize) {
        int i = 0;
        for (int index : pos) {
            faceUpCards[index] = cards[i];
            i++;
        }

        this.trainDeckSize = deckSize;

        activePresenter.update();

    }

    public Board getBoard() {
        return board;
    }

    public void initializeBoard(String JSONCities, String JSONRoutes) {
        this.board = new Board(JSONCities, JSONRoutes);
    }

    public void claimRoute(Integer player, String routeID) {
        Player p = players.get(player);
        board.claimRoute(p, routeID);
        String user = p.getUser();
        String message = String.format("***%s claimed a route %s***", user, routeID);
        GameHistory gameHistory = new GameHistory(user, message);
        addGameHistoryObj(gameHistory);
        activePresenter.update();
    }

    public int getPlayersNumTrainCards(TrainCard card) {
        Player p = getUserPlayer();
        return p.getCountOfCardType(card);
    }
    public void updateState(Integer player, StateType state){
        Player p = players.get(player);
        p.setState(state);

        if(state == StateType.TURN_STATE){
            this.state = new TurnState((GamePresenter) activePresenter);
        }
        else if(state == StateType.NOT_TURN_STATE){
            this.state = new NotTurnState((GamePresenter) activePresenter);
        }
        else{
            this.state = new OneTrainPickedState((GamePresenter) activePresenter);
        }

        if(player == userPlayer){
            activePresenter.setState(this.state);
            activePresenter.displayError("It's your turn");
        }
    }

    public Route getRouteFromID(String routeId) {
        return board.getRouteFromID(routeId);
    }

    public int[] getClaimingCards(int length, TrainCard color) {
        return players.get(userPlayer).getRouteClaimingCards(length, color);
    }

    public State getState(){
        return state;
    }
}
