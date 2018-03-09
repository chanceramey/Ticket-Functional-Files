package com.team.jcti.ttr.game;

import com.google.gson.JsonParser;
import com.team.jcti.ttr.IPresenter;
import com.team.jcti.ttr.communication.ServerProxy;
import com.team.jcti.ttr.login.LoginActivity;
import com.team.jcti.ttr.models.ClientGameModel;
import com.team.jcti.ttr.models.ClientModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import model.Game;

/**
 * Created by Chance on 3/7/18.
 */

public class GamePresenter implements IPresenter{

    private ClientModel mClientModel = ClientModel.getInstance();
    private ServerProxy mServerProxy = ServerProxy.getInstance();
    private ClientGameModel mClientGameModel = ClientGameModel.getInstance();
    private GameActivity mGameActivity;

    public GamePresenter(GameActivity gameActivity){
        mClientModel.setActivePresenter(this);
        this.mGameActivity = gameActivity;
    }

    public void claimRoute(String routeId) {
        mServerProxy.claimRoute(mClientModel.getAuthToken(), mClientModel.getGame().getID(), routeId);
    }

    @Override
    public void displayError(String message) {
        mGameActivity.toast(message);
    }

    @Override
    public void updateGame(Game game) {

    }

    @Override
    public void update() {

    }
}
