package com.team.jcti.ttr.gamelist;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.team.jcti.ttr.R;
import com.team.jcti.ttr.gamelobby.GameLobbyActivity;
import com.team.jcti.ttr.models.ClientModel;

import org.w3c.dom.Text;

import java.util.List;

import model.Game;


public class GameListActivity extends AppCompatActivity implements IGameListActivity {

    private GameListPresenter mPresenter;
    Button mCreateButton;
    private RecyclerView mRecyclerGames;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_join);

        mPresenter = new GameListPresenter(this);

        mRecyclerGames = (RecyclerView) findViewById(R.id.recycler_view_games);
        mRecyclerGames.setLayoutManager(new LinearLayoutManager(this));

        mCreateButton = (Button) findViewById(R.id.create_new_game);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateGame();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.setActivePresenter();
    }

    @Override
    public void onCreateGame() {
        Intent intent = new Intent(this, GameLobbyActivity.class);
        startActivity(intent);
    }

    @Override
    public void onJoin() {
        Intent intent = new Intent(this, GameLobbyActivity.class);
        startActivity(intent);
    }

    @Override
    public void setGamesList(List<Game> games) {
        adapter = new Adapter(this, games);
        mRecyclerGames.setAdapter(adapter);
    }

    @Override
    public void startGameLobbyActivity(String gameId) {
        Intent intent = new Intent(this, GameLobbyActivity.class);
        intent.putExtra("gameId", gameId);
        startActivity(intent);
    }

    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    class Adapter extends RecyclerView.Adapter<Holder> {

        private List<Game> games;
        private LayoutInflater inflater;

        public Adapter(Context context, List<Game> games) {
            this.games = games;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.list_item, parent, false);
            Holder viewholder = new Holder(view);
            view.setOnClickListener(viewholder);
            return viewholder;
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Game game = games.get(position);
            holder.bind(game);
        }

        @Override
        public int getItemCount() {
            return games.size();
        }

    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView gameName;
        TextView numPlayers;
        private Game game;


        public Holder(View view) {
            super(view);
            gameName = (TextView) view.findViewById(R.id.host_name);
            numPlayers = (TextView) view.findViewById(R.id.num_players);
            //gameName.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mPresenter.join(game.getID());
        }

        void bind(Game game) {
            this.game = game;
            gameName.setText(game.getGameName());
            numPlayers.setText(game.getNumPlayersString());
        }
    }
}