package com.team.jcti.ttr.finalScreen;

import com.team.jcti.ttr.IGamePresenter;
import com.team.jcti.ttr.models.ClientGameModel;
import com.team.jcti.ttr.models.ClientModel;
import com.team.jcti.ttr.models.Route;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import model.playerStates.Player;

/**
 * Created by Jeff on 3/22/2018.
 */

public class FinalScreenPresenter implements IGamePresenter, Observer{
    ClientGameModel mClientGameModel = ClientGameModel.getInstance();
    FinalScreenActivity mActivity;
    public FinalScreenPresenter(FinalScreenActivity activity) {
        mClientGameModel.setActivePresenter(this);
        mActivity = activity;

    }

    public void startRecyclerView() {
        if (mClientGameModel.isFinalPointsReceived()) {
            mActivity.startRecyclerView(mClientGameModel.getAllPlayersFinalPoints());
        }
    }

    public String getWinner() {
        List<Player> playerList = mClientGameModel.getPlayers();
        Player winner = playerList.get(0);
        for (Player p : playerList) {
            if (p.getPoints() > winner.getPoints()) {
                winner = p;
            }
        }
        return winner.getUser();
    }

    public List<Player> getPlayers(){
        return mClientGameModel.getPlayers();
    }

    @Override
    public void displayError(String message) {
        mActivity.toast(message);
    }


    @Override
    public void drawDestCards() {

    }

    @Override
    public void update() {
        startRecyclerView();
    }

    @Override
    public void onGameEnded() {

    }


    @Override
    public void update(Observable observable, Object o) {

    }

    public void playAgain() {
        mClientGameModel.clearGame();
        ClientModel.getInstance().setGame(null);
        mActivity.enterGameListActivity();
    }
}
