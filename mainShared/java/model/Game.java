package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Isaak on 2/2/2018.
 */

public class Game {

    private int numPlayers;
    private String host;
    private transient Map<String, String> playerIDs = new HashMap<>();
    private List<String> players = new ArrayList<>();
    private String gameName;
    private String gameID;

    public Game(int numPlayers, String user, String gameName, String gameID, String auth) {
        this.numPlayers = numPlayers;
        this.host = user;
        this.gameName = gameName;
        this.gameID = gameID;
        this.playerIDs.put(auth, user);
        this.players.add(user);
    }

    public void joinGame(String username, String auth) {
        this.playerIDs.put(auth, username);
        players.add(username);
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

    public Map<String, String> getPlayerIDs() {
        return playerIDs;
    }

    public List<String> getPlayers() { return players; }

    public String getID() {
        return gameID;
    }

    public String getNumPlayersString() {
        return String.format("%d/%d", players.size(), numPlayers);
    }

    public String getPlayersNames() {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (String player : players) {
            sb.append(String.format("%d. %s\n",i ,player));
            i++;
        }
        while (i <= numPlayers) {
            sb.append(String.format("%d. \n", i));
            i++;
        }
        return sb.toString();
    }
}
