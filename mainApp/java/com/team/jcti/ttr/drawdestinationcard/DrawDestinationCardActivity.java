package com.team.jcti.ttr.drawdestinationcard;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.team.jcti.ttr.R;
import com.team.jcti.ttr.game.GameActivity;

import java.util.ArrayList;
import java.util.List;

import model.DestinationCard;

public class DrawDestinationCardActivity extends AppCompatActivity implements IDrawDestinationCardActivity{

    private IDrawDestinationCardPresenter drawDestinationCardPresenter;
    private Button submitButton;
    private List<Integer> chosenCards;
    private List<ImageView> cardImages;
    private List<TextView> cardTexts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_destination_card);

        drawDestinationCardPresenter = new DrawDestinationCardPresenter(this);

        chosenCards = new ArrayList<>();
        cardImages = new ArrayList<>();
        cardTexts = new ArrayList<>();

        cardImages.add((ImageView) findViewById(R.id.destination_option_one));
        cardImages.add((ImageView) findViewById(R.id.destination_option_two));
        cardImages.add((ImageView) findViewById(R.id.destination_option_three));
        setImageViews();

        cardTexts.add((TextView) findViewById(R.id.destination_text_one));
        cardTexts.add((TextView) findViewById(R.id.destination_text_two));
        cardTexts.add((TextView) findViewById(R.id.destination_text_three));
        setTextViews();

        submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setEnabled(false);

        cardImages.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleImageClick(0);
            }
        });
        cardImages.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleImageClick(1);
            }
        });
        cardImages.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleImageClick(2);
            }
        });

        //submit click listener
        submitButton.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                drawDestinationCardPresenter.returnRejectedDestCards(getRejectedDestCards());

                enterGameActivity();
            }
        });

        setTextViews();
        setImageViews();

    }

    public void enterGameActivity() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    private List<Integer> getRejectedDestCards() {

        List<Integer> rejectedCards = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            if(!chosenCards.contains(i)){
                rejectedCards.add(i);
            }
        }
        return rejectedCards;
    }

    private void handleImageClick(int imageNum) {

        ImageView selectedImage = cardImages.get(imageNum);

        if(!chosenCards.contains(imageNum)) {
            selectedImage.setBackgroundColor(Color.argb(100,0,0,255));
        }
        else {
            selectedImage.setBackgroundColor(Color.argb(0,0,0,0));
        }



        if(chosenCards.contains(imageNum)) {
            chosenCards.remove(chosenCards.indexOf(imageNum));
        }
        else {
            chosenCards.add(imageNum);
        }

        // must have 2 selected at game start or 1 selected otherwise to submit, else the button is disabled
        if((drawDestinationCardPresenter.isGameStart() && chosenCards.size() >= 2) ||
                (!drawDestinationCardPresenter.isGameStart() && chosenCards.size() >= 1)){
            submitButton.setEnabled(true);
        }
        else{
            submitButton.setEnabled(false);
        }
    }

    private void setTextViews() {

        List<DestinationCard> destDeck = drawDestinationCardPresenter.getCards();
        for(int i = 0; i < 3; i++){

            if(destDeck.size() > i){

                cardTexts.get(i).setText(destDeck.get(i).getSrcCity() + " to " + destDeck.get(i).getDestCity());
            }
        }
    }

    private void setImageViews() {

        for(int i = 0; i < 3; i++){

            if(drawDestinationCardPresenter.getCards().size() > i){
                cardImages.get(i).setImageResource(R.drawable.tempdestcard);
            }
            else{
                cardImages.get(i).setImageResource(android.R.color.transparent);
            }
        }
    }

    public void update() {
        setImageViews();
        setTextViews();
    }
}
