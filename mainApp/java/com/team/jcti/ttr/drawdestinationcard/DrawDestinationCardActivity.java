package com.team.jcti.ttr.drawdestinationcard;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.team.jcti.ttr.R;
import com.team.jcti.ttr.finalScreen.FinalScreenActivity;
import com.team.jcti.ttr.game.GameActivity;

import java.util.ArrayList;
import java.util.List;

import model.DestinationCard;

public class DrawDestinationCardActivity extends AppCompatActivity implements IDrawDestinationCardActivity{

    private IDrawDestinationCardPresenter drawDestinationCardPresenter;
    private Button submitButton;
    private List<Integer> chosenCards;
    private List<TextView> cardImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_destination_card);

        drawDestinationCardPresenter = new DrawDestinationCardPresenter(this);

        chosenCards = new ArrayList<>();
        cardImages = new ArrayList<>();

        cardImages.add((TextView) findViewById(R.id.destination_option_one));
        cardImages.add((TextView) findViewById(R.id.destination_option_two));
        cardImages.add((TextView) findViewById(R.id.destination_option_three));
        setImageViews();

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
            }
        });

        setImageViews();

    }

    public void enterGameActivity() {
        finish();
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
        if (drawDestinationCardPresenter.getCards().get(imageNum) == null) return;
        TextView selectedCard = cardImages.get(imageNum);

        if(!chosenCards.contains(imageNum)) {
            selectedCard.setBackgroundColor(Color.MAGENTA);
        }
        else {
            selectedCard.setBackgroundColor(Color.argb(100,0,0,255));
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


    @Override
    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void setImageViews() {

        List<DestinationCard> destDeck = drawDestinationCardPresenter.getCards();
        for(int i = 0; i < destDeck.size(); i++){

            if(drawDestinationCardPresenter.getCards().get(i) == null){
                cardImages.get(i).setBackgroundColor(Color.TRANSPARENT);
                cardImages.get(i).setText("");
            }
            else{
                cardImages.get(i).setText(destDeck.get(i).toString());
                cardImages.get(i).setBackgroundColor(Color.argb(100,0,0,255));
            }
        }
    }

    public void update() {
        setImageViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        drawDestinationCardPresenter.update();
    }

    @Override
    public void enterFinalScreen() {
        Intent intent = new Intent(this, FinalScreenActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Must make destination card selection", Toast.LENGTH_SHORT).show();
    }
}
