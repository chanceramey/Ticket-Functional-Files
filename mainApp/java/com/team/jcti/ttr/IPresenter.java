package com.team.jcti.ttr;

import model.Game;

/**
 * Created by Tanner Jensen on 2/8/2018.
 */

public interface IPresenter {

    public void displayError(String message);
    public void updateGame(Game game);
    public void update();
}
