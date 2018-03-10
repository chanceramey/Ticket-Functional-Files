package com.team.jcti.ttr.models;

import com.team.jcti.ttr.IGamePresenter;
import com.team.jcti.ttr.IPresenter;
import com.team.jcti.ttr.game.GamePresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import model.Color;
import model.DestinationCard;
import model.GameHistory;

import model.Game;
import model.Player;
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

    private List<Player> players;
    private String gameId;
    private int userPlayer;
    private int gameHistoryPosition;
    private List<GameHistory> gameHistoryArr = new ArrayList<>();
    private IGamePresenter activePresenter;
    private int turnPosition = 0; // keeps track of who's turn it is by their position in the array
    private TrainCard[] faceUpCards;

    public boolean isMyTurn() {
        return myTurn;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    private boolean myTurn;

    public void startGame(Game game) {
        this.gameId = game.getID();
        List<String> playerStrings = game.getPlayers();
        this.players = new ArrayList<>();
        faceUpCards = new TrainCard[5];
        Color[] colors = Color.values();
        for (int i = 0; i < playerStrings.size(); i++) {
            if (playerStrings.get(i).equals(ClientModel.getInstance().getUsername())) userPlayer = i;
            Player player = new Player(playerStrings.get(i), colors[i], i);
            players.add(player);
        }
        this.active = true;
        gameHistoryPosition = 0;
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

    public String getGameId() {
        return gameId;
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

    public void moveTurnPosition() {
        turnPosition++;
        if (turnPosition == players.size()) {
            turnPosition = 0;
        }
        checkTurn();
    }

    public void checkTurn() {
        if (turnPosition == userPlayer) {
            myTurn = true;
            activePresenter.displayError("It's your turn");
        } else {
            myTurn = false;
        }
    }

    public void updateFaceUpCards(int[] pos, TrainCard[] newCards) {

        for(int position : pos){
            faceUpCards[position] = newCards[position];
        }
    }

    public TrainCard[] getFaceUpCards() {

        return faceUpCards;
    }

    public void drawDestCards(Integer playerIndex, Integer numCards, DestinationCard[] cards) {
        Player player = players.get(playerIndex);
        if (playerIndex == userPlayer) {
            player.addDestCards(cards);
            activePresenter.drawDestCards();
        }
        else {
            player.addDestCards(numCards);
        }
        activePresenter.update();
    }

    public void setActivePresenter(IGamePresenter gamePresenter) {
        this.activePresenter = gamePresenter;
    }

    public List<DestinationCard> getUsersDestCard() {
       return players.get(userPlayer).getDestCards();
    }

    public Player getUserPlayer() {
       return players.get(userPlayer);
    }
}
