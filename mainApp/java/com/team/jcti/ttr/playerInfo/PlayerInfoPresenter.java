package com.team.jcti.ttr.playerInfo;

import com.team.jcti.ttr.IPresenter;
import com.team.jcti.ttr.models.ClientGameModel;

import java.util.Observable;
import java.util.Observer;

import model.Game;

/**
 * Created by Jeff on 3/7/2018.
 */

public class PlayerInfoPresenter implements IPresenter, Observer {
    PlayerInfoActivity mActivity;
    ClientGameModel gameModel = ClientGameModel.getInstance();
    public PlayerInfoPresenter(PlayerInfoActivity activity) {
        mActivity = activity;
        gameModel.setActivePresenter(this);
    }

    @Override
    public void displayError(String message) {
        mActivity.toast(message);
    }

    @Override
    public void updateGame(Game game) {

    }

    @Override
    public void update() {
        mActivity.setPlayerInfo(gameModel.getPlayers());
    }


    @Override
    public void update(Observable observable, Object o) {
        update();
    }
}
