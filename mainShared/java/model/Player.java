package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tjense25 on 2/24/18.
 */

public class Player {

    private int id;
    private String user;
    private Color color;

    private int numTrainCards;
    private List<TrainCard> trainCards;
    private Map<TrainCard, Integer> trainCardCounts;
    private int numDestCards;
    private List<DestinationCard> destCards;
    private int numTrains;
    private boolean turn = false; // if it is this player's turn or not

    private boolean firstDestPick;

    private int points;

    private List<String> claimedRouteIds;

    public int getNumTrains() {
        return numTrains;
    }


    public Player(String user, Color color, int id) {
        this.user = user;
        this.color = color;
        this.id = id;

        this.trainCards = new ArrayList<>();
        this.trainCardCounts = new HashMap<>();
        for (TrainCard card : TrainCard.values()) {
            trainCardCounts.put(card, 0);
        }
        this.destCards = new ArrayList<>();
        this.claimedRouteIds = new ArrayList<>();
        this.numTrains = 45;
        firstDestPick = true;
    }

    public int getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public Color getColor() {
        return color;
    }

    public List<TrainCard> getTrainCards() {
        return trainCards;
    }

    public List<DestinationCard> getDestCards() {
        return destCards;
    }

    public List<String> getRoutesClaimed() {
        return claimedRouteIds;
    }

    public boolean isTurn() {
        return turn;
    }

    public void addTrainCards(TrainCard[] cards) {
        for (TrainCard card : cards) {
            trainCards.add(card);
            int updatedCount = trainCardCounts.get(card) + 1;
            trainCardCounts.remove(card);
            trainCardCounts.put(card, updatedCount);
        }
        this.numTrainCards = trainCards.size();
    }
    public void addTrainCard(TrainCard card) {
        trainCards.add(card);
        this.numTrainCards = trainCards.size();
    }

    public void addTrainCards(int num) {
        numTrainCards += num;
    }

    public TrainCard[] removeTrainCards(int[] pos) {
        TrainCard[] discarded = new TrainCard[pos.length];
        Arrays.sort(pos);
        for (int i = pos.length - 1; i >= 0; i--) {
            discarded[i] = trainCards.remove(pos[i]);
        }
        this.numTrainCards = trainCards.size();
        for (TrainCard card : discarded) {
            int updatedCount = trainCardCounts.get(card) - 1;
            trainCardCounts.remove(card);
            trainCardCounts.put(card, updatedCount);
        }
        return discarded;
    }

    public void setTurn(boolean b) {
        turn = b;
    }

    public void removeTrainCards(int num) {
        numTrainCards -= num;
    }


    public void addDestCards(DestinationCard[] cards) {
        Collections.addAll(destCards, cards);
        this.numDestCards = destCards.size();
    }

    public void addDestCards(int num) { this.numDestCards += num; }

    public DestinationCard[] removeDestCards(int[] pos) {
        DestinationCard[] discarded = new DestinationCard[pos.length];
        for (int i = 0; i < pos.length; i++) {
            discarded[i] = destCards.remove(pos[i]);
        }
        this.numDestCards = destCards.size();
        return discarded;
    }

    public void removeTrains(int i) { numTrains -= i;}

    public void removeDestCards(int num) { this.numDestCards -= num; }

    public int getPoints() {
        return points;
    }

    public int getNumDestCards() {
        return numDestCards;
    }

    public int getNumTrainCards() {
        return numTrainCards;
    }

    public void addRoute(String routeID) {
        this.claimedRouteIds.add(routeID);
    }

    public boolean isFirstDestPick() {
        return firstDestPick;
    }

    public void setFirstDestPick() {
        this.firstDestPick = false;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getCountOfCardType(TrainCard card) {
        return trainCardCounts.get(card);
    }

    public int[] getRouteClaimingCards(int length, TrainCard color) {
        int[] cardPos = new int[length];
        int total = 0;
        for (int i = 0; i < trainCards.size(); i++) {
            if (trainCards.get(i) == color) {
                cardPos[total] = i;
                total++;
            }
            if (total == length) return cardPos;
        }

        for (int i = 0; i < trainCards.size(); i++) {
            if (trainCards.get(i) == TrainCard.WILD) {
                cardPos[total] = i;
                total++;
            }
            if (total == length) return cardPos;
        }

        return null;
    }
}
