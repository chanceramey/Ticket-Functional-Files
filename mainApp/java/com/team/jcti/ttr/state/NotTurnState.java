package com.team.jcti.ttr.state;

import com.team.jcti.ttr.game.GamePresenter;

/**
 * Created by Isaak on 3/21/2018.
 */

public class NotTurnState implements State{
    private GamePresenter gamePresenter;

    public NotTurnState(GamePresenter gamePresenter){
        this.gamePresenter = gamePresenter;
    }


    @Override
    public void drawFromTrainDeck() {
        gamePresenter.displayError("It's not your turn");
    }

    @Override
    public void drawFaceUpTrainCard(int pos) {
        gamePresenter.displayError("It's not your turn");
    }

    @Override
    public void drawDestinationCards() {
        gamePresenter.displayError("It's not your turn");
    }

    @Override
    public void claimRoute(String routeId) {
        gamePresenter.displayError("It's not your turn");
    }
}
