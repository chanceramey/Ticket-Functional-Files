package model.playerStates;

import model.DestinationCard;
import model.TrainCard;

/**
 * Created by tjense25 on 3/26/18.
 */

public class NotTurnState extends IPlayerState {


    public NotTurnState(Player player) {
        super(player);
    }

    @Override
    public TrainCard[] claimRoute(String routeID, int[] pos) {
        return null;
    }

    @Override
    public boolean addFaceUpCard(TrainCard card) {
        return false;
    }

    @Override
    public boolean addTrainDeckCard(TrainCard card) {
        return false;
    }

    @Override
    public boolean addDestCards(DestinationCard[] cards) {
        if (player.isFirstDestPick()) {
            player.addDrawnDestCards(cards);
            return true;
        }
        return false;
    }

    @Override
    public boolean isTurn() {
        return false;
    }
}
