package com.team.jcti.ttr.message;

import com.team.jcti.ttr.IGamePresenter;
import com.team.jcti.ttr.communication.ServerProxy;
import com.team.jcti.ttr.models.ClientGameModel;
import com.team.jcti.ttr.models.ClientModel;

import java.util.Observable;
import java.util.Observer;

import model.GameHistory;

/**
 * Created by Jeff on 2/27/2018.
 */

public class MessagePresenter implements IMessagePresenter, IGamePresenter, Observer {
    private ClientModel mClientModel = ClientModel.getInstance();
    private ServerProxy mServerProxy = ServerProxy.getInstance();
    private IMessageActivity mActivity;
    private MessageFragment mFragment;
    private ClientGameModel mActiveGame = ClientGameModel.getInstance();

    /**
     *
     * @param activity
     */
    public MessagePresenter(IMessageActivity activity) {
        this.mActivity = activity;
        mActiveGame.setActivePresenter(this);
    }

    @Override
    public void displayError(String message) {
        mActivity.toast(message);
    }


    @Override
    public void update() {
        mFragment.setHistory(mActiveGame.getGameHistory());
    }

    @Override
    public void onGameEnded() {
        mActivity.enterFinalGameScreen();
    }


    @Override
    public void drawDestCards() {
        //do nothing
    }

    @Override
    public void update(Observable o, Object arg) {update();}

    @Override
    public void sendMessage(String message) {
        GameHistory historyObj = new GameHistory(null, message);
        String auth = mClientModel.getAuthToken();
        String gameId = mActiveGame.getID();
        mServerProxy.sendMessage(auth, gameId, historyObj);
    }

    public void setFragment(MessageFragment frag) {
        mFragment = frag;
    }

    @Override
    public void updateGameHistory(GameHistory gameHistory) {
        mActiveGame.addGameHistoryObj(gameHistory);
        mFragment.setHistory(mActiveGame.getGameHistory());
    }

}
