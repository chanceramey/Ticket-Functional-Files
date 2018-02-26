package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by tjense25 on 2/24/18.
 */

public class Player {

    private int id;
    private String user;
    private Color color;

    private int numTrainCards;
    private List<TrainCard> trainCards;
    private int numDestCards;
    private List<DestinationCard> destCards;

    private int points;

    private List<Route> routesClaimed;

    public Player(String user, Color color, int id) {
        this.user = user;
        this.color = color;
        this.id = id;

        this.trainCards = new ArrayList<>();
        this.destCards = new ArrayList<>();
        this.routesClaimed = new ArrayList<>();
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

    public List<Route> getRoutesClaimed() {
        return routesClaimed;
    }

    public void addTrainCards(TrainCard[] cards) {
        Collections.addAll(trainCards, cards);
        this.numTrainCards = trainCards.size();
    }

    public void addTrainCards(int num) {
        numTrainCards += num;
    }

    public TrainCard[] removeTrainCards(int[] pos) {
        TrainCard[] discarded = new TrainCard[pos.length];
        for (int i = 0; i < pos.length; i++) {
            discarded[i] = trainCards.remove(pos[i]);
        }
        this.numTrainCards = trainCards.size();
        return discarded;
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
}