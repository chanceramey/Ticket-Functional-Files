package com.team.jcti.ttr.finalScreen;

import com.team.jcti.ttr.IGamePresenter;
import com.team.jcti.ttr.IPresenter;
import com.team.jcti.ttr.models.ClientGameModel;
import com.team.jcti.ttr.models.Route;
import com.team.jcti.ttr.state.State;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import model.Game;
import model.Player;

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
        mActivity.startRecyclerView(mClientGameModel.getPlayers());
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

    public int getRoutePoints(List<String> routes) {
        if (routes.size() <= 0) {
            return 0;
        }
        int points = 0;
        Route r;
        for (String s : routes) {
            r = mClientGameModel.getRouteFromID(s);
            points += r.getPointValue();
        }
        return points;
    }

    @Override
    public void displayError(String message) {
        mActivity.toast(message);
    }

    @Override
    public void setState(State state) {
        //do nothing?
    }

    @Override
    public void drawDestCards() {

    }

    @Override
    public void update() {
        startRecyclerView();
    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
