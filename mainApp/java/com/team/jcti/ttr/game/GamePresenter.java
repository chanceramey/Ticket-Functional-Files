package com.team.jcti.ttr.game;

import com.team.jcti.ttr.IGamePresenter;
import com.team.jcti.ttr.communication.ServerProxy;
import com.team.jcti.ttr.models.Board;
import com.team.jcti.ttr.models.City;
import com.team.jcti.ttr.models.ClientGameModel;
import com.team.jcti.ttr.models.ClientModel;
import com.team.jcti.ttr.models.Route;
import com.team.jcti.ttr.state.State;
import com.team.jcti.ttr.state.TurnState;

import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import model.DestinationCard;
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
    private Route selectedRoute;
    private State state;


    public GamePresenter(GameActivity gameActivity){
        mActiveGame.setActivePresenter(this);
        this.mGameActivity = gameActivity;
        this.state = mActiveGame.getState();
    }

    public void makeActivePresenter() {
        mActiveGame.setActivePresenter(this);
    }

    public void claimRoute(String routeId) {
        state.claimRoute(routeId);
    }

    public boolean claimRouteWithColor(TrainCard color) {
        if (selectedRoute == null) return false;
        int[] cardPos = mActiveGame.getClaimingCards(selectedRoute.getLength(), color);
        if (cardPos == null) {
            this.selectedRoute = null;
            mGameActivity.toast("You do not have enough cards to claim this route");
            mPlayersHandFragment.updateCardList();
            this.state = new TurnState(this);
            return false;
        }

        mServerProxy.claimRoute(mClientModel.getAuthToken(), mActiveGame.getGameID(), selectedRoute.getRouteId(), selectedRoute.getLength(), cardPos);
        selectedRoute = null;
        return true;
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

    public void onGameEnded() {
       mGameActivity.enterFinalScreen();
    }

    public void displayCities(DestinationCard destCard) {
        City[] cities = mActiveGame.getCitiesFromDest(destCard);
        mBoardFragment.drawMarkersForCities(cities);
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


    public int getPlayerTrainCardsSize() {
        return mActiveGame.getPlayersTrainCards().size();
    }

    public List<DestinationCard> getPlayerDestCards() {
        return mActiveGame.getPlayersDestCards();
    }

    public boolean isFirstTurn(){
        return mActiveGame.isFirstTurn();
    }

    public TrainCard[] getFaceUpCards(){

        return mActiveGame.getFaceUpCards();
    }

    public int getDestDeckSize() {

        return mActiveGame.getDestDeckSize();
    }

    public int getTrainDeckSize() {

        return mActiveGame.getTrainDeckSize();
    }

    public void onDestDeckClick() {

        state.drawDestinationCards();
    }

    public void onTrainDeckClick() {

        state.drawFromTrainDeck();
    }

    public void onFaceUpClick(int pos) {

        state.drawFaceUpTrainCard(pos);
    }

    public Collection<City> getCities() {
        return mActiveGame.getBoard().getCities();
    }

    public Collection<Route> getRoutes() {
        return mActiveGame.getBoard().getRoutes();
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

    public int getNumCards(TrainCard card) {
        return mActiveGame.getPlayersNumTrainCards(card);
    }

    public void setState(State state){
        this.state = state;
    }

    public void setSelectedRoute(Route selectedRoute) {
        this.selectedRoute = selectedRoute;
    }

    public void startSelectionState() {
        mPlayersHandFragment.startSelectionState();
    }

    public void clearDestMarkers() {
        mBoardFragment.clearCityMarkers();
    }
}
