package com.team.jcti.ttr.message;

import com.team.jcti.ttr.IPresenter;
import com.team.jcti.ttr.communication.ServerProxy;
import com.team.jcti.ttr.models.ClientGameModel;
import com.team.jcti.ttr.models.ClientModel;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import model.Game;
import model.GameHistory;

/**
 * Created by Jeff on 2/27/2018.
 */

public class MessagePresenter implements IMessagePresenter, IPresenter, Observer {
    private ClientModel mClientModel = ClientModel.getInstance();
    private ServerProxy mServerProxy = ServerProxy.getInstance();
    private IMessageActivity mActivity;
    private MessageFragment mFragment;
    private Game mGame;
    private ClientGameModel mActiveGame = ClientGameModel.getInstance();

    public MessagePresenter(IMessageActivity activity) {
        this.mActivity = activity;
        mActiveGame.setActivePresenter(this);
        mGame = mClientModel.getGame();
    }

    @Override
    public void displayError(String message) {
        mActivity.toast(message);
    }

    @Override
    public void updateGame(Game game) {
//        mClientModel.setGame(game);
//        mGame = game;
    }
    @Override
    public void update() {
        List<GameHistory> hist = mActiveGame.getGameHistory();
        mFragment.setHistory(mActiveGame.getGameHistory());
    }

    @Override
    public void update(Observable o, Object arg) {update();}

    @Override
    public void sendMessage(String message) {
        GameHistory historyObj = new GameHistory(null, message);
        String auth = mClientModel.getAuthToken();
//        String gameId = mActiveGame.getGameId();
        String gameId = "ab123"; // for testing
        mServerProxy.sendMessage(auth, gameId, historyObj);
    }

    public void setFragment(MessageFragment frag) {
        mFragment = frag;
    }

    @Override
    public void updateGameHistory(GameHistory gameHistory) {
        mActiveGame.addGameHistoryObj(gameHistory);
    }

}
