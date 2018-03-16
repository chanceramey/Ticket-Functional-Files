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

import model.TrainCard;

/**
 * Created by Chance on 3/7/18.
 */

public class City {

    private String name;
    private List<Route> edges;
    private LatLng location;

    public City(String name, double lat, double lon) {
        this.name = name;
        this.location = new LatLng(lat, lon);
        edges = new ArrayList<>();
    }

    public String getName() {
        return name;
    }


   public LatLng getLocation() { return this.location; }

    public static List<City> loadCities(String JSONcities) {
        List<City> cities = new ArrayList<>();
        try {
            JSONArray citiesArray = new JSONArray(JSONcities);
            for (int i = 0; i < citiesArray.length(); i++) {
                JSONObject thisObject = citiesArray.getJSONObject(i);
                double lat = Double.parseDouble(thisObject.getString("lat"));
                double lon = Double.parseDouble(thisObject.getString("long"));
                City city = new City(thisObject.getString("name"), lat, lon);
                cities.add(city);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cities;
    }

}
