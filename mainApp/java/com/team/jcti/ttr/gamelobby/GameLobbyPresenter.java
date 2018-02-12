package com.team.jcti.ttr.gamelobby;

import com.team.jcti.ttr.IPresenter;
import com.team.jcti.ttr.communication.ServerProxy;
import com.team.jcti.ttr.models.ClientModel;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

import model.Game;

/**
 * Created by Tanner Jensen on 2/4/2018.
 */

public class GameLobbyPresenter implements IPresenter, Serializable {
    private GameLobbyActivity mGameLobbyActivity;
    private GameLobbyFragment mGameLobbyFragment;
    private ClientModel mClientModel = ClientModel.getInstance();
    private ServerProxy mServerProxy = ServerProxy.getInstance();
    private Game game;

    public GameLobbyPresenter(GameLobbyActivity activity) {
        mClientModel.setActivePresenter(this);
        this.mGameLobbyActivity = activity;
        this.game = mClientModel.getGame();
    }

    public void setFragment(GameLobbyFragment gameLobbyFragment) {
        this.mGameLobbyFragment = gameLobbyFragment;
    }

    public boolean hasGame() {
        if (mClientModel.getGame() == null)
            return false;
        else
            return true;
    }

    public void createNewGame(int numPlayers, String gameName) {
        if (numPlayers == 0 || gameName.equals("")) {
            this.mGameLobbyActivity.toast("Invalid input, please try again");
            return;
        }
        String auth = mClientModel.getAuthToken();
        mServerProxy.createGame(numPlayers, gameName, auth);
    }

    public void onCreateGame() {
        this.game = mClientModel.getGame();
        mGameLobbyActivity.switchFragments();
    }

    @Override
    public void displayError(String message) {
        mGameLobbyActivity.toast(message);
    }

    @Override
    public void updateGame(Game game) {
        mClientModel.setGame(game);
        mGameLobbyFragment.updateGameInfo();
    }

    public Game getGame() {
        return game;
    }

    boolean isHost() {
        if (game.getHost().equals(mClientModel.getAuthToken())) return true;
        else return false;
    }

    public void update() {
        if(mGameLobbyFragment != null) {
            mGameLobbyFragment.updateGameInfo();
        }
    }

    public void leaveGame() {
        mClientModel.setGame(null);
        mServerProxy.leaveGame(mClientModel.getAuthToken(), game.getID());
    }

    public void onLeaveGame() {
        mGameLobbyActivity.enterGameList();
    }
}
