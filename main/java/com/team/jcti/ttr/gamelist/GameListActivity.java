package com.team.jcti.ttr.gamelist;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.team.jcti.ttr.R;


public class GameListActivity extends AppCompatActivity implements IGameListActivity {

    private GameListPresenter mPresenter = new GameListPresenter();
    Button mCreateButton;
    Button mJoinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_join);
    }

    @Override
    public void onCreate(String username, int numPlayers) {

    }

    @Override
    public void onJoin(String username, String gameId) {

    }
}
