package model;

/**
 * Created by tjense25 on 2/24/18.
 */

public class DestinationCard {
    private int pointValue;
    private String srcCity;
    private String destCity;
    private boolean finished = false;

    public DestinationCard(String destCity, String srcCity, int pointValue) {
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

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean val) {
        finished = val;
    }

    @Override
    public String toString() {
        return String.format("DestCard: {src: %s, dest: %s, points: %d}", srcCity, destCity, pointValue);
    }
}
