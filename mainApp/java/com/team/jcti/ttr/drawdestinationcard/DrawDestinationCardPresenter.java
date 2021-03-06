package com.team.jcti.ttr.drawdestinationcard;

import com.team.jcti.ttr.IGamePresenter;
import com.team.jcti.ttr.communication.ServerProxy;
import com.team.jcti.ttr.models.ClientGameModel;
import com.team.jcti.ttr.models.ClientModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import model.DestinationCard;
import model.playerStates.Player;


/**
 * Created by Isaak on 3/4/2018.
 */

public class DrawDestinationCardPresenter implements Observer, IDrawDestinationCardPresenter, IGamePresenter {

    private IDrawDestinationCardActivity drawDestinationCardActivity;
    private List<DestinationCard> choosableCards;
    private ClientModel mClientModel;
    private ClientGameModel mClientGameModel;
    private Player player;
    private ServerProxy mServerProxy;

    public DrawDestinationCardPresenter(DrawDestinationCardActivity activity) {

        drawDestinationCardActivity = activity;
        mClientModel = ClientModel.getInstance();
        mClientGameModel = ClientGameModel.getInstance();
        mClientGameModel.setActivePresenter(this);
        mServerProxy = ServerProxy.getInstance();
        player = mClientGameModel.getUserPlayer();
        choosableCards = new ArrayList<>();

    }

    @Override
    public List<DestinationCard> getCards(){
        return mClientGameModel.getUsersDestCard();
    }

    @Override
    public void update(Observable observable, Object o) {
    }

    public boolean isGameStart(Player player){
        return player.isFirstDestPick();
    }

    @Override
    public boolean isGameStart(){
        return isGameStart(player);
    }

    @Override
    public void returnRejectedDestCards(List<Integer> rejectedCardsPos) {

        int[] rejectedCardPositions = new int[rejectedCardsPos.size()];
        int count = 0;
        for(Integer pos : rejectedCardsPos){
            rejectedCardPositions[count] = pos+player.getNumDestCards()-3;
            count++;
        }

        if(player.isFirstDestPick()) {
            player.setFirstDestPick();
        }
        mServerProxy.returnDestinationCards(mClientModel.getAuthToken(), mClientGameModel.getID(), rejectedCardPositions);
    }

    @Override
    public void displayError(String message) {
        drawDestinationCardActivity.toast(message);

    }

    @Override
    public void update() {
        if (mClientGameModel.exitDrawDest()) {
            drawDestinationCardActivity.enterGameActivity();
        }
    }

    @Override
    public void onGameEnded() {
        drawDestinationCardActivity.enterFinalScreen();
    }

    @Override
    public void drawDestCards() {

    }

    public void updateCards(Integer playerID, Integer numCards, DestinationCard[] cards){

        player = mClientGameModel.getPlayerById(playerID);
        for(int i = 0; i < numCards; i++){
            choosableCards.add(cards[i]);
        }
        drawDestinationCardActivity.update();
    }

}
