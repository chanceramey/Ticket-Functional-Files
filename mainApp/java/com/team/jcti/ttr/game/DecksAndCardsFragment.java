package com.team.jcti.ttr.game;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.team.jcti.ttr.R;
import com.team.jcti.ttr.drawdestinationcard.DrawDestinationCardActivity;
import com.team.jcti.ttr.gamelist.GameListActivity;
import com.team.jcti.ttr.models.ClientModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import model.TrainCard;

import static model.TrainCard.*;


public class DecksAndCardsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private List<ImageView> faceUpCards;
    private ImageView destinationCardDeck;
    private ImageView trainCardDeck;
    private TextView destDeckCount;
    private TextView trainDeckCount;



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


        trainCardDeck = v.findViewById(R.id.train_deck);
        destinationCardDeck = v.findViewById(R.id.destination_deck);
        destDeckCount = v.findViewById(R.id.dest_deck_count);
        trainDeckCount = v.findViewById(R.id.train_deck_count);

        faceUpCards = new ArrayList<>();
        faceUpCards.add((ImageView) v.findViewById(R.id.face_up_train_one));
        faceUpCards.add((ImageView) v.findViewById(R.id.face_up_train_two));
        faceUpCards.add((ImageView) v.findViewById(R.id.face_up_train_three));
        faceUpCards.add((ImageView) v.findViewById(R.id.face_up_train_four));
        faceUpCards.add((ImageView) v.findViewById(R.id.face_up_train_five));
        setFaceUpCards(((GameActivity)getActivity()).getGamePresenter().getFaceUpCards());

        destDeckCount.setText(Integer.toString(((GameActivity)getActivity()).getGamePresenter().getDestDeckSize()));
        trainDeckCount.setText(Integer.toString(((GameActivity)getActivity()).getGamePresenter().getTrainDeckSize()));

        destinationCardDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(((GameActivity)getActivity()).getGamePresenter().verifyTurn()){
                    ((GameActivity)getActivity()).getGamePresenter().onDestDeckClick();
                }
            }
        });

        trainCardDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(((GameActivity)getActivity()).getGamePresenter().verifyTurn()){
                    ((GameActivity)getActivity()).getGamePresenter().onTrainDeckClick();
                }
            }
        });

        faceUpCards.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(((GameActivity)getActivity()).getGamePresenter().verifyTurn()){

                }
                ((GameActivity)getActivity()).getGamePresenter().onFaceUpClick(0);
            }
        });

        faceUpCards.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(((GameActivity)getActivity()).getGamePresenter().verifyTurn()){
                    ((GameActivity)getActivity()).getGamePresenter().onFaceUpClick(1);
                }
            }
        });

        faceUpCards.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((GameActivity)getActivity()).getGamePresenter().verifyTurn()){
                    ((GameActivity)getActivity()).getGamePresenter().onFaceUpClick(2);
                }
            }
        });

        faceUpCards.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((GameActivity)getActivity()).getGamePresenter().verifyTurn()){
                    ((GameActivity)getActivity()).getGamePresenter().onFaceUpClick(3);
                }
            }
        });

        faceUpCards.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((GameActivity)getActivity()).getGamePresenter().verifyTurn()){
                    ((GameActivity)getActivity()).getGamePresenter().onFaceUpClick(4);
                }
            }
        });

        return v;
    }

    public void setFaceUpCards(TrainCard[] cards){

        for(int i = 0; i < 5; i++) {
            if(cards[i] != null) { //checkback too tired to think if this will work in the future lol
                switch (cards[i])
                {
                    case RED:
                        faceUpCards.get(i).setImageResource(R.drawable.redtrain);
                        break;
                    case BLUE:
                        faceUpCards.get(i).setImageResource(R.drawable.bluetrain);
                        break;
                    case GREEN:
                        faceUpCards.get(i).setImageResource(R.drawable.greentrain);
                        break;
                    case BLACK:
                        faceUpCards.get(i).setImageResource(R.drawable.blacktrain);
                        break;
                    case ORANGE:
                        faceUpCards.get(i).setImageResource(R.drawable.orangetrain);
                        break;
                    case YELLOW:
                        faceUpCards.get(i).setImageResource(R.drawable.yellowtrain);
                        break;
                    case WHITE:
                        faceUpCards.get(i).setImageResource(R.drawable.whitetrain);
                        break;
                    case PURPLE:
                        faceUpCards.get(i).setImageResource(R.drawable.purpletrain);
                        break;
                    case WILD:
                        faceUpCards.get(i).setImageResource(R.drawable.rainbowtrain);
                        break;
                    default:
                        throw new RuntimeException("ruh roh");
                }
            }
            else {
                faceUpCards.get(i).setImageResource(android.R.color.transparent);
            }
        }
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
