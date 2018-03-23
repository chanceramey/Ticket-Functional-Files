package com.team.jcti.ttr;

import com.team.jcti.ttr.state.OneTrainPickedState;
import com.team.jcti.ttr.state.State;

/**
 * Created by tjense25 on 3/10/18.
 */

public interface IGamePresenter {

    void displayError(String message);
    void setState(State state);
    void drawDestCards();
    void update();
}
