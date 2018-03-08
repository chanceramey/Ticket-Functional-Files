package com.team.jcti.ttr.playerInfo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team.jcti.ttr.R;

import org.w3c.dom.Text;

import java.util.List;

import model.Player;

public class PlayerInfoActivity extends AppCompatActivity {
    private PlayerInfoPresenter mPresenter;
    private RecyclerView mRecycler;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_info);
        mPresenter = new PlayerInfoPresenter(this);
        mRecycler = (RecyclerView) findViewById(R.id.recycler_view_player_info);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mPresenter.update();
    }

    public void setPlayerInfo(List<Player> players) {
        adapter = new Adapter(this, players);
        mRecycler.setAdapter(adapter);
    }

    class Adapter extends RecyclerView.Adapter<Holder> {

        private List<Player> players;
        private LayoutInflater inflater;

        public Adapter(Context context, List<Player> players) {
            this.players = players;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.player_info_list, parent, false);
            Holder viewholder = new Holder(view);
            view.setOnClickListener(viewholder);
            return viewholder;
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Player item = players.get(position);
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return players.size();
        }

    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView playerName;
        private TextView points;
        private TextView trains;
        private TextView turn;
        private Player player;


        public Holder(View view) {
            super(view);
            playerName = (TextView) view.findViewById(R.id.player_name);
            points = (TextView) view.findViewById(R.id.player_points);
            trains = (TextView) view.findViewById(R.id.player_num_trains);
            turn = (TextView) view.findViewById(R.id.player_turn);
        }

        @Override
        public void onClick(View view) {
        }

        void bind(Player p) {
            this.player = p;
            playerName.setText(p.getUser());
            points.setText(p.getPoints());
            trains.setText(p.getNumTrains());
            if (p.isTurn()) {
                turn.setText("**YES**");
            }
        }
    }
}
