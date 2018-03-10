package com.team.jcti.ttr.gamelobby;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.team.jcti.ttr.R;
import com.team.jcti.ttr.drawdestinationcard.DrawDestinationCardActivity;
import com.team.jcti.ttr.game.GameActivity;
import com.team.jcti.ttr.gamelist.GameListActivity;
import com.team.jcti.ttr.message.MessageActivity;
import com.team.jcti.ttr.playerInfo.PlayerInfoActivity;


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
        Fragment myFragment = null;
        Boolean hasGame = mPresenter.hasGame();
        if(hasGame) {
            myFragment = new GameLobbyFragment();
        } else {
            myFragment = new CreateGameFragment();
        }

        return myFragment;
    }

    public void switchFragments() {
        toast("Entering Game Lobby!");
        startActivity(new Intent(this, GameLobbyActivity.class));
    }

    public void enterGameList() {
        Intent intent = new Intent(this, GameListActivity.class);
        startActivity(intent);
    }

    public void enterGameActivity() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}

