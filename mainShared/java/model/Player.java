package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tjense25 on 2/24/18.
 */

public class Player {

    private String id;
    private String user;
    private Color color;

    private List<TrainCard> trainCards;
    private List<DestinationCard> destCards;

    private List<Route> routesClaimed;

    public Player(String user, Color color, String id) {
        this.user = user;
        this.color = color;
        this.id = id;

        this.trainCards = new ArrayList<>();
        this.destCards = new ArrayList<>();
        this.routesClaimed = new ArrayList<>();
    }

    public String getId() {
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
}
