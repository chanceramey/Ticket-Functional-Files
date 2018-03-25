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
import android.widget.Toast;

import com.team.jcti.ttr.R;
import com.team.jcti.ttr.models.ClientGameModel;
import com.team.jcti.ttr.utils.Util;

import org.w3c.dom.Text;

import java.util.List;

import model.Player;
import model.StateType;

public class PlayerInfoActivity extends AppCompatActivity {
    private PlayerInfoPresenter mPresenter;
    private RecyclerView mRecycler;
    private Adapter adapter;
    public int COUNTER = 0;
    public List<Player> players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_info);
        mPresenter = new PlayerInfoPresenter(this);
        mRecycler = (RecyclerView) findViewById(R.id.recycler_view_player_info);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        players = mPresenter.getPlayers();
        adapter = new Adapter(this, players);
        mRecycler.setAdapter(adapter);
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
            View view = inflater.inflate(R.layout.player_info_list, parent, false);
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

        private TextView playerNumber;
        private TextView playerName;
        private TextView points;
        private TextView trains;
        private TextView turn;
        private TextView numTrainCards;
        private TextView numDestCards;
        private Player player;


        public Holder(View view) {
            super(view);
            playerNumber = (TextView) view.findViewById(R.id.player_num);
            playerName = (TextView) view.findViewById(R.id.player_name);
            points = (TextView) view.findViewById(R.id.player_points);
            trains = (TextView) view.findViewById(R.id.player_num_trains);
            turn = (TextView) view.findViewById(R.id.player_turn);
            numTrainCards = (TextView) view.findViewById(R.id.player_num_train_cards);
            numDestCards = (TextView) view.findViewById(R.id.player_num_dest_cards);
        }

        @Override
        public void onClick(View view) {
        }

        void bind(Player p) {
            this.player = p;
            COUNTER++;
            playerNumber.setText(Integer.toString(COUNTER));
            playerName.setText(p.getUser());
            points.setText(Integer.toString(p.getPoints()));
            trains.setText(Integer.toString(p.getNumTrains()));
            numTrainCards.setText(Integer.toString(p.getNumTrainCards()));
            numDestCards.setText(Integer.toString(p.getNumDestCards()));
            setColor();

            if (p.getState() == StateType.TURN_STATE) {
                turn.setText("Yes");
            } else  {
                turn.setText("No");

            }
        }

        void setColor() {
            playerNumber.setTextColor(Util.getPlayerColorCode(this.player.getColor()));
            playerName.setTextColor(Util.getPlayerColorCode(this.player.getColor()));
            points.setTextColor(Util.getPlayerColorCode(this.player.getColor()));
            trains.setTextColor(Util.getPlayerColorCode(this.player.getColor()));
            numTrainCards.setTextColor(Util.getPlayerColorCode(this.player.getColor()));
            numDestCards.setTextColor(Util.getPlayerColorCode(this.player.getColor()));
            turn.setTextColor(Util.getPlayerColorCode(this.player.getColor()));
        }
    }

}
