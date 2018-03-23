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
            for (int i = 0; i < (color == TrainCard.WILD ? 14 : 12); i++) {
                deck.add(color);
            }
        }
        shuffle(deck, deck);
    }

    private void shuffle(List<TrainCard> startDeck, List<TrainCard> resultDeck) {
        Random rand = new Random();
        int length = startDeck.size();
        for (int i = length - 1; i >= 0; i--) {
            int randomPos = rand.nextInt(i + 1);
            resultDeck.add(startDeck.remove(randomPos));
        }
    }

    public TrainCard[] drawCards(int numToDraw) {
        //If player tries to draw more cards than are currently in deck, shuffle discard and add to deck
        if (numToDraw > deck.size()) {
            shuffle(discard, deck);
            //if after reshuffle, there still aren't enough cards, only draw as many as possible
            if (numToDraw > deck.size()) numToDraw = deck.size();
        }
        TrainCard[] cards = new TrainCard[numToDraw];
        for (int i = 0; i < numToDraw; i++) {
            cards[i] = deck.remove(0);
        }
        return cards;
    }

    public TrainCard drawCard() {
        //If there are no cards in deck, reshuffle discard into deck
        if (deck.size() == 0) {
            shuffle(discard, deck);
            //If after shuffle there still are no cards return no cards (no cards left)
            if (deck.size() == 0) return null;
        }
        //remove and return card at first position in deck
        return deck.remove(0);
    }

    public void discard(TrainCard[] discarded) {
        //Put each card in the discarded array into the discard deck
        for (int i = 0; i < discarded.length; i ++) {
            discard.add(discarded[i]);
        }
    }


    public int size() {
        return deck.size() + discard.size();
    }
}
