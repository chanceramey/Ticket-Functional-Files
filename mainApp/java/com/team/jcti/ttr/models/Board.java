package com.team.jcti.ttr.models;

import com.google.android.gms.maps.model.LatLng;
import com.team.jcti.ttr.utils.Util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Player;

/**
 * Created by tjense25 on 3/15/18.
 */

public class Board {
    private Map<String, City> cities;
    private Map<String, Route> routes;
    private Map<String, Integer> claimedRoutes;

    public Board(String JSONcities, String JSONRoutes) {
        this.cities = new HashMap<>();
        for(City city : City.loadCities(JSONcities) ) {
            cities.put(city.getName(), city);
        }
        this.routes = new HashMap<>();
        for (Route route : Route.loadRoutes(JSONRoutes)) {
            routes.put(route.getRouteId(), route);
        }
        setRouteLatLong();
        this.claimedRoutes = new HashMap<>();

    }

    private void setRouteLatLong() {
        Set<String> seenRoutes = new HashSet<>();
        for (Route route : routes.values()) {
            if (seenRoutes.contains(route.getRouteId())) continue;

            City source = cities.get(route.getSrcCity());
            City dest = cities.get(route.getDestCity());

            if(route.getPairedRoute() == null) {
                route.setSrc(source.getLocation());
                route.setDest(dest.getLocation());
            }
            else {
                Route pairedRoute = route.getPairedRoute();
                LatLng[] parallelCoords = makeParallelCoordinates(source.getLocation(), dest.getLocation());
                route.setSrc(parallelCoords[0]);
                route.setDest(parallelCoords[1]);
                pairedRoute.setSrc(parallelCoords[2]);
                pairedRoute.setDest(parallelCoords[3]);

                seenRoutes.add(pairedRoute.getRouteId());
            }
        }
    }

    private LatLng[] makeParallelCoordinates(LatLng src, LatLng dest) {
        double numerator = src.latitude - dest.latitude;
        double denominator = src.longitude - dest.longitude;
        //denominator and numerator refer to the slope of the current line, not the perpendicular, which is the inverse
        double perpSlope = -denominator / numerator;

        final double DISTANCE = 0.5;
        double xincrement = DISTANCE * Math.cos(Math.atan(perpSlope));
        double yincrement = DISTANCE * Math.sin(Math.atan(perpSlope));
        LatLng srcCoord1 = new LatLng(src.latitude + yincrement, src.longitude + xincrement);
        LatLng destCoord1 = new LatLng(dest.latitude + yincrement, dest.longitude + xincrement);
        LatLng srcCoord2 = new LatLng(src.latitude - yincrement, src.longitude - xincrement);
        LatLng destCoord2 = new LatLng(dest.latitude - yincrement, dest.longitude - xincrement);
        LatLng[] coords = {srcCoord1, destCoord1, srcCoord2, destCoord2};
        return coords;
    }

    public Collection<City> getCities() {
        return cities.values();
    }

    public Collection<Route> getRoutes() {
        return routes.values();
    }

    public void claimRoute(Player p, String routeID) {
        p.addRoute(routeID);
        int color = Util.getPlayerColorCode(p.getColor());
        claimedRoutes.put(routeID, p.getId());
        routes.get(routeID).setColor(color);
    }
}
