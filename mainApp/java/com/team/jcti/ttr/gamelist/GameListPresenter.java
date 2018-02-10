package com.team.jcti.ttr.gamelist;


import android.content.Intent;

import com.team.jcti.ttr.communication.ServerProxy;
import com.team.jcti.ttr.gamelobby.GameLobbyActivity;
import com.team.jcti.ttr.IPresenter;
import com.team.jcti.ttr.models.ClientModel;
import com.team.jcti.ttr.models.GameModel;

import model.Game;

/**
 * Created by Jeff on 2/2/2018.
 */

public class GameListPresenter implements IGameListPresenter, IPresenter {
    private ClientModel mClientModel = ClientModel.getInstance();
    private GameListActivity mActivity = new GameListActivity();
    private ServerProxy mServerProxy = ServerProxy.getInstance();

    @Override
    public void create(String username, int numPlayers) {

    }

    @Override
    public void join(String username, String gameId) {

    }

    public Game[] getGames() {
        mServerProxy.getServerGames();
    @Override
    public void displayError(String message) {
    }
}
