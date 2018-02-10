package com.team.jcti.ttr.communication;

import com.team.jcti.ttr.IPresenter;
import com.team.jcti.ttr.gamelobby.GameLobbyPresenter;
import com.team.jcti.ttr.login.LoginPresenter;
import com.team.jcti.ttr.models.ClientModel;

import interfaces.IClient;
import model.Game;

/**
 * Created by Jeff on 2/2/2018.
 */

public class ClientFacade implements IClient {
    private ClientModel mClientModel = ClientModel.getInstance();

    @Override
    public void onLogin(String authToken) {
        mClientModel.setAuthToken(authToken);
        LoginPresenter presenter = (LoginPresenter) mClientModel.getActivePresenter();
        presenter.onLogin();
    }

    @Override
    public void onRegister(String authToken) {
        mClientModel.setAuthToken(authToken);
        LoginPresenter presenter = (LoginPresenter) mClientModel.getActivePresenter();
        presenter.onRegister();
    }

    @Override
    public void displayError(String message) {
        mClientModel.getActivePresenter();
    }

    @Override
    public void promptRenewSession() {

    }

    @Override
    public void onCreateGame(Game game) {
        mClientModel.setGame(game);
        GameLobbyPresenter presenter = (GameLobbyPresenter) mClientModel.getActivePresenter();
        presenter.onCreateGame();
    }

    @Override
    public void onJoinGame(String gameID) {

    }

    @Override
    public void onGetServerGameList(Game[] games) {

    }

    @Override
    public void addGametoList(Game game) {

    }

    @Override
    public void removeGameFromList(String gameID) {

    }

    @Override
    public void updateGameInList(Game game) {

    }
}
