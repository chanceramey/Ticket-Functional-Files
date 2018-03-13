package com.team.jcti.ttr.game;

import android.view.MenuItem;

import com.team.jcti.ttr.IGamePresenter;
import com.team.jcti.ttr.IPresenter;
import com.team.jcti.ttr.communication.ServerProxy;
import com.team.jcti.ttr.models.ClientGameModel;
import com.team.jcti.ttr.models.ClientModel;
import com.team.jcti.ttr.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import model.Color;
import model.DestinationCard;
import model.Game;
import model.Player;
import model.TrainCard;

/**
 * Created by Chance on 3/7/18.
 */

public class GamePresenter implements IGamePresenter, Observer{

    private ClientModel mClientModel = ClientModel.getInstance();
    private ServerProxy mServerProxy = ServerProxy.getInstance();
    private ClientGameModel mActiveGame = ClientGameModel.getInstance();
    private GameActivity mGameActivity;
    private PlayersHandFragment mPlayersHandFragment;
    private BoardFragment mBoardFragment;
    private DecksAndCardsFragment mDecksAndCardsFragment;
    private ArrayList<String> testCommands = new ArrayList<>();
    private int testNumber = 0;
    private boolean testing = false;


    public GamePresenter(GameActivity gameActivity){
        mActiveGame.setActivePresenter(this);
        this.mGameActivity = gameActivity;
        createTestCases();

    }

    public void createTestCases(){
        testCommands.add("Update player points");
        testCommands.add("Add/remove train cards for this player");
        testCommands.add("Add/remove player destination cards for this player");
        testCommands.add("Update number of train cards for other players");
        testCommands.add("Update the number of train cars for other players");
        testCommands.add("Update number of destination cards for other players");
        testCommands.add("Update visible cards and number of invisible cards in train card deck");
        testCommands.add("Update number of cards in destination card deck");
        testCommands.add("Add claimed route (for any player)");
        testCommands.add("Add chat message from any player");
        testCommands.add("Add game history entries");
    }

    public void claimRoute(String routeId) {
        mServerProxy.claimRoute(mClientModel.getAuthToken(), mClientModel.getGame().getID(), routeId);
    }

    @Override
    public void displayError(String message) {
        mGameActivity.toast(message);
    }

    @Override
    public void update() {
        mDecksAndCardsFragment.updateView();
        mPlayersHandFragment.updateCardList();
        mDecksAndCardsFragment.setFaceCardImages(mActiveGame.getFaceUpCards());
    }

    @Override
    public void drawDestCards() {
        mGameActivity.enterDrawDestinationActivity();
    }

    public void setPlayersHandFragment(PlayersHandFragment frag) {
        mPlayersHandFragment = frag;
    }

    public void setDecksAndCardsFragment(DecksAndCardsFragment frag) {
        mDecksAndCardsFragment = frag;
    }

    public void setBoardFragment(BoardFragment frag) { mBoardFragment = frag; }


    public List<TrainCard> getPlayerTrainCards() {
        return mActiveGame.getPlayersTrainCards();
    }

    public List<DestinationCard> getPlayerDestCards() {
        return mActiveGame.getPlayersDestCards();
    }

    public boolean isFirstTurn(){
        return mActiveGame.isFirstTurn();
    }

    public void onClaimRoute(Integer player, String routeID) {
        Color color = mActiveGame.getPlayers().get(player).getColor();
        mBoardFragment.claimRoute(routeID, color);
    }

    public boolean verifyTurn() {
        return (mActiveGame.isMyTurn());
    }

    //ike

    public TrainCard[] getFaceUpCards(){

        return mActiveGame.getFaceUpCards();
    }

    public void updateFaceUpCards(int[] pos, TrainCard[] faceUpCards){

        mActiveGame.updateFaceUpCards(pos, faceUpCards);
    }

    public int getDestDeckSize() {

        return mActiveGame.getDestDeckSize(); //checkback not sure best way to implement
    }

    public int getTrainDeckSize() {

        return 560; //checkback not sure best way to implement, thought Tanner might've had an idea
    }

    public void onDestDeckClick() {

       mServerProxy.drawDestinationCards(mClientModel.getAuthToken(), mActiveGame.getGameID());
    }

    public void onTrainDeckClick() {
        mServerProxy.drawTrainCards(mClientModel.getAuthToken(), 1, mClientModel.getGame().getID());

    }

    public void onFaceUpClick(int i) {
        mServerProxy.drawFaceUp(mClientModel.getAuthToken(), mActiveGame.getGameID(), i);
    }

    public void testRun(MenuItem item) {
        if (!testing && testNumber == 0) {
            testing = true;
            item.setTitle(testCommands.get(testNumber));

        } else {
            item.setTitle(testCommands.get(testNumber));
            mGameActivity.toast("Doing: " + testCommands.get(testNumber));
            doCommand(testCommands.get(testNumber));
            if (testNumber < testCommands.size() - 1 ) {
                testNumber++;
            } else {
                testNumber = 0;
                item.setTitle("Test");
                testing = false;
            }
        }

    }

    public void doCommand(String command) {

    }

    public void testButtonClick() {
        switch (mActiveGame.getTestIndex()) {
            case 0:

                break;
            case 1:
                displayError("Add train cards for this player");
                addTrainCards(); // add
                mActiveGame.incrementTestIndex();
                break;
            case 2:
                displayError("Remove train cards for this player");
                removeTrainCards(); // remove
                mActiveGame.incrementTestIndex();
                break;
            case 3:
                displayError("Add dest cards for this player");
                addDestCards(); //add
                mActiveGame.incrementTestIndex();
                break;
            case 4:
                displayError("Remove dest cards for this player");
                removeDestCards(); // remove
                mActiveGame.incrementTestIndex();
                break;
            case 5:
                displayError("Add train cards for other players");
                addTrainCardsOtherPlayer();
                mActiveGame.incrementTestIndex();
                break;
            /*case 6:
                displayError("Update trains for other players");
                //removeTrainsOtherPlayer(-10);
                mActiveGame.incrementTestIndex();
                break;
            case 7:
                displayError("Update dest cards for other players");
                //addDestCardsOtherPlayer(4);
                mActiveGame.incrementTestIndex();
                break;
            case 8:
                displayError("update face up and face down cards");
                updateFaceUpFaceDownCards();
                mActiveGame.incrementTestIndex();
                break;
            case 9:
                displayError("update dest card deck");
                updateDestCardDeck();
                mActiveGame.incrementTestIndex();
                break;*/
            default:
                break;
        }}

    private void updatePlayerPoints() {
        mActiveGame.getUserPlayer().setPoints(10);
    }
    private void addTrainCards() {
        TrainCard[] trainCards = { TrainCard.WILD, TrainCard.BLUE };
        int thisPlayer = mActiveGame.getUserPlayerInt();
        mActiveGame.drawTrainCards(thisPlayer, 2, trainCards);
    }

    private void addDestCards() {
        DestinationCard[] destinationCards = {new DestinationCard("Dallas", "New York City", 11), new DestinationCard("Los Angeles", "Miami", 20)};
        int thisPlayer = mActiveGame.getUserPlayerInt();
        mActiveGame.drawDestCards(thisPlayer, 2, destinationCards);
    }

    private void removeTrainCards() {
        mActiveGame.discardTrainCards(mActiveGame.getUserPlayerInt(), 2, new int[] {0,1});
    }

    private void removeDestCards() {
        int thisPlayer = mActiveGame.getUserPlayerInt();
        int[] cards = { 0, 1 };
        mActiveGame.discardDestCards(thisPlayer, 2, cards);
    }

    private void addTrainCardsOtherPlayer() {
        TrainCard[] trainCards = { TrainCard.WILD, TrainCard.BLUE };
        int thisPlayer = mActiveGame.getUserPlayerInt();
        if (thisPlayer > 1) {
            if (mActiveGame.getUserPlayerInt() == 0) {

                mActiveGame.drawTrainCards(1, 2, trainCards);
            } else {
                mActiveGame.drawTrainCards(thisPlayer - 1, 2, trainCards);

            }

        }
    }

    private void addDestCardsOtherPlayer() {
        DestinationCard[] cards = {new DestinationCard("Dallas", "New York City", 11), new DestinationCard("Los Angeles", "Miami", 20)};
        int thisPlayer = mActiveGame.getUserPlayerInt();
        if (thisPlayer > 1) {
            if (mActiveGame.getUserPlayerInt() == 0) {

                mActiveGame.drawDestCards(1, 2, cards);
            } else {
                mActiveGame.drawDestCards(thisPlayer - 1, 2, cards);

            }
        }
    }

    private void updateFaceUpFaceDownCards() {
        mActiveGame.swapFaceUpCards(new int[] {2,3}, new TrainCard[] {TrainCard.BLACK, TrainCard.BLUE});
    }

    private void updateDestCardDeck() {
        mActiveGame.removeDestCardsFromDeck(3);
    }

    @Override
    public void update(Observable observable, Object o) {
        update();
    }
}
