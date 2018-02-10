package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Isaak on 2/2/2018.
 */

public class Game {

    private int numPlayers;
    private String host;
    private List<String> playerIDs;
    private String gameName;
    private String gameID;

    public Game(int numPlayers, String host, String gameName, String gameID) {
        this.numPlayers = numPlayers;
        this.host = host;
        this.gameName = gameName;
        this.gameID = gameID;
        this.playerIDs = new ArrayList<>();
        this.playerIDs.add(host);
    }

    public void joinGame(String username) {
        this.playerIDs.add(username);
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public String getHost() {
        return host;
    }

    public String getGameName() {
        return gameName;
    }

    public List<String> getPlayerIDs() {
        return playerIDs;
    }

    public String getID() {
        return gameID;
    }
}
