package com.team.jcti.ttr.game;

import java.util.ArrayList;

/**
 * Created by tjense25 on 2/24/18.
 */

public interface IGameActivity {
    void displayErrorMessages(ArrayList<String> errorMessages);

    void enterDrawDestinationActivity();
}
