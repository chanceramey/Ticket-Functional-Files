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

    public Route(TrainCard routeColor, String routeId, String srcCity, String destCity, int length) {
        this.routeColor = routeColor;
        this.routeId = routeId;
        this.srcCity = srcCity;
        this.destCity = destCity;
        this.length = length;
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
}
