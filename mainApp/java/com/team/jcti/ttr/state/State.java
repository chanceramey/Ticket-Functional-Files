package com.team.jcti.ttr.state;

/**
 * Created by Isaak on 3/21/2018.
 */

public interface State {

    void drawFromTrainDeck();
    void drawFaceUpTrainCard(int pos);
    void drawDestinationCards();
    void claimRoute(String routeId);
}
