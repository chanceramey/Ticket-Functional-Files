package com.team.jcti.ttr.game;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.team.jcti.ttr.R;
import com.team.jcti.ttr.models.ClientModel;
import com.team.jcti.ttr.utils.Util;

import java.util.ArrayList;
import java.util.List;

import model.TrainCard;


public class DecksAndCardsFragment extends Fragment {

    private List<ImageView> faceUpCardIVs;
    private ImageView destinationCardDeck;
    private ImageView trainCardDeck;
    private TextView destDeckCount;
    private TextView trainDeckCount;
    private GamePresenter mGamePresenter;


    public DecksAndCardsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_decks_and_cards, container, false);
        this.mGamePresenter = ((GameActivity)getActivity()).getGamePresenter();
        mGamePresenter.setDecksAndCardsFragment(this);
        setUpView(v);

        return v;
    }

    public void setUpView(View v) {

        // initialize deck views
        trainCardDeck = (ImageView) v.findViewById(R.id.train_deck);
        destinationCardDeck = (ImageView) v.findViewById(R.id.destination_deck);
        destDeckCount = (TextView) v.findViewById(R.id.dest_deck_count);
        trainDeckCount = (TextView) v.findViewById(R.id.train_deck_count);
        destinationCardDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mGamePresenter.verifyTurn()){
                    mGamePresenter.onDestDeckClick();
                }
            }
        });

        trainCardDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mGamePresenter.verifyTurn()){
                    mGamePresenter.onTrainDeckClick();
                }
            }
        });

        initializeFaceCardViews(v);

        //set deck counts
        destDeckCount.setText(Integer.toString(mGamePresenter.getDestDeckSize()));
        trainDeckCount.setText(Integer.toString(mGamePresenter.getTrainDeckSize()));


    }

    public void initializeFaceCardViews(View v) {
        // initialize faceUpCard View
        faceUpCardIVs = new ArrayList<>();
        faceUpCardIVs.add((ImageView) v.findViewById(R.id.face_up_train_one));
        faceUpCardIVs.add((ImageView) v.findViewById(R.id.face_up_train_two));
        faceUpCardIVs.add((ImageView) v.findViewById(R.id.face_up_train_three));
        faceUpCardIVs.add((ImageView) v.findViewById(R.id.face_up_train_four));
        faceUpCardIVs.add((ImageView) v.findViewById(R.id.face_up_train_five));
        setFaceCardImages(mGamePresenter.getFaceUpCards());

        for (int i = 0; i < faceUpCardIVs.size(); i++) {
            ImageView faceUpCard = faceUpCardIVs.get(i);
            final int INDEX = i;
            faceUpCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(mGamePresenter.verifyTurn()){
                        mGamePresenter.onFaceUpClick(INDEX);
                    }
                }
            });
        }
    }

    public void setFaceCardImages(TrainCard[] cards){

        for(int i = 0; i < cards.length; i++) {
            faceUpCardIVs.get(i).setImageResource(Util.getTrainCardDrawable(cards[i]));
        }
    }

}
