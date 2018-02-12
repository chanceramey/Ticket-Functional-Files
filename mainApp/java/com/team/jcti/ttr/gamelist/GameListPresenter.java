package com.team.jcti.ttr.gamelist;

import com.team.jcti.ttr.communication.ServerProxy;
import com.team.jcti.ttr.IPresenter;
import com.team.jcti.ttr.models.ClientModel;

import java.util.List;

import model.Game;

/**
 * Created by Jeff on 2/2/2018.
 */

public class GameListPresenter implements IGameListPresenter, IPresenter {
    private ClientModel mClientModel = ClientModel.getInstance();
    private IGameListActivity mActivity;
    private ServerProxy mServerProxy = ServerProxy.getInstance();

    public GameListPresenter(IGameListActivity activity) {
        this.mActivity = activity;
        mClientModel.setActivePresenter(this);
        mServerProxy.getServerGames(mClientModel.getAuthToken());
    }

    @Override
    public void join(String gameId) {
        mServerProxy.joinGame(mClientModel.getAuthToken(), gameId);
    }

    public List<Game> getGames() {
       return mClientModel.getWaitingGames();
    }

    @Override
    public void displayError(String message) {
        mActivity.toast(message);
    }

    @Override
    public void updateGame(Game game) {
        for(int i = 0; i < mClientModel.getWaitingGames().size(); i++) {
            if (game.getID().equals(mClientModel.getWaitingGames().get(i))) {
                mClientModel.getWaitingGames().remove(i);
                mClientModel.getWaitingGames().set(i, game);
                return;
            }
        }
        update();
    }

    public void onJoinGame() {
        mActivity.onJoin();
    }

    @Override
    public void update() {
        mActivity.setGamesList(mClientModel.getWaitingGames());
    }
}
