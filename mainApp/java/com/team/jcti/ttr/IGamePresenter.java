package com.team.jcti.ttr;


import model.DestinationCard;

/**
 * Created by tjense25 on 3/10/18.
 */

public interface IGamePresenter {

    void displayError(String message);
    void drawDestCards();
    void update();

    void onGameEnded();
}
