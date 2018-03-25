package com.team.jcti.ttr.models;

import com.google.android.gms.maps.model.LatLng;
import com.team.jcti.ttr.R;
import com.team.jcti.ttr.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Color;
import model.TrainCard;

/**
 * Created by tjense25 on 2/24/18.
 */

public class Route {

    private int routeColor;
    private String routeId;
    private String srcCity;
    private String destCity;
    private LatLng src;
    private LatLng dest;
    private int length;
    private Route pairedRoute;

    public Route(int routeColor, String routeId, String srcCity, String destCity, int length) {
        this.routeColor = routeColor;
        this.routeId = routeId;
        this.srcCity = srcCity;
        this.destCity = destCity;
        this.length = length;
        this.pairedRoute = null;
    }

    public Route(int double_color, String routeId, String srcCity, String destCity, int length, Route pairedRoute) {
        this.routeColor = double_color;
        this.routeId = routeId;
        this.srcCity = srcCity;
        this.destCity = destCity;
        this.length = length;
        this.pairedRoute = pairedRoute;
    }

    public void setPairedRoute(Route r) { this.pairedRoute = r; }

    public Route getPairedRoute() {
        return pairedRoute;
    }

    public int getRouteColor() {
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
                int color = Util.getColorCode(TrainCard.valueOf(thisObject.getString("color")));

                Route route = new Route(color, getRouteID(thisObject), thisObject.getString("from"),
                                        thisObject.getString("to"), thisObject.getInt("cost"));
                routes.add(route);
                if (thisObject.has("double_color")) {
                    int double_color = Util.getColorCode(TrainCard.valueOf(thisObject.getString("double_color")));

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

    public void setColor(int color) {
        this.routeColor = color;
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
}
