package com.team.jcti.ttr.models;

import com.team.jcti.ttr.IPresenter;
import com.team.jcti.ttr.gamelobby.GameLobbyPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private IPresenter activePresenter;
    private Map<String, String> properForms;
    private String AuthToken;

    private ClientModel() {
        this.user = null;
        this.authToken = "test";
        this.activeGame = null;
        this.waitingGames = new ArrayList<>();
        this.properForms = new HashMap<>();
        properForms.put("username", ".+");
        properForms.put("password", ".+");
        properForms.put("ipAddress", "\\d+\\.\\d+\\.\\d+\\.\\d+");
        properForms.put("portNumber", "\\d+");
    }

    public Game getGame() {
        return activeGame;
    }

    public void setGame(Game game) {
        this.activeGame = game;
    }

    public void setActivePresenter(IPresenter presenter) {
        this.activePresenter = presenter;
    }

    public IPresenter getActivePresenter() {
        return this.activePresenter;
    }

    public String getAuth() {
        return authToken;
    }

    public Map<String, String> getProperForms() {
        return properForms;
    }

    public String getAuthToken() {
        return AuthToken;
    }

    public void setAuthToken(String authToken) {
        AuthToken = authToken;
    }
}
