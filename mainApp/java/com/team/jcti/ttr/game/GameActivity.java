package com.team.jcti.ttr.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.team.jcti.ttr.R;
import com.team.jcti.ttr.message.MessageActivity;
import com.team.jcti.ttr.playerInfo.PlayerInfoActivity;

public class GameActivity extends AppCompatActivity implements IGameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }

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
}
