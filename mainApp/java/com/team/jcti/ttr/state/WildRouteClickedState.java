package com.team.jcti.ttr.state;

import com.team.jcti.ttr.communication.ServerProxy;
import com.team.jcti.ttr.game.GamePresenter;
import com.team.jcti.ttr.models.ClientGameModel;
import com.team.jcti.ttr.models.ClientModel;

/**
 * Created by ImpossiblyIdiotic on 3/23/2018.
 */

public class WildRouteClickedState implements State{
    private ClientModel mClientModel = ClientModel.getInstance();
    private ServerProxy mServerProxy = ServerProxy.getInstance();
    private ClientGameModel mActiveGame = ClientGameModel.getInstance();
    private GamePresenter gamePresenter;

    public WildRouteClickedState(GamePresenter gamePresenter){
        this.gamePresenter = gamePresenter;
    }

    @Override
    public void drawFromTrainDeck() {
        gamePresenter.displayError("You must select which train color to use");
    }

    @Override
    public void drawFaceUpTrainCard(int pos) {
        gamePresenter.displayError("You must select which train color to use");
    }

    @Override
    public void drawDestinationCards() {
        gamePresenter.displayError("You must select which train color to use");
    }

    @Override
    public void claimRoute(String routeId) {
        gamePresenter.displayError("You must select which train color to use");
    }
}
