package com.team.jcti.ttr.models;

import java.util.ArrayList;
import java.util.List;

import model.Color;
import model.Player;

/**
 * Created by Jeff on 2/2/2018.
 */

public class ClientGameModel {

    private static ClientGameModel SINGLETON;
    public static ClientGameModel getInstance() {
        if(SINGLETON == null) {
            SINGLETON = new ClientGameModel();
        }
        return SINGLETON;
    }

    boolean active = false;

    private List<Player> players;
    private int userPlayer;
    private int gameHistoryPosition;

    public void startGame(List<String> playerStrings) {
        this.players = new ArrayList<>();
        Color[] colors = Color.values();
        for (int i = 0; i < playerStrings.size(); i++) {
            if (playerStrings.get(i).equals(ClientModel.getInstance().getUsername())) userPlayer = i;
            Player player = new Player(playerStrings.get(i), colors[i], i);
            players.add(player);
        }
        active = true;
        gameHistoryPosition = 0;
    }
}
