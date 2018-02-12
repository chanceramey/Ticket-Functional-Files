package com.team.jcti.ttr.communication;

import com.team.jcti.ttr.IPresenter;
import com.team.jcti.ttr.gamelist.GameListPresenter;
import com.team.jcti.ttr.gamelobby.GameLobbyPresenter;
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

    }

    @Override
    public void onRegister(String authToken) {

    }

    @Override
    public void displayError() {
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
    public void onJoinGame(Game game) {
        mClientModel.setGame(game);
        GameListPresenter presenter = (GameListPresenter) mClientModel.getActivePresenter();
        presenter.postJoin();
    }

    @Override
    public void onGetServerGameList(Game[] games) {
        mClientModel.setWaitingGames(games);
        GameListPresenter presenter = (GameListPresenter) mClientModel.getActivePresenter();
        presenter.displayGames(games);
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
