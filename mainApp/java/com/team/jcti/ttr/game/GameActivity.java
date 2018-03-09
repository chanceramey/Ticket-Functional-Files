package com.team.jcti.ttr.game;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.team.jcti.ttr.R;

public class GameActivity extends AppCompatActivity implements IGameActivity {

    GamePresenter mGamePresenter;


    private FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mGamePresenter = new GamePresenter(this);
        Fragment fragment = fm.findFragmentById(R.id.players_card_fragment);
        if(fragment == null) {
            fragment = new PlayersHandFragment();
            fm.beginTransaction().add(R.id.players_card_fragment, fragment).commit();
        }

    }

}
