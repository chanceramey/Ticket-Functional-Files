package com.team.jcti.ttr;

/**
 * Created by tjense25 on 3/10/18.
 */

public interface IGamePresenter extends IPresenter {

    void displayError(String message);
    void drawDestCards();
}