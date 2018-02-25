package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by tjense25 on 2/24/18.
 */

public class TrainCardDeck {

    private List<TrainCard> deck;
    private List<TrainCard> discard;

    public TrainCardDeck() {
        deck = new ArrayList<>();
        discard = new ArrayList<>();
        initializeDeck();
    }

    private void initializeDeck() {
        for (TrainCard color : TrainCard.values()) {
            for (int i = 0; i < (color == TrainCard.LOCOMOTIVE ? 14 : 12); i++) {
                deck.add(color);
            }
        }
        shuffle(deck, deck);
    }

    private void shuffle(List<TrainCard> startDeck, List<TrainCard> resultDeck) {
        Random rand = new Random();
        int length = startDeck.size();
        for (int i = length - 1; i >= 0; i++) {
            int randomPos = rand.nextInt(i + 1);
            resultDeck.add(startDeck.remove(randomPos));
        }
    }


}
