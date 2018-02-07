package com.team.jcti.ttr.gamelist;


import com.team.jcti.ttr.models.ClientModel;

/**
 * Created by Jeff on 2/2/2018.
 */

public class GameListPresenter implements IGameListPresenter {
    private ClientModel mClientModel = ClientModel.getInstance();
    private GameListActivity mActivity = new GameListActivity();
    @Override
    public void create(String username, int numPlayers) {

    }

    @Override
    public void join(String username, String gameId) {

    }
}
