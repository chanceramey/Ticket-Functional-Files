package com.team.jcti.ttr.gamelist;


/**
 * Created by Jeff on 2/2/2018.
 */

public interface IGameListPresenter {
    void create(String username, int numPlayers);
    void join(String username, String gameId);
}
