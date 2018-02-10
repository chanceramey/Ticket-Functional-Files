package com.team.jcti.ttr.gamelist;


/**
 * Created by Jeff on 2/2/2018.
 */

public interface IGameListActivity {
    void onCreate(String username, int numPlayers);
    void onJoin(String username, String gameId);
}
