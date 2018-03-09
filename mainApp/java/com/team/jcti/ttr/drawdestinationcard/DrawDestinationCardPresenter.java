package com.team.jcti.ttr.drawdestinationcard;

import com.team.jcti.ttr.IPresenter;
import com.team.jcti.ttr.communication.ServerProxy;
import com.team.jcti.ttr.models.ClientModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import model.DestCardDeck;
import model.DestinationCard;
import model.Game;
import model.Player;
import model.User;


/**
 * Created by Isaak on 3/4/2018.
 */

public class DrawDestinationCardPresenter implements Observer, IDrawDestinationCardPresenter, IPresenter{

    private IDrawDestinationCardActivity drawDestinationCardActivity;
    private List<DestinationCard> destinationCardDeck;
    private ClientModel mClientModel = ClientModel.getInstance();
    private Player player;
    private ServerProxy mServerProxy = ServerProxy.getInstance();

    public DrawDestinationCardPresenter(DrawDestinationCardActivity activity) {

        drawDestinationCardActivity = activity;
        mClientModel.addObserver(this);

        player = mClientModel.getActivePlayer(); //comeback
        if(isGameStart(player)){
            //destinationCardDeck = mClientModel. comeback
        }
        else{
            //comeback
        }


    }

    @Override
    public void onSubmit(List<DestinationCard> cards) {
        //comeback
    }

    @Override
    public int getDestinationDeckSize(){

        return destinationCardDeck.size();
    }

    @Override
    public List<DestinationCard> getCards(){
        return destinationCardDeck;
    }

    @Override
    public void update(Observable observable, Object o) {
        //comeback
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

        List<DestinationCard> rejectedCards = new ArrayList<>();
        for(Integer pos : rejectedCardsPos){
            if(pos < destinationCardDeck.size()){
                rejectedCards.add(destinationCardDeck.get(pos));
            }
        }

        mServerProxy.returnDestinationCards(mClientModel.getAuthToken(), mClientModel.getGame().getID(), rejectedCards);
    }

    @Override
    public void displayError(String message) {
        //comeback
    }

    @Override
    public void updateGame(Game game) {

    }

    @Override
    public void update() {

    }
}
