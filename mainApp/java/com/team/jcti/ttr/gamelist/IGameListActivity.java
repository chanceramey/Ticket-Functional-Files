package com.team.jcti.ttr.gamelist;


import java.util.List;

import model.Game;

/**
 * Created by Jeff on 2/2/2018.
 */

public interface IGameListActivity {
    void onCreateGame();
    void setGamesList(List<Game> games);
    void onJoin();
    void toast(String message);
}
