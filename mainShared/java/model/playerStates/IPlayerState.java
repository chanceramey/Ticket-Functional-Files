package model.playerStates;

import model.DestinationCard;
import model.TrainCard;

/**
 * Created by tjense25 on 3/26/18.
 */

public abstract class IPlayerState {
    protected transient Player player;

    IPlayerState(Player player) {
        this.player = player;
    }

    abstract TrainCard[] claimRoute(String routeID, int[] pos);
    abstract boolean addFaceUpCard(TrainCard card);
    abstract boolean addTrainDeckCard(TrainCard card);
    abstract boolean addDestCards(DestinationCard[] cards);
    abstract boolean isTurn();
}
