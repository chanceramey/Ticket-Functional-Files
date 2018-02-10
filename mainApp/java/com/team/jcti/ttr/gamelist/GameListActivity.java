package com.team.jcti.ttr.gamelist;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.team.jcti.ttr.R;
import com.team.jcti.ttr.communication.ServerProxy;
import com.team.jcti.ttr.gamelobby.GameLobbyActivity;
import com.team.jcti.ttr.models.GameModel;

import org.w3c.dom.Text;

import model.Game;


public class GameListActivity extends AppCompatActivity implements IGameListActivity {

    private GameListPresenter mPresenter = new GameListPresenter();
    Button mCreateButton;
    private RecyclerView mRecyclerGames;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_join);
        mRecyclerGames = (RecyclerView) findViewById(R.id.recycler_view_games);
        mRecyclerGames.setLayoutManager(new LinearLayoutManager(this));
        //setGamesList(games);

        mCreateButton = (Button) findViewById(R.id.create_game_button);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateGame();
            }
        });

    }

    @Override
    public void onCreateGame() {
        Intent intent = new Intent(this, GameLobbyActivity.class);
        startActivity(intent);
    }

    @Override
    public void onJoin(String gameId) {

    }

    public void setGamesList(GameModel[] games) {
        adapter = new Adapter(this, games);
        mRecyclerGames.setAdapter(adapter);
    }

    public void startGameLobbyActivity(String gameId) {
        Intent intent = new Intent(this, GameLobbyActivity.class);
        intent.putExtra("gameId", gameId);
        startActivity(intent);
    }


    class Adapter extends RecyclerView.Adapter<Holder> {

        private GameModel[] items;
        private LayoutInflater inflater;

        public Adapter(Context context, GameModel[] items) {
            this.items = items;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.list_item, parent, false);
            return new Holder(view, items);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            String host = items[position].getHost();
            String players = items[position].getPlayers();
            String playersAllowed = items[position].getPlayersAllowed();
            String playersInLobby = players + "/" + playersAllowed;
            holder.bind(host, playersInLobby);
        }

        @Override
        public int getItemCount() {
            return items.length;
        }

    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView hostName;
        private String item;
        private GameModel[] model;
        TextView numPlayers;

        public Holder(View view, GameModel[] model) {
            super(view);
            hostName = (TextView) view.findViewById(R.id.host_name);
            numPlayers = (TextView) view.findViewById(R.id.num_players);
            hostName.setOnClickListener(this);
            this.model = model;

        }

        @Override
        public void onClick(View view) {
            int index = getAdapterPosition();
            onJoin(model[index].getGameId());
            startGameLobbyActivity(model[index].getGameId());

        }

        void bind(String host, String playersInLobby) {
            this.item = item;
            hostName.setText(host);
            numPlayers.setText(playersInLobby);
        }
    }
}
