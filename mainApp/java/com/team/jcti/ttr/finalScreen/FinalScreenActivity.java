package com.team.jcti.ttr.finalScreen;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.games.Players;
import com.team.jcti.ttr.R;
import com.team.jcti.ttr.playerInfo.PlayerInfoActivity;
import com.team.jcti.ttr.utils.Util;

import java.util.List;

import model.Player;

public class FinalScreenActivity extends AppCompatActivity {
    FinalScreenPresenter mPresenter;
    private RecyclerView mRecycler;
    private Adapter mAdapter;
    private TextView mWinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_screen);
        mRecycler = (RecyclerView) findViewById(R.id.recycler_view_final_screen);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mWinner = (TextView) findViewById(R.id.final_winner);
        mPresenter = new FinalScreenPresenter(this);
        mWinner.setText(mPresenter.getWinner());
        mPresenter.startRecyclerView();

    }

    public void startRecyclerView(List<Player> players) {
        mAdapter = new Adapter(this, players);
        mRecycler.setAdapter(mAdapter);
    }

    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
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
            View view = inflater.inflate(R.layout.final_screen_list, parent, false);
            Holder viewHolder = new Holder(view);
            return viewHolder;
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
        private TextView routePoints;
        private TextView destPoints;
        private TextView unfinishedDestPoints;
        private TextView totalPoints;
        private TextView longestPath;
        private Player player;


        public Holder(View view) {
            super(view);
            playerName = (TextView) view.findViewById(R.id.final_player_name);
            routePoints = (TextView) view.findViewById(R.id.final_routes);
            destPoints = (TextView) view.findViewById(R.id.final_destination);
            unfinishedDestPoints = (TextView) view.findViewById(R.id.final_unfinished_dest);
            totalPoints = (TextView) view.findViewById(R.id.final_total);
            longestPath = (TextView) view.findViewById(R.id.final_longest_path);
        }

        @Override
        public void onClick(View view) {
        }

        void bind(Player p) {
            p.calculateDestCardPoints(); // this will calculate the finished and unfinished dest card points
            this.player = p;
            playerName.setText(p.getUser());
            routePoints.setText(Integer.toString(mPresenter.getRoutePoints(p.getRoutesClaimed())));
            destPoints.setText(Integer.toString(p.getDestCardPoints()));
            unfinishedDestPoints.setText(Integer.toString(p.getUnfinishedDestCardPoints()));
            totalPoints.setText(Integer.toString(p.getPoints()));
            longestPath.setText(Integer.toString(p.getLongestRoutePoints()));
//            if (p.hasLongestPath()) {        //
//                longestPath.setText(10);
//            }
        }

    }

}
