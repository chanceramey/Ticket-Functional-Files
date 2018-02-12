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

import org.w3c.dom.Text;

import model.Game;
import model.User;


public class GameListActivity extends AppCompatActivity implements IGameListActivity {

    private GameListPresenter mPresenter;
    Button mCreateButton;
    private RecyclerView mRecyclerGames;
    private Adapter adapter;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_join);

        mPresenter  = new GameListPresenter(this);
        mUser = mPresenter.getUser();
        mPresenter.getGames();
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
        mPresenter.create();
    }

    @Override
    public void onJoin(String username, String gameId) {
        mPresenter.join(username, gameId);
    }

    public void setGamesList(Game[] games) {
        mRecyclerGames = (RecyclerView) findViewById(R.id.recycler_view_games);
        mRecyclerGames.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, games);
        mRecyclerGames.setAdapter(adapter);
    }



    class Adapter extends RecyclerView.Adapter<Holder> {

        private Game[] items;
        private LayoutInflater inflater;

        public Adapter(Context context, Game[] items) {
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
            int players = items[position].getNumPlayers();
            String playersInLobby = players + " waiting";
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
        private Game[] model;
        TextView numPlayers;

        public Holder(View view, Game[] model) {
            super(view);
            hostName = (TextView) view.findViewById(R.id.host_name);
            numPlayers = (TextView) view.findViewById(R.id.num_players);
            hostName.setOnClickListener(this);
            this.model = model;

        }

        @Override
        public void onClick(View view) {
            int index = getAdapterPosition();
            onJoin(mUser.getUsername(), model[index].getGameID());
           // startGameLobbyActivity(model[index].getGameId());

        }

        void bind(String host, String playersInLobby) {
            this.item = item;
            hostName.setText(host);
            numPlayers.setText(playersInLobby);
        }
    }
}
