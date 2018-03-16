package com.team.jcti.ttr.game;

import android.view.MenuItem;

import com.team.jcti.ttr.IGamePresenter;
import com.team.jcti.ttr.IPresenter;
import com.team.jcti.ttr.communication.ServerProxy;
import com.team.jcti.ttr.models.Board;
import com.team.jcti.ttr.models.City;
import com.team.jcti.ttr.models.ClientGameModel;
import com.team.jcti.ttr.models.ClientModel;
import com.team.jcti.ttr.models.Route;
import com.team.jcti.ttr.utils.Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import model.Color;
import model.DestinationCard;
import model.Game;
import model.GameHistory;
import model.Player;
import model.TrainCard;

/**
 * Created by Chance on 3/7/18.
 */

public class GamePresenter implements IGamePresenter, Observer{

    private ClientModel mClientModel = ClientModel.getInstance();
    private ServerProxy mServerProxy = ServerProxy.getInstance();
    private ClientGameModel mActiveGame = ClientGameModel.getInstance();
    private GameActivity mGameActivity;
    private PlayersHandFragment mPlayersHandFragment;
    private BoardFragment mBoardFragment;
    private DecksAndCardsFragment mDecksAndCardsFragment;


    public GamePresenter(GameActivity gameActivity){
        mActiveGame.setActivePresenter(this);
        this.mGameActivity = gameActivity;
    }

    public void makeActivePresenter() {
        mActiveGame.setActivePresenter(this);
    }

    public void claimRoute(String routeId) {
        mServerProxy.claimRoute(mClientModel.getAuthToken(), mClientModel.getGame().getID(), routeId);
    }

    @Override
    public void displayError(String message) {
        mGameActivity.toast(message);
    }

    @Override
    public void update() {
        mDecksAndCardsFragment.updateView();
        mPlayersHandFragment.updateCardList();
        mBoardFragment.update();
        mDecksAndCardsFragment.setFaceCardImages(mActiveGame.getFaceUpCards());
    }

    @Override
    public void drawDestCards() {
        mGameActivity.enterDrawDestinationActivity();
    }

    public void setPlayersHandFragment(PlayersHandFragment frag) {
        mPlayersHandFragment = frag;
    }

    public void setDecksAndCardsFragment(DecksAndCardsFragment frag) {
        mDecksAndCardsFragment = frag;
    }

    public void setBoardFragment(BoardFragment frag) { mBoardFragment = frag; }


    public List<TrainCard> getPlayerTrainCards() {
        return mActiveGame.getPlayersTrainCards();
    }

    public List<DestinationCard> getPlayerDestCards() {
        return mActiveGame.getPlayersDestCards();
    }

    public boolean isFirstTurn(){
        return mActiveGame.isFirstTurn();
    }

    public boolean verifyTurn() {
        return (mActiveGame.isMyTurn());
    }

    //ike

    public TrainCard[] getFaceUpCards(){

        return mActiveGame.getFaceUpCards();
    }

    public int getDestDeckSize() {

        return mActiveGame.getDestDeckSize(); //checkback not sure best way to implement
    }

    public int getTrainDeckSize() {

        return 560; //checkback not sure best way to implement, thought Tanner might've had an idea
    }

    public void onDestDeckClick() {

       mServerProxy.drawDestinationCards(mClientModel.getAuthToken(), mActiveGame.getGameID());
    }

    public void onTrainDeckClick() {
        mServerProxy.drawTrainCards(mClientModel.getAuthToken(), 1, mClientModel.getGame().getID());

    }

    public Collection<City> getCities() {
        return mActiveGame.getBoard().getCities();
    }

    public Collection<Route> getRoutes() {
        return mActiveGame.getBoard().getRoutes();
    }

    public void onFaceUpClick(int i) {
        mServerProxy.drawFaceUp(mClientModel.getAuthToken(), mActiveGame.getGameID(), i);
    }


    @Override
    public void update(Observable observable, Object o) {
        update();
    }

    public Board getBoard() {
        return mActiveGame.getBoard();
    }

    public void initializeBoard(String jsonCities, String jsonRoutes) {
        mActiveGame.initializeBoard(jsonCities, jsonRoutes);
    }
}
