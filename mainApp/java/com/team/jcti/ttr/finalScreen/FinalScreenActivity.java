package com.team.jcti.ttr.finalScreen;

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
import android.widget.TextView;
import android.widget.Toast;

import com.team.jcti.ttr.R;
import com.team.jcti.ttr.gamelist.GameListActivity;

import java.util.List;

import model.FinalGamePoints;
import model.playerStates.Player;

public class FinalScreenActivity extends AppCompatActivity {
    FinalScreenPresenter mPresenter;
    private RecyclerView mRecycler;
    private Adapter mAdapter;
    private TextView mWinner;
    private List<Player> players;
    private Button mPlayAgainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_screen);
        mPlayAgainButton = (Button) findViewById(R.id.play_again_button);
        mPlayAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.playAgain();
            }
        });
        mRecycler = (RecyclerView) findViewById(R.id.recycler_view_final_screen);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mWinner = (TextView) findViewById(R.id.final_winner);
        mPresenter = new FinalScreenPresenter(this);
        mWinner.setText(mPresenter.getWinner());
        mPresenter.startRecyclerView();
        this.players = mPresenter.getPlayers();
    }

    public void startRecyclerView(FinalGamePoints[] finalGamePoints) {
        mAdapter = new Adapter(this, finalGamePoints);
        mRecycler.setAdapter(mAdapter);
    }

    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void enterGameListActivity() {
        Intent intent = new Intent(this, GameListActivity.class);
        startActivity(intent);
    }


    class Adapter extends RecyclerView.Adapter<Holder> {

        private FinalGamePoints[] mFinalGamePoints;
        private LayoutInflater inflater;

        public Adapter(Context context, FinalGamePoints[] finalGamePoints) {
            this.mFinalGamePoints = finalGamePoints;
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
            FinalGamePoints item = mFinalGamePoints[position];
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return mFinalGamePoints.length;
        }



    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView playerName;
        private TextView routePoints;
        private TextView destPoints;
        private TextView unfinishedDestPoints;
        private TextView totalPoints;
        private TextView longestPath;
        private Player thisPlayer;


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

        void bind(FinalGamePoints fgp) {
            //TODO: ORDER THESE FROM WINNER TO LOSER
            this.thisPlayer = players.get(fgp.getPlayerNumber());
            playerName.setText(thisPlayer.getUser());
            routePoints.setText(Integer.toString(fgp.getRoutePoints()));
            destPoints.setText(Integer.toString(fgp.getFinishedDestPoints()));
            unfinishedDestPoints.setText(Integer.toString(fgp.getUnfinishedDestPoints()));
            totalPoints.setText(Integer.toString(fgp.getTotalPoints()));
            longestPath.setText(Integer.toString(fgp.getLongestPathPoints()));
        }



    }

}
