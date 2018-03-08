package com.team.jcti.ttr.drawdestinationcard;

import java.util.List;

import model.DestinationCard;

/**
 * Created by Isaak on 3/5/2018.
 */

public interface IDrawDestinationCardPresenter {

    void onSubmit(List<DestinationCard> cards);

    int getDestinationDeckSize();

    List<DestinationCard> getCards();

    boolean isGameStart();

    void returnRejectedDestCards(List<Integer> rejectedCards);
}
