package model.playerStates;

import model.DestinationCard;
import model.TrainCard;

/**
 * Created by tjense25 on 3/26/18.
 */

public class TurnState extends IPlayerState {

    public TurnState(Player player) {
        super(player);
    }

    @Override
    public TrainCard[] claimRoute(String routeID, int[] pos) {
        if(!player.addRoute(routeID, pos.length)) {
            return null;
        }
        TrainCard[] discardedTrainCards = player.removeTrainCards(pos);
        player.setState(new NotTurnState(player));
        return discardedTrainCards;
    }

    @Override
    public boolean addFaceUpCard(TrainCard card) {
        player.addTrainCard(card);
        if(card == TrainCard.WILD) player.setState(new NotTurnState(player));
        else player.setState(new OneTrainCardPickedState(player));
        return true;
    }

    @Override
    public boolean addTrainDeckCard(TrainCard card) {
        player.addTrainCard(card);
        player.setState(new OneTrainCardPickedState(player));
        return true;
    }

    @Override
    public boolean addDestCards(DestinationCard[] cards) {
        player.addDrawnDestCards(cards);
        player.setState(new NotTurnState(player));
        return true;
    }

    @Override
    public boolean isTurn() {
        return true;
    }
}
