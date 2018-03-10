package com.team.jcti.ttr.game;

import com.team.jcti.ttr.IPresenter;
import com.team.jcti.ttr.communication.ServerProxy;
import com.team.jcti.ttr.models.ClientGameModel;
import com.team.jcti.ttr.models.ClientModel;
import com.team.jcti.ttr.utils.Util;

import java.util.List;

import model.Color;
import model.DestinationCard;
import model.Game;
import model.TrainCard;

/**
 * Created by Chance on 3/7/18.
 */

public class GamePresenter implements IPresenter{

    private ClientModel mClientModel = ClientModel.getInstance();
    private ServerProxy mServerProxy = ServerProxy.getInstance();
    private ClientGameModel mActiveGame = ClientGameModel.getInstance();
    private GameActivity mGameActivity;
    private PlayersHandFragment mPlayersHandFragment;
    private BoardFragment mBoardFragment;

    public GamePresenter(GameActivity gameActivity){
        mActiveGame.setActivePresenter(this);
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

    public void setPlayersHandFragment(PlayersHandFragment frag) {
        mPlayersHandFragment = frag;
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

    public void onClaimRoute(Integer player, String routeID) {
        Color color = mActiveGame.getPlayers().get(player).getColor();
        mBoardFragment.claimRoute(routeID, color);
    }

    public boolean verifyTurn() {
        return (mActiveGame.isMyTurn());
    }

    //ike

    public TrainCard[] getFaceUpCards(){

        return mActiveGame.getFaceUpCards();
    }

    public void updateFaceUpCards(int[] pos, TrainCard[] faceUpCards){

        mActiveGame.updateFaceUpCards(pos, faceUpCards);
    }

    public int getDestDeckSize() {

        return 30; //checkback not sure best way to implement
    }

    public int getTrainDeckSize() {

        return 560; //checkback not sure best way to implement, thought Tanner might've had an idea
    }

    public void onDestDeckClick() {

        mGameActivity.enterDrawDestinationActivity();
    }

    public void onTrainDeckClick() {

    }

    public void onFaceUpClick(int i) {
    }
}
