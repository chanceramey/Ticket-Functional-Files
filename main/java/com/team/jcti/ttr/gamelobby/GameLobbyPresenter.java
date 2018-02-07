package com.team.jcti.ttr.gamelobby;

import com.team.jcti.ttr.communication.ServerProxy;
import com.team.jcti.ttr.models.ClientModel;

import java.util.Observable;
import java.util.Observer;

import model.Game;

/**
 * Created by Tanner Jensen on 2/4/2018.
 */

public class GameLobbyPresenter implements Observer {
    private GameLobbyActivity mGameLobbyActivity;
    private ClientModel mClientModel = ClientModel.getInstance();
    private ServerProxy mServerProxy = ServerProxy.getInstance();

    public GameLobbyPresenter(GameLobbyActivity activity) {
        mClientModel.setLobbyPresenter(this);
        this.mGameLobbyActivity = activity;
    }

    public boolean hasGame() {
        if (mClientModel.getGame() == null)
            return false;
        else
            return true;
    }

    public void createNewGame(int numPlayers, String GameName) {
        String auth = mClientModel.getAuth();
        mServerProxy.createGame(numPlayers, GameName, auth);
    }

    public void onCreateGame() {
        mGameLobbyActivity.switchFragments();
    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
