package com.team.jcti.ttr.state;

import com.team.jcti.ttr.communication.ServerProxy;
import com.team.jcti.ttr.game.GamePresenter;
import com.team.jcti.ttr.models.ClientGameModel;
import com.team.jcti.ttr.models.ClientModel;

import model.TrainCard;

/**
 * Created by Isaak on 3/21/2018.
 */

public class TurnState implements State{

    private ClientModel mClientModel = ClientModel.getInstance();
    private ServerProxy mServerProxy = ServerProxy.getInstance();
    private ClientGameModel mActiveGame = ClientGameModel.getInstance();
    private GamePresenter gamePresenter;

    public TurnState(GamePresenter gamePresenter){
        this.gamePresenter = gamePresenter;
    }

    @Override
    public void drawFromTrainDeck() {

        mServerProxy.drawTrainCards(mClientModel.getAuthToken(), 1, mClientModel.getGame().getID());
        gamePresenter.setState(new OneTrainPickedState(gamePresenter));
    }

    @Override
    public void drawFaceUpTrainCard(int pos) {

        mServerProxy.drawFaceUp(mClientModel.getAuthToken(), mActiveGame.getGameID(), pos);

        TrainCard[] faceUpCards = gamePresenter.getFaceUpCards();
        if(faceUpCards[pos] == TrainCard.WILD){
            gamePresenter.setState(new NotTurnState(gamePresenter));
        }
        else{
            gamePresenter.setState(new OneTrainPickedState(gamePresenter));
        }
    }

    @Override
    public void drawDestinationCards() {
        mServerProxy.drawDestinationCards(mClientModel.getAuthToken(), mActiveGame.getGameID());
        gamePresenter.setState(new NotTurnState(gamePresenter));
    }

    @Override
    public void claimRoute(String routeId) {
        mServerProxy.claimRoute(mClientModel.getAuthToken(), mClientModel.getGame().getID(), routeId);
        gamePresenter.setState(new NotTurnState(gamePresenter));
    }
}
