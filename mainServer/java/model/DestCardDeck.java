package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by tjense25 on 2/26/18.
 */

public class DestCardDeck {
    private List<DestinationCard> deck;

    public DestCardDeck() {
        deck = new ArrayList<>();
        try {
            initializeDeck();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeDeck() throws IOException {
        BufferedReader reader = new BufferedReader(
                                     new FileReader(
                                             new File("server/src/main/destCards.txt")));
        String line = null;
        while ((line = reader.readLine()) != null) {
            String[] col = line.trim().split(",");
            deck.add(new DestinationCard(col[0], col[1], Integer.parseInt(col[2])));
        }
        shuffle(deck, deck);
    }

    private void shuffle(List<DestinationCard> startDeck, List<DestinationCard> resultDeck) {
        Random rand = new Random();
        int length = startDeck.size();
        for (int i = length - 1; i >= 0; i--) {
            int randomPos = rand.nextInt(i + 1);
            resultDeck.add(startDeck.remove(randomPos));
        }
    }

    public DestinationCard[] drawCards(int numToDraw) {
        //If player tries to draw more cards than are currently in deck, shuffle discard and add to deck
        if (numToDraw > deck.size()) {
           numToDraw = deck.size();
        }
        DestinationCard[] cards = new DestinationCard[numToDraw];
        for (int i = 0; i < numToDraw; i++) {
            cards[i] = deck.remove(0);
        }
        return cards;
    }

    public void discard(DestinationCard[] discarded) {
        //Put each card in the discarded array into the end of the deck
        for (int i = 0; i < discarded.length; i ++) {
            deck.add(discarded[i]);
        }
    }
}
