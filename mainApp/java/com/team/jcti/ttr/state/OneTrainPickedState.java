package com.team.jcti.ttr.state;

import com.team.jcti.ttr.communication.ServerProxy;
import com.team.jcti.ttr.game.GamePresenter;
import com.team.jcti.ttr.models.ClientGameModel;
import com.team.jcti.ttr.models.ClientModel;

import model.TrainCard;

/**
 * Created by Isaak on 3/21/2018.
 */

public class OneTrainPickedState implements State{
    private ClientModel mClientModel = ClientModel.getInstance();
    private ServerProxy mServerProxy = ServerProxy.getInstance();
    private ClientGameModel mActiveGame = ClientGameModel.getInstance();
    private GamePresenter gamePresenter;

    public OneTrainPickedState(GamePresenter gamePresenter){
        this.gamePresenter = gamePresenter;
    }


    @Override
    public void drawFromTrainDeck() {
        mServerProxy.drawTrainCards(mClientModel.getAuthToken(), 1, mClientModel.getGame().getID());
        gamePresenter.setState(new OneTrainPickedState(gamePresenter));
    }

    @Override
    public void drawFaceUpTrainCard(int pos) {

        TrainCard[] faceUpCards = gamePresenter.getFaceUpCards();
        if(faceUpCards[pos] == TrainCard.WILD){
            gamePresenter.displayError("You can't draw another locomotive");
        }
        else{
            gamePresenter.setState(new NotTurnState(gamePresenter));
            mServerProxy.drawFaceUp(mClientModel.getAuthToken(), mActiveGame.getGameID(), pos);
        }
    }

    @Override
    public void drawDestinationCards() {
        gamePresenter.displayError("You already picked one train card");
    }

    @Override
    public void claimRoute(String routeId) {
        gamePresenter.displayError("You already picked one train card");
    }
}
