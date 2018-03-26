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
import com.team.jcti.ttr.models.City;
import com.team.jcti.ttr.utils.AppUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.team.jcti.ttr.models.Route;

public class BoardFragment extends android.support.v4.app.Fragment {

    private GoogleMap mMap;
    private View mView;
    private City[] markedCities = new City[] {};
    private Map<Polyline, Route> polylines = new HashMap<>();
    private Map<Polyline, Marker> railLines = new HashMap<>();
    private Map<Marker, Polyline> midPointsToRailLines = new HashMap<>();
    private final double ZOOM_PREFERENCE = 4.5;
    private final int WIDTH = 15;
    private final double DISTANCE = 0.5;
    SupportMapFragment supportMapFragment;
    private GameActivity mGameActivity;
    private GamePresenter mGamePresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
       if(mMap != null) update();

    }

    public void update() {
        mMap.clear();
        drawCities();
        drawRailLines();
        drawMarkedCities();
    }

    private void drawMarkedCities() {
        for (City city : markedCities) {
           mMap.addMarker(new MarkerOptions().position(city.getLocation()));
        }
    }



    public void enableClaiming() {

        for (Map.Entry<Polyline, Marker> entry : railLines.entrySet()) {

            mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
                @Override
                public void onPolylineClick(Polyline polyline) {
                    mGamePresenter.claimRoute(polylines.get(polyline).getRouteId()); // do we need this???
                }
            });

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    if (railLines.containsValue(marker)) {
                        Polyline polyline = midPointsToRailLines.get(marker);
                        mGamePresenter.claimRoute(polylines.get(polyline).getRouteId()); // added this
                    }
                    return false;
                }
            });

        }
    }

    public void drawCities() {
        for(City city : mGamePresenter.getCities()){
            LatLng adjustedDown = new LatLng(city.getLocation().latitude - DISTANCE, city.getLocation().longitude);
            mMap.addMarker(new MarkerOptions()
                    .position(adjustedDown)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.cityiconbig)));
        }
    }

    public void drawRailLines(){

        railLines.clear();
        midPointsToRailLines.clear();
        polylines.clear();

        for (Route route : mGamePresenter.getRoutes()) {

                int image = AppUtil.getImage(route.getLength());

                int color = route.getRouteColor();


                double midLat = (route.getSrc().latitude + route.getDest().latitude) / 2 - .05;
                double midLong = (route.getSrc().longitude + route.getDest().longitude) / 2;
                LatLng midpoint = new LatLng(midLat, midLong);
                Polyline thisLine = mMap.addPolyline(new PolylineOptions()
                        .add(route.getSrc(), route.getDest())
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
        enableClaiming();

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_board, container, false);
        mGameActivity = (GameActivity) getActivity();
        mGamePresenter = mGameActivity.getGamePresenter();
        mGamePresenter.setBoardFragment(this);

        if(mGamePresenter.getBoard() == null) {
            try {
                String JSONCities = AppUtil.getStringFromResourceFile(getActivity(), R.raw.cities);
                String JSONRoutes = AppUtil.getStringFromResourceFile(getActivity(), R.raw.routes);
                mGamePresenter.initializeBoard(JSONCities, JSONRoutes);

            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        supportMapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);

        supportMapFragment.getMapAsync(new MapReady());

        return mView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        //inflater.inflate(R.menu.map_menu, menu);

    }

    public void drawMarkersForCities(City[] cities) {
        this.markedCities = cities;
        update();
    }

    public void clearCityMarkers() {
        this.markedCities = new City[] {};
        update();
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

           update();
        }

    }

}