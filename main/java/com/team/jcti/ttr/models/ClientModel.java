package com.team.jcti.ttr.models;

import com.team.jcti.ttr.gamelobby.GameLobbyPresenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.Game;
import model.User;

/**
 * Created by Jeff on 2/2/2018.
 */

public class ClientModel {

    private static ClientModel SINGLETON;
    public static ClientModel getInstance() {
        if(SINGLETON == null) {
            SINGLETON = new ClientModel();
        }
        return SINGLETON;
    }

    private User user;
    private String authToken;
    private Game activeGame;
    private List<Game> waitingGames;
    private GameLobbyPresenter lobbyPresenter;

    private ClientModel() {
        this.user = null;
        this.authToken = "test";
        this.activeGame = null;
        this.waitingGames = new ArrayList<>();
    }

    public Game getGame() {
        return activeGame;
    }

    public void setGame(Game game) {
        this.activeGame = game;
    }

    public void setLobbyPresenter(GameLobbyPresenter presenter) {
        this.lobbyPresenter = presenter;
    }

    public GameLobbyPresenter getLobbyPresenter() {
        return this.lobbyPresenter;
    }

    public String getAuth() {
        return authToken;
    }

    public void setWaitingGames(Game[] games) {
        waitingGames = Arrays.asList(games);
    }

}
