package com.team.jcti.ttr.communication;

import com.team.jcti.ttr.IPresenter;
import com.team.jcti.ttr.gamelist.GameListPresenter;
import com.team.jcti.ttr.gamelobby.GameLobbyPresenter;
import com.team.jcti.ttr.login.LoginPresenter;
import com.team.jcti.ttr.models.ClientModel;

import java.util.Arrays;

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
       IPresenter activePresenter = mClientModel.getActivePresenter();
       activePresenter.displayError(message);
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
    public void onJoinGame(Game game) {
        mClientModel.setGame(game);
        GameListPresenter presenter = (GameListPresenter) mClientModel.getActivePresenter();
        presenter.onJoinGame();
    }

    @Override
    public void onLeaveGame() {
        GameLobbyPresenter presenter = (GameLobbyPresenter) mClientModel.getActivePresenter();
        presenter.onLeaveGame();
    }


    @Override
    public void onGetServerGameList(Game[] games) {
        mClientModel.setWaitingGames(Arrays.asList(games));
        mClientModel.getActivePresenter().update();
    }

    @Override
    public void addGametoList(Game game) {
        mClientModel.getWaitingGames().add(game);
        mClientModel.getActivePresenter().update();
    }

    @Override
    public void removeGameFromList(String gameID) {
        for(int i = 0; i < mClientModel.getWaitingGames().size(); i++) {
            if (gameID.equals(mClientModel.getWaitingGames().get(i))) {
                mClientModel.getWaitingGames().remove(i);
                return;
            }
        }
        mClientModel.getActivePresenter().update();
    }

    @Override
    public void updateGame(Game game) {
       IPresenter presenter = mClientModel.getActivePresenter();
       presenter.updateGame(game);
    }
}
