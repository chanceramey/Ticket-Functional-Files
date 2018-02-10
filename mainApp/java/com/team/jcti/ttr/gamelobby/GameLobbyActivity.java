package com.team.jcti.ttr.gamelobby;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.team.jcti.ttr.R;


/**
 * Created by Tanner Jensen on 2/4/2018.
 */
public class GameLobbyActivity extends AppCompatActivity {

    private GameLobbyPresenter mPresenter = new GameLobbyPresenter(this);
    private FragmentManager fm = getSupportFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = getFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }

    }

    private Fragment getFragment() {
        if(!mPresenter.hasGame()) {
            return new CreateGameFragment();
        }
        else {
            return new GameLobbyFragment();
        }
    }

    public void switchFragments() {
        toast("Entering Game Lobby!");
        startActivity(new Intent(this, GameLobbyActivity.class));
    }

    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}

