package com.team.jcti.ttr.game;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.team.jcti.ttr.R;
import com.team.jcti.ttr.drawdestinationcard.DrawDestinationCardActivity;
import com.team.jcti.ttr.message.MessageActivity;
import com.team.jcti.ttr.models.ClientGameModel;
import com.team.jcti.ttr.playerInfo.PlayerInfoActivity;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity implements IGameActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_menu:
                startActivity(new Intent(this, MessageActivity.class));
                return true;
            case R.id.action_scoreboard:
                startActivity(new Intent(this, PlayerInfoActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public GamePresenter getGamePresenter() {
        return mGamePresenter;
    }

    GamePresenter mGamePresenter;
    ClientGameModel mClientGameModel;


    private FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mGamePresenter = new GamePresenter(this);
        mClientGameModel = ClientGameModel.getInstance();
        mClientGameModel.setActivePresenter(mGamePresenter);

        Fragment playerCardFragment = fm.findFragmentById(R.id.players_card_fragment);
        if(playerCardFragment == null) {
            playerCardFragment = new PlayersHandFragment();
            fm.beginTransaction().add(R.id.players_card_fragment, playerCardFragment).commit();
        }

        Fragment decksAndCardFragment = fm.findFragmentById(R.id.deck_fragment);
        if(decksAndCardFragment == null) {
            decksAndCardFragment = new DecksAndCardsFragment();
            fm.beginTransaction().add(R.id.deck_fragment, decksAndCardFragment).commit();
        }

        Fragment mapFragment = fm.findFragmentById(R.id.map_fragment);
        if(mapFragment == null) {
            mapFragment = new BoardFragment();
            fm.beginTransaction().add(R.id.map_fragment, mapFragment).commit();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGamePresenter.makeActivePresenter();
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

    @Override
    public void enterDrawDestinationActivity() {
        Intent intent = new Intent(this, DrawDestinationCardActivity.class);
        startActivity(intent);
    }

    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}
