package model;

import java.util.List;
import java.util.Map;

import command.Command;

/**
 * Created by tjense25 on 2/24/18.
 */

public class ServerGameModel {

    Map<String, Player> players;

    TrainCardDeck trainCardDeck;
    List<TrainCard> faceUpTrainCards;

    List<DestinationCard> destCardDeck;

    List<Command> gameHistoryCommands;

    public ServerGameModel(Game game) {

    }

    public void startGame() {

    }
}
