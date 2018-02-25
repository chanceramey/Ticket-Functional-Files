package model;

/**
 * Created by tjense25 on 2/24/18.
 */

public class DestinationCard {
    private int pointValue;
    private String srcCity;
    private String destCity;
    private String id;

    public DestinationCard(String id, String destCity, String srcCity, int pointValue) {
        this.id = id;
        this.destCity = destCity;
        this.srcCity = srcCity;
        this.pointValue = pointValue;
    }

    public int getPointValue() {
        return pointValue;
    }

    public String getSrcCity() {
        return srcCity;
    }

    public String getDestCity() {
        return destCity;
    }

    public String getId() {
        return id;
    }
}
