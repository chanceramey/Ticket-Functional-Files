package com.team.jcti.ttr.drawdestinationcard;

import com.team.jcti.ttr.IPresenter;
import com.team.jcti.ttr.communication.ServerProxy;
import com.team.jcti.ttr.models.ClientGameModel;
import com.team.jcti.ttr.models.ClientModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import model.DestinationCard;
import model.Game;
import model.Player;
import model.User;


/**
 * Created by Isaak on 3/4/2018.
 */

public class DrawDestinationCardPresenter implements Observer, IDrawDestinationCardPresenter, IPresenter{

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
        mServerProxy = ServerProxy.getInstance();
        choosableCards = new ArrayList<>();

    }

    @Override
    public List<DestinationCard> getCards(){
        return choosableCards;
    }

    @Override
    public void update(Observable observable, Object o) {
    }

    public boolean isGameStart(Player player){
        if(player.getNumDestCards() == 0){
            return true;
        }
        return false;
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
            rejectedCardPositions[count] = pos+player.getNumDestCards()+1; //checkback
            count++;
        }

        mServerProxy.returnDestinationCards(mClientModel.getAuthToken(), mClientModel.getGame().getID(), rejectedCardPositions);
    }

    @Override
    public void displayError(String message) {
    }

    @Override
    public void updateGame(Game game) {}

    @Override
    public void update() {

    }

    public void updateCards(Integer playerID, Integer numCards, DestinationCard[] cards){

        player = mClientGameModel.getPlayerById(playerID);
        for(int i = 0; i < numCards; i++){
            choosableCards.add(cards[i]);
        }
    }

}
