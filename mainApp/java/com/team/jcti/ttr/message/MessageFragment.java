package com.team.jcti.ttr.message;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team.jcti.ttr.IPresenter;
import com.team.jcti.ttr.R;
import com.team.jcti.ttr.models.ClientGameModel;
import com.team.jcti.ttr.models.ClientModel;

import java.util.List;

import model.GameHistory;


public class MessageFragment extends Fragment {
   private RecyclerView mHistoryRecycler;
   private Adapter mAdapter;
   private ClientGameModel mClientGameModel = ClientGameModel.getInstance();
   private MessagePresenter mPresenter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_message, container, false);
        this.mPresenter = (MessagePresenter) mClientGameModel.getActivePresenter();
        mPresenter.setFragment(this);
        mHistoryRecycler = (RecyclerView) v.findViewById(R.id.recycler_view_chat);
        mHistoryRecycler.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mPresenter.update();

        return v;
    }

    public void setHistory(List<GameHistory> messages) {
        mAdapter = new Adapter(this.getActivity(), messages);
        mHistoryRecycler.setAdapter(mAdapter);
    }


    class Adapter extends RecyclerView.Adapter<Holder> {

        private List<GameHistory> items;
        private LayoutInflater inflater;

        public Adapter(Context context, List<GameHistory> items) {
            this.items = items;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.game_history_list, parent, false);
            Holder viewholder = new Holder(view);
            view.setOnClickListener(viewholder);
            return viewholder;
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            GameHistory item = items.get(position);
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView playerName;
        private TextView message;
        private GameHistory item;


        public Holder(View view) {
            super(view);
            playerName = (TextView) view.findViewById(R.id.history_user_name);
            message = (TextView) view.findViewById(R.id.history_message);
        }

        @Override
        public void onClick(View view) {
        }

        void bind(GameHistory item) {
            this.item = item;
            playerName.setText(item.getPlayerName());
            message.setText(item.toString());
        }
    }
}
