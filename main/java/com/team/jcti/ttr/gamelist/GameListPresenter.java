package com.team.jcti.ttr.gamelist;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.team.jcti.ttr.communication.ServerProxy;
import com.team.jcti.ttr.gamelobby.GameLobbyActivity;
import com.team.jcti.ttr.IPresenter;
import com.team.jcti.ttr.models.ClientModel;

import model.Game;
import model.User;

/**
 * Created by Jeff on 2/2/2018.
 */

public class GameListPresenter implements IGameListPresenter, IPresenter{
    private ClientModel mClientModel = ClientModel.getInstance();
    private GameListActivity mActivity = new GameListActivity();
    private ServerProxy mServerProxy = ServerProxy.getInstance();
    private GameListActivity mGameListActivity;

    public GameListPresenter(GameListActivity activity) {
        mClientModel.setActivePresenter(this);
        this.mGameListActivity = activity;

    }

    @Override
    public void create() {
        Intent intent = new Intent(mActivity, GameLobbyActivity.class);
        mActivity.startActivity(intent);
    }

    @Override
    public void join(String username, String gameId) {
        mServerProxy.joinGame(username, gameId);
    }

    public void getGames() {
        mServerProxy.getServerGames();
    }

    @Override
    public void displayError(String message) {
    }

    public void displayGames(Game[] games) {
        mActivity.setGamesList(games);
    }

    public User getUser() {
        return mClientModel.getUser();
    }

    public void postJoin() {
        Intent intent = new Intent(mActivity, GameLobbyActivity.class);
        mActivity.startActivity(intent);
    }
}
