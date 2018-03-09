package com.team.jcti.ttr.game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.team.jcti.ttr.R;

public class GameActivity extends AppCompatActivity implements IGameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }
}