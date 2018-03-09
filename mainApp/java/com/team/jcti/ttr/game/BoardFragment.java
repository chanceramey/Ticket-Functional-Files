package com.team.jcti.ttr.game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.team.jcti.ttr.R;
import com.team.jcti.ttr.utils.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import model.Color;
import model.Route;
import model.TrainCard;

public class BoardFragment extends android.support.v4.app.Fragment {

    private GoogleMap mMap;
    private View mView;
    private Map<String, Route> routeIdtoRoutes = new HashMap<>();
    private Map<Polyline, Route> polylines = new HashMap<>();
    private Map<String, LatLng> cities = new HashMap<>();
    private String JSONRoutes;
    private JSONArray routesArray;
    private Map<Polyline, Marker> railLines = new HashMap<>();
    private Map<Marker, Polyline> midPointsToRailLines = new HashMap<>();
    private final double ZOOM_PREFERENCE = 4.5;
    private final int WIDTH = 15;
    private final double DISTANCE = 0.5;
    SupportMapFragment supportMapFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    public void createCities() {
        cities.put("vancouver", new LatLng(49.2827, -123.1207));
        cities.put("calgary", new LatLng(51.0486, -114.0708));
        cities.put("winnipeg", new LatLng(49.8951, -97.1384));
        cities.put("sault_st_marie", new LatLng(46.51677, -84.33325));
        cities.put("montreal", new LatLng(45.501689, -73.567256));
        cities.put("seattle", new LatLng(47.606209, -122.332071));
        cities.put("helena", new LatLng(46.588371, -112.024505));
        cities.put("duluth", new LatLng(46.786672, -92.100485));
        cities.put("toronto", new LatLng(43.653226, -79.383184));
        cities.put("boston", new LatLng(42.360082, -71.05888));
        cities.put("new_york", new LatLng(40.712775, -74.005973));
        cities.put("pittsburgh", new LatLng(40.440625, -79.995886));
        cities.put("chicago", new LatLng(41.878114, -87.629798));
        cities.put("omaha", new LatLng(41.252363, -95.997988));
        cities.put("denver", new LatLng(39.739236, -104.990251));
        cities.put("salt_lake_city", new LatLng(40.760779, -111.891047));
        cities.put("portland", new LatLng(45.523062, -122.676482));
        cities.put("san_francisco", new LatLng(37.774929, -122.419416));
        cities.put("los_angeles", new LatLng(34.052234, -118.243685));
        cities.put("las_vegas", new LatLng(36.169941, -115.13983));
        cities.put("phoenix", new LatLng(33.448377, -112.074037));
        cities.put("santa_fe", new LatLng(35.686975, -105.937799));
        cities.put("oklahoma_city", new LatLng(35.467560, -97.516428));
        cities.put("kansas_city", new LatLng(39.099727, -94.578567));
        cities.put("saint_louis", new LatLng(38.627003, -90.199404));
        cities.put("nashville", new LatLng(36.162664, -86.781602));
        cities.put("atlanta", new LatLng(33.748995, -84.387982));
        cities.put("raleigh", new LatLng(35.779590, -78.638179));
        cities.put("washington", new LatLng(38.907192, -77.036871));
        cities.put("charleston", new LatLng(32.776475, -79.931051));
        cities.put("miami", new LatLng(25.761680, -80.19179));
        cities.put("new_orleans", new LatLng(29.951066, -90.071532));
        cities.put("houston", new LatLng(29.760427, -95.369803));
        cities.put("dallas", new LatLng(32.776664, -96.796988));
        cities.put("el_paso", new LatLng(31.761878, -106.485022));
        cities.put("little_rock", new LatLng(34.7465, -92.2896));

    }



    @Override
    public void onResume() {
        super.onResume();

    }

    private String getRouteID(JSONObject object) throws JSONException{
        StringBuilder sb = new StringBuilder();
        sb.append("from ");
        sb.append(object.getString("from"));
        sb.append(" to ");
        sb.append(object.getString("to"));
        return sb.toString();
    }

    public void createRoutes() {

        try {
            this.JSONRoutes = Util.getStringFromResourceFile(getContext(), R.raw.routes);
            routesArray = new JSONArray(JSONRoutes);
            for (int i = 0; i < routesArray.length(); i++) {
                JSONObject thisObject = (JSONObject) routesArray.get(i);

                Route route;
                if (thisObject.has("double_color")) {
                    route = new Route(TrainCard.valueOf(thisObject.getString("color")), getRouteID(thisObject), thisObject.getString("from"), thisObject.getString("to"), thisObject.getInt("cost"), TrainCard.valueOf(thisObject.getString("double_color")));
                } else {
                    route = new Route(TrainCard.valueOf(thisObject.getString("color")), getRouteID(thisObject), thisObject.getString("from"), thisObject.getString("to"), thisObject.getInt("cost"));
                }
                routeIdtoRoutes.put(route.getRouteId(), route);
            }
            createDuplicateRailLines();
            drawRailLines();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void enableClaiming(final Color playerColor) {

        for (Map.Entry<Polyline, Marker> entry : railLines.entrySet()) {

            mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
                @Override
                public void onPolylineClick(Polyline polyline) {
                    ((GameActivity)getActivity()).getGamePresenter().claimRoute(polylines.get(polyline).getRouteId());
                }
            });

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    if (railLines.containsValue(marker)) {
                        Polyline polyline = midPointsToRailLines.get(marker);
                        claimRoute(polylines.get(polyline).getRouteId(), playerColor);
                    }
                    return false;
                }
            });

        }
    }



    public void createDuplicateRailLines() {
        List<Route> routesToAdd = new ArrayList<>();
        for (Route route : routeIdtoRoutes.values()) {
            String id1 = UUID.randomUUID().toString();
            String id2 = UUID.randomUUID().toString();
            if (route.getColorOfDuplicate() != null) {
                LatLng[] parallelCoords = makeParallelCoordinates(cities.get(route.getSrcCity()), cities.get(route.getDestCity()));
                cities.put(route.getSrcCity()+id1, parallelCoords[0]);
                cities.put(route.getDestCity()+id1, parallelCoords[1]);
                cities.put(route.getSrcCity()+id2, parallelCoords[2]);
                cities.put(route.getDestCity()+id2, parallelCoords[3]);
                routesToAdd.add(new Route(route.getColorOfDuplicate(), route.getRouteId().concat(" (alt)"), route.getSrcCity()+id2, route.getDestCity()+id2, route.getLength()));
                route.setSrcCity(route.getSrcCity()+id1);
                route.setDestCity(route.getDestCity()+id1);
            }
        }
        for (Route route : routesToAdd) {
            this.routeIdtoRoutes.put(route.getRouteId(), route);
        }
    }

    public void drawRailLines(){

        for (Route route : routeIdtoRoutes.values()) {

                int image = Util.getImage(route.getLength());

                int color = Util.getColorCode(route.getRouteColor());
                if(route.isClaimed()) color = Util.getPlayerColorCode(route.getClaimedColor());

                double midLat = (cities.get(route.getSrcCity()).latitude + cities.get(route.getDestCity()).latitude) / 2 - .05;
                double midLong = (cities.get(route.getSrcCity()).longitude + cities.get(route.getDestCity()).longitude) / 2;
                LatLng midpoint = new LatLng(midLat, midLong);
                Polyline thisLine = mMap.addPolyline(new PolylineOptions()
                        .add(cities.get(route.getSrcCity()), cities.get(route.getDestCity()))
                        .clickable(true)
                        .color(color)
                        .width(WIDTH));
                Marker thisMidPoint = mMap.addMarker(new MarkerOptions()
                        .position(midpoint)
                        .icon(BitmapDescriptorFactory.fromResource(image)));
                railLines.put(thisLine, thisMidPoint);
                midPointsToRailLines.put(thisMidPoint, thisLine);
                polylines.put(thisLine, route);
        }

    }




    private LatLng[] makeParallelCoordinates(LatLng src, LatLng dest) {
        double numerator = src.latitude - dest.latitude;
        double denominator = src.longitude - dest.longitude;
        //denominator and numerator refer to the slope of the current line, not the perpendicular, which is the inverse
        double perpSlope = -denominator / numerator;
        double xincrement = DISTANCE * Math.cos(Math.atan(perpSlope));
        double yincrement = DISTANCE * Math.sin(Math.atan(perpSlope));
        LatLng srcCoord1 = new LatLng(src.latitude + yincrement, src.longitude + xincrement);
        LatLng destCoord1 = new LatLng(dest.latitude + yincrement, dest.longitude + xincrement);
        LatLng srcCoord2 = new LatLng(src.latitude - yincrement, src.longitude - xincrement);
        LatLng destCoord2 = new LatLng(dest.latitude - yincrement, dest.longitude - xincrement);
        LatLng[] coords = {srcCoord1, destCoord1, srcCoord2, destCoord2};
        return coords;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_board, container, false);

        ((GameActivity)getActivity()).getGamePresenter().setBoardFragment(this);

        supportMapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);

        supportMapFragment.getMapAsync(new MapReady());

        return mView;
    }



    public void drawCities() {
        for(Map.Entry<String, LatLng> city : cities.entrySet()){
            LatLng adjustedDown = new LatLng(city.getValue().latitude - DISTANCE, city.getValue().longitude);
            mMap.addMarker(new MarkerOptions()
                    .position(adjustedDown)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.cityiconbig)));
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        //inflater.inflate(R.menu.map_menu, menu);

    }

    class MapReady implements OnMapReadyCallback {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            LatLng centerOfUSA = new LatLng(40.0902, -95.7129);
            LatLngBounds.Builder boundBuilder = new LatLngBounds.Builder();
            boundBuilder.include(centerOfUSA);
            LatLngBounds bounds = boundBuilder.build();
            mMap.setLatLngBoundsForCameraTarget(bounds);
            mMap.setMaxZoomPreference((float)ZOOM_PREFERENCE);
            mMap.setMinZoomPreference((float)ZOOM_PREFERENCE);
            mMap.getUiSettings().setAllGesturesEnabled(false);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

            createCities();
            drawCities();
            createRoutes();
            drawRailLines();
            enableClaiming(Color.BLUE);

        }

    }




    public void claimRoute(String routeID, Color color) {
        Route route = routeIdtoRoutes.get(routeID);
        route.setClaimed(true);
        route.setClaimedColor(color);
        drawRailLines();
    }
}