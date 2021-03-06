package com.team.jcti.ttr.models;

import com.google.android.gms.maps.model.LatLng;
import com.team.jcti.ttr.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import model.playerStates.Player;
import model.TrainCard;

/**
 * Created by tjense25 on 2/24/18.
 */

public class Route {

    private int routeColor;
    private TrainCard trainCardColor;
    private String routeId;
    private String srcCity;
    private String destCity;
    private LatLng src;
    private LatLng dest;
    private int length;
    private int claimedBy;
    private Route pairedRoute;
    private int pointValue;

    public Route(TrainCard color, String routeId, String srcCity, String destCity, int length) {
        this.trainCardColor = color;
        this.routeColor = Util.getColorCode(color);
        this.routeId = routeId;
        this.srcCity = srcCity;
        this.destCity = destCity;
        this.length = length;
        this.pairedRoute = null;
        this.claimedBy = -1;
        this.setPointValue();
    }

    public Route(TrainCard double_color, String routeId, String srcCity, String destCity, int length, Route pairedRoute) {
        this.trainCardColor = double_color;
        this.routeColor = Util.getColorCode(double_color);
        this.routeId = routeId;
        this.srcCity = srcCity;
        this.destCity = destCity;
        this.length = length;
        this.pairedRoute = pairedRoute;
        this.claimedBy = -1;
        this.setPointValue();
    }

    public void setPairedRoute(Route r) { this.pairedRoute = r; }

    public Route getPairedRoute() {
        return pairedRoute;
    }

    public int getRouteColor() {
        return routeColor;
    }

    public TrainCard getTrainCardColor() {return trainCardColor; }

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

    public void setSrc(LatLng src) {
        this.src = src;
    }

    public void setDest(LatLng dest) {
        this.dest = dest;
    }


    public static List<Route> loadRoutes(String JSONRoutes) {
        List<Route> routes = new ArrayList<>();
        try {
            JSONArray routesArray = new JSONArray(JSONRoutes);
            for (int i = 0; i < routesArray.length(); i++) {
                JSONObject thisObject = routesArray.getJSONObject(i);
                TrainCard color = TrainCard.valueOf(thisObject.getString("color"));

                Route route = new Route(color, getRouteID(thisObject), thisObject.getString("from"),
                                        thisObject.getString("to"), thisObject.getInt("cost"));
                routes.add(route);
                if (thisObject.has("double_color")) {
                    TrainCard double_color = TrainCard.valueOf(thisObject.getString("double_color"));

                    Route double_route = new Route(double_color, route.getRouteId().concat(" (alt)"),
                                                    route.getSrcCity(), route.getDestCity(),
                                                    route.getLength(), route);
                    routes.add(double_route);
                    route.setPairedRoute(double_route);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return routes;
    }

    private static String getRouteID(JSONObject object) throws JSONException {
        StringBuilder sb = new StringBuilder();
        sb.append("from ");
        sb.append(object.getString("from"));
        sb.append(" to ");
        sb.append(object.getString("to"));
        return sb.toString();
    }

    public LatLng getSrc() {
        return src;
    }

    public LatLng getDest() {
        return dest;
    }

    public void claim(Player player) {
        this.claimedBy = player.getId();
        this.routeColor = Util.getPlayerColorCode(player.getColor());
    }

    public int getClaimedBy() {
        return this.claimedBy;
    }


    @Override
    public boolean equals(Object o) {
        if (o.getClass() != Route.class) {
            return false;
        }

        Route otherRoute = (Route) o;

        if (otherRoute.getSrcCity() == this.getSrcCity() && otherRoute.getDestCity() == this.getDestCity()) {
            return true;
        }

        return false;
    }
  
  public void setPointValue() {
        switch (length){
            case 1:
                pointValue = 1;
                break;
            case 2:
                pointValue = 2;
                break;
            case 3:
                pointValue = 4;
                break;
            case 4:
                pointValue = 7;
                break;
            case 5:
                pointValue = 10;
                break;
            case 6:
                pointValue = 15;
                break;
            default:
                pointValue = 0;
                break;
        }
    }

    public int getPointValue() {
        return pointValue;
    }
}
