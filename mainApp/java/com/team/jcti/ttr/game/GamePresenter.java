package com.team.jcti.ttr.game;

import com.team.jcti.ttr.IGamePresenter;
import com.team.jcti.ttr.communication.ServerProxy;
import com.team.jcti.ttr.models.Board;
import com.team.jcti.ttr.models.City;
import com.team.jcti.ttr.models.ClientGameModel;
import com.team.jcti.ttr.models.ClientModel;
import com.team.jcti.ttr.models.Route;
import com.team.jcti.ttr.utils.Util;

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



    public GamePresenter(GameActivity gameActivity){
        mActiveGame.setActivePresenter(this);
        this.mGameActivity = gameActivity;
    }

    public void makeActivePresenter() {
        mActiveGame.setActivePresenter(this);
        mDecksAndCardsFragment.updateView();
    }

    public void claimRoute(String routeId) {
        if(!mActiveGame.isMyTurn()) {
            mGameActivity.toast("It is not your turn!");
            return;
        }

        Route route = mActiveGame.getRouteFromID(routeId);
        if(route == null) {
            mGameActivity.toast("Route already claimed by a player.");
            return;
        }

        if(route.getPairedRoute() != null) {
            Route pairedRoute = route.getPairedRoute();
            if(pairedRoute.getClaimedBy() == mActiveGame.getUserPlayer().getId()) {
                mGameActivity.toast("You already Claim the paired route!");
                return;
            }
            if(mActiveGame.getRouteFromID(pairedRoute.getRouteId()) == null && mActiveGame.getPlayers().size() <= 3) {
                mGameActivity.toast("Cannot claim double route with less than 3 players");
                return;
            }
        }

        if(!mActiveGame.playerHasEnoughTrains(route.getLength())) {
            mGameActivity.toast("You do not have enough trains to claim this route");
            return;
        }

        this.selectedRoute = route;
        if (route.getTrainCardColor() == TrainCard.WILD) {
            mGameActivity.toast("Select train card color to claim this route");
            mPlayersHandFragment.startSelectionState();
            return;
        }

        claimRouteWithColor(route.getTrainCardColor());
    }

    public boolean claimRouteWithColor(TrainCard color) {
        if (selectedRoute == null) return false;
        int[] cardPos = mActiveGame.getClaimingCards(selectedRoute.getLength(), color);
        if (cardPos == null) {
            this.selectedRoute = null;
            mGameActivity.toast("You do not have enough cards to claim this route");
            mPlayersHandFragment.updateCardList();
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
    }

    public void onGameEnded() {
       mGameActivity.enterFinalScreen();
       mGameActivity.toast(Integer.toString(mActiveGame.getLengthOfLongestPath()));
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
        if(!mActiveGame.isMyTurn()) {
            mGameActivity.toast("It's not your turn!");
            return;
        }
        if(getDestDeckSize() == 0) {
            mGameActivity.toast("No More Destination Cards in Deck");
            return;
        }

        mServerProxy.drawDestinationCards(mClientModel.getAuthToken(), mActiveGame.getGameID());

    }

    public void onTrainDeckClick() {
        if(!mActiveGame.isMyTurn()) {
            mGameActivity.toast("It's not your turn!");
            return;
        }
        if(getTrainDeckSize() == 0) {
            mGameActivity.toast("No More Train Cards in Deck");
            return;
        }

        mServerProxy.drawTrainCards(mClientModel.getAuthToken(), 1, mActiveGame.getGameID());
    }

    public void onFaceUpClick(int pos) {
        if(!mActiveGame.isMyTurn()) {
            mGameActivity.toast("It's not your turn!");
            return;
        }


        mServerProxy.drawFaceUp(mClientModel.getAuthToken(), mActiveGame.getGameID(), pos);

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


    public void clearDestMarkers() {
        mBoardFragment.clearCityMarkers();
    }

}
