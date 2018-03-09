package com.team.jcti.ttr.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.team.jcti.ttr.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameActivity extends AppCompatActivity implements IGameActivity {

    public GamePresenter getGamePresenter() {
        return mGamePresenter;
    }

    GamePresenter mGamePresenter = new GamePresenter(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment currentFragment = this.getSupportFragmentManager().findFragmentById(R.id.boardFrame);
        if (currentFragment != null) {
            transaction.remove(currentFragment);
        }
        currentFragment = new BoardFragment();
        Bundle args = new Bundle();
        currentFragment.setArguments(args);
        transaction.add(R.id.boardFrame, currentFragment);
        transaction.commit();


    }


    @Override
    public void displayErrorMessages(ArrayList<String> errorMessages) {
        StringBuilder sbr = new StringBuilder();

        for (String errorMessage : errorMessages) {
            sbr.append(errorMessage);
            sbr.append("\n");
        }

        toast(sbr.toString());

    }

    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
