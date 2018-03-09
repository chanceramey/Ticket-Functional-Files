package com.team.jcti.ttr.game;

import android.content.Context;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import model.TrainCard;

import static model.TrainCard.*;


public class DecksAndCardsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private List<ImageView> faceUpCards;
    private ImageView destinationDeck;
    private ImageView trainCardDeck;
    private TextView destDeckCount;
    private TextView trainDeckCount;

    //private IGamePresenter gamePresenter;

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

        //gamePresenter = new GamePresenter(this);

        trainCardDeck = v.findViewById(R.id.train_deck);
        destinationDeck = v.findViewById(R.id.destination_deck);
        destDeckCount = v.findViewById(R.id.dest_deck_count);
        trainDeckCount = v.findViewById(R.id.train_deck_count);

        faceUpCards = new ArrayList<>();
        faceUpCards.add((ImageView) v.findViewById(R.id.face_up_train_one));
        faceUpCards.add((ImageView) v.findViewById(R.id.face_up_train_two));
        faceUpCards.add((ImageView) v.findViewById(R.id.face_up_train_three));
        faceUpCards.add((ImageView) v.findViewById(R.id.face_up_train_four));
        faceUpCards.add((ImageView) v.findViewById(R.id.face_up_train_five));
        //setFaceUpCards(gamePresenter.getCards());

        //destDeckCount.setText(Integer.toString(gamePresenter.getDestDeckSize()));
        //trainDeckCount.setText(Integer.toString(gamePresenter.getTrainDeckSize()));

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setFaceUpCards(List<TrainCard> cards){

        for(int i = 0; i < 5; i++) {
            if(cards.size() > i) {
                switch (cards.get(i))
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
