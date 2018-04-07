package com.team.jcti.ttr.drawdestinationcard;

import java.util.List;

import model.DestinationCard;

/**
 * Created by Isaak on 3/4/2018.
 */

public interface IDrawDestinationCardActivity {

    void toast(String message);
    void update();

    void enterFinalScreen();

    void enterGameActivity();
}
