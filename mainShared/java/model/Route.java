package model;

/**
 * Created by tjense25 on 2/24/18.
 */

public class Route {

    private TrainCard routeColor;
    private String routeId;
    private String srcCity;
    private String destCity;
    private int length;
    private TrainCard colorOfDuplicate;
    private boolean claimed;
    private Color claimedColor;

    public Route(TrainCard routeColor, String routeId, String srcCity, String destCity, int length) {
        this.routeColor = routeColor;
        this.routeId = routeId;
        this.srcCity = srcCity;
        this.destCity = destCity;
        this.length = length;
    }

    public Route(TrainCard routeColor, String routeId, String srcCity, String destCity, int length, TrainCard colorOfDuplicate) {
        this.routeColor = routeColor;
        this.routeId = routeId;
        this.srcCity = srcCity;
        this.destCity = destCity;
        this.length = length;
        this.colorOfDuplicate = colorOfDuplicate;
    }

    public TrainCard getColorOfDuplicate() {
        return colorOfDuplicate;
    }

    public TrainCard getRouteColor() {
        return routeColor;
    }

    public String getRouteId() {
        return routeId;
    }

    public String getSrcCity() {
        return srcCity;
    }

    public String getDestCity() {
        return destCity;
    }

    public int getLength() {
        return length;
    }

    public void setRouteColor(TrainCard routeColor) {
        this.routeColor = routeColor;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public void setSrcCity(String srcCity) {
        this.srcCity = srcCity;
    }

    public void setDestCity(String destCity) {
        this.destCity = destCity;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setColorOfDuplicate(TrainCard colorOfDuplicate) {
        this.colorOfDuplicate = colorOfDuplicate;
    }

    public boolean isClaimed() {
        return claimed;
    }

    public Color getClaimedColor() {
        return claimedColor;
    }

    public void setClaimed(boolean claimed) {
        this.claimed = claimed;
    }

    public void setClaimedColor(Color color) {
        this.claimedColor = color;
    }
}
