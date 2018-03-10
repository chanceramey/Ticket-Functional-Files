package com.team.jcti.ttr.drawdestinationcard;

import java.util.List;

import model.DestinationCard;

/**
 * Created by Isaak on 3/5/2018.
 */

public interface IDrawDestinationCardPresenter {

    List<DestinationCard> getCards();

    boolean isGameStart();

    void returnRejectedDestCards(List<Integer> rejectedCards);

    void updateCards(Integer player, Integer numCards, DestinationCard[] cards);
}
