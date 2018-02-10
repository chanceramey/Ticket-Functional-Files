package com.team.jcti.ttr.models;
/**
 * Created by Jeff on 2/2/2018.
 */

public class GameModel {
    String gameId;
    String host;
    String players; // number of players already in the game
    String playersAllowed; // number of players that can join the game

    public GameModel(String gameId, String host, String players, String playersAllowed) {
        this.gameId = gameId;

        this.host = host;
        this.players = players;
        this.playersAllowed = playersAllowed;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPlayers() {
        return players;
    }

    public void setPlayers(String players) {
        this.players = players;
    }

    public String getPlayersAllowed() {
        return playersAllowed;
    }

    public void setPlayersAllowed(String playersAllowed) {
        this.playersAllowed = playersAllowed;
    }


}
