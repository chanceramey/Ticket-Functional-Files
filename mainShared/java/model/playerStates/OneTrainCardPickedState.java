package model.playerStates;

import model.DestinationCard;
import model.TrainCard;

/**
 * Created by tjense25 on 3/26/18.
 */

public class OneTrainCardPickedState extends IPlayerState {
    public OneTrainCardPickedState(Player player) {
        super(player);
    }

    @Override
    public TrainCard[] claimRoute(String routeID, int[] pos) {
        return null;
    }

    @Override
    public boolean addFaceUpCard(TrainCard card) {
        if(card == TrainCard.WILD) return false;
        player.addTrainCard(card);
        player.setState(new NotTurnState(player));
        return true;
    }

    @Override
    public boolean addTrainDeckCard(TrainCard card) {
        player.addTrainCard(card);
        player.setState(new NotTurnState(player));
        return true;
    }

    @Override
    public boolean addDestCards(DestinationCard[] cards) {
        return false;
    }

    @Override
    public boolean isTurn() {
        return true;
    }
}
