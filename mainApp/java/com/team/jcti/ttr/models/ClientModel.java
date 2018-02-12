package com.team.jcti.ttr.models;

import com.team.jcti.ttr.IPresenter;
import com.team.jcti.ttr.communication.Poller;
import com.team.jcti.ttr.gamelobby.GameLobbyPresenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

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

    private String authToken;
    private Game activeGame;
    private List<Game> waitingGames;
    private GameLobbyPresenter lobbyPresenter;
    private IPresenter activePresenter;
    private Map<String, String> properForms;

    private ClientModel() {
        this.authToken = null;
        this.activeGame = null;
        this.waitingGames = new ArrayList<>();
        this.properForms = new HashMap<>();
        properForms.put("username", ".+");
        properForms.put("password", ".+");
        properForms.put("ipAddress", "\\d+\\.\\d+\\.\\d+\\.\\d+");
        properForms.put("portNumber", "\\d+");

        //Set Poller to go every second
        Timer timer = new Timer();
        int SECOND = 1000; //1,000 milliseconds in one second
        timer.schedule(new Poller(this), SECOND);
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

    public void setWaitingGames(List<Game> games) {
        this.waitingGames = games;
    }

    public List<Game> getWaitingGames() { return waitingGames;}

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Map<String, String> getProperForms() {
        return properForms;
    }
}
