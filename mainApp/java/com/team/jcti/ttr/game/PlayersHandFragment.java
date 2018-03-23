package com.team.jcti.ttr.game;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.team.jcti.ttr.R;
import com.team.jcti.ttr.models.ClientGameModel;
import com.team.jcti.ttr.utils.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.DestinationCard;
import model.TrainCard;


public class PlayersHandFragment extends Fragment {
    private ClientGameModel mClientGameModel = ClientGameModel.getInstance();
    private GamePresenter mPresenter;

    private RecyclerView cardRecyclerView;
    private RecyclerView.Adapter adapter;

    private TextView numCardsText;
    private Button switchCardsButton;


    private boolean trainCards;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_players_hand, container, false);
        this.mPresenter = (GamePresenter) mClientGameModel.getActivePresenter();
        mPresenter.setPlayersHandFragment(this);


        cardRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_players_hand);
        cardRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        numCardsText = (TextView) v.findViewById(R.id.number_cards_text_view);

        switchCardsButton = (Button) v.findViewById(R.id.switch_cards_button);
        switchCardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trainCards = !trainCards;
                updateCardList();
            }
        });



        trainCards = true;
        updateCardList();

        return v;
    }

    public void updateCardList() {
        int numCards = 0;
        if(trainCards) {
             List<TrainCard> cards = new ArrayList<TrainCard>(Arrays.asList(TrainCard.values()));
             numCards = cards.size();
             adapter = new TrainCardAdapter(getActivity(), cards);
             switchCardsButton.setText("View Destination Cards");
        } else {
            List<DestinationCard> cards = mPresenter.getPlayerDestCards();
            numCards = cards.size();
            adapter = new DestCardAdapter(getActivity(), cards);
            switchCardsButton.setText("View Train Cards");
        }
        cardRecyclerView.setAdapter(adapter);
        numCardsText.setText(String.format("Number of Cards: %d", numCards));

    }

    class TrainCardAdapter extends RecyclerView.Adapter<TrainCardHolder> {
        private List<TrainCard> mTrainCards;
        private LayoutInflater mLayoutInflater;

        public TrainCardAdapter(Context context, List<TrainCard> items) {
            this.mTrainCards = items;
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public TrainCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mLayoutInflater.inflate(R.layout.card_list_item, parent, false);
            TrainCardHolder viewholder = new TrainCardHolder(view);
            return viewholder;
        }

        @Override
        public void onBindViewHolder(TrainCardHolder holder, int position) {
            TrainCard card = mTrainCards.get(position);
            holder.bind(card);
        }

        @Override
        public int getItemCount() {
            return mTrainCards.size();
        }
    }

    class TrainCardHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;

        public TrainCardHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.card_image_view);
        }



        void bind(TrainCard card) {
            Drawable drawable = getResources().getDrawable(Util.getTrainCardDrawable(card));
            mTextView.setBackground(drawable);
            mTextView.setText(mPresenter.getNumCards(card));
        }


    }

    class DestCardAdapter extends RecyclerView.Adapter<DestCardHolder> {

        private List<DestinationCard> mDestinationCards;
        private LayoutInflater inflater;

        public DestCardAdapter(Context context, List<DestinationCard> items) {
            this.mDestinationCards = items;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public DestCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.card_list_item, parent, false);
            DestCardHolder viewholder = new DestCardHolder(view);
            return viewholder;
        }

        @Override
        public void onBindViewHolder(DestCardHolder holder, int position) {
            DestinationCard item = mDestinationCards.get(position);
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return mDestinationCards.size();
        }

    }

    class DestCardHolder extends RecyclerView.ViewHolder {

        private TextView mImageView;


        public DestCardHolder(View view) {
            super(view);
            mImageView = (TextView) view.findViewById(R.id.card_image_view);
        }


        void bind(DestinationCard item) {
            mImageView.setText(item.toString());
            mImageView.setBackgroundColor(Color.argb(100,0,0,255));
        }
    }


}

