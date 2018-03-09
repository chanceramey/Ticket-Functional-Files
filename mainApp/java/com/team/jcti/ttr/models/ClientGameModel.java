package com.team.jcti.ttr.models;

import com.team.jcti.ttr.IPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import model.Color;
import model.GameHistory;

import model.Game;
import model.Player;

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
    private IPresenter activePresenter;

    public void startGame(Game game) {
        this.gameId = game.getID();
        List<String> playerStrings = game.getPlayers();
        this.players = new ArrayList<>();
        Color[] colors = Color.values();
        for (int i = 0; i < playerStrings.size(); i++) {
            if (playerStrings.get(i).equals(ClientModel.getInstance().getUsername())) userPlayer = i;
            Player player = new Player(playerStrings.get(i), colors[i], i);
            players.add(player);
        }
        this.active = true;
        gameHistoryPosition = 0;
    }

    public void setActivePresenter(IPresenter presenter) {
        activePresenter = presenter;
    }
    public IPresenter getActivePresenter() {
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

    //ikes

    public Player getPlayerById(int id){ //checkback
        for(Player player : players){
            if(player.getId() == id){

                return player;
            }
        }
        return null;
    }
}
