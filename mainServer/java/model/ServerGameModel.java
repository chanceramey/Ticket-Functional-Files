package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import command.Command;

/**
 * Created by tjense25 on 2/24/18.
 */

public class ServerGameModel {

    List<Player> players;

    TrainCardDeck trainCardDeck;
    TrainCard[] faceUpTrainCards;

    List<DestinationCard> destCardDeck;

    List<Command> gameHistoryCommands;

    public ServerGameModel(Game game) {
        initializePlayersList(game);
        trainCardDeck = new TrainCardDeck();
        faceUpTrainCards = trainCardDeck.drawCards(5);

    }

    private void initializePlayersList(Game game) {
        this.players = new ArrayList<>();
        Color[] colors = Color.values();
        int playerNumber = 0;
        for (String auth : game.getPlayerIDs().keySet()) {
            String user = null;
            try {
                user = ServerModel.getInstance().getUserFromAuth(auth);
            } catch (ServerModel.AuthTokenNotFoundException e) {
                e.printStackTrace();
            }
            Player player = new Player(user, colors[playerNumber], playerNumber);
            players.add(player);
            playerNumber++;
        }
    }

    public void startGame() {

    }
}
