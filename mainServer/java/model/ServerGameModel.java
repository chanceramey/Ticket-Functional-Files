package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import command.Command;
import communication.ClientProxy;

/**
 * Created by tjense25 on 2/24/18.
 */

public class ServerGameModel {

    private ClientProxy clientProxy = new ClientProxy();

    private List<Player> players;
    private Map<String, Integer> userIndexMap;
    private int currentPlayer;

    private TrainCardDeck trainCardDeck;
    private TrainCard[] faceUpTrainCards;

    private DestCardDeck destCardDeck;

    private List<Command> gameHistoryCommands;

    public ServerGameModel(Game game) {
        initializePlayersList(game);
        trainCardDeck = new TrainCardDeck();
        faceUpTrainCards = new TrainCard[5];
        for (int i = 0;  i < faceUpTrainCards.length; i++) {
            faceUpTrainCards[i] = null;
        }
        destCardDeck = new DestCardDeck();
        gameHistoryCommands = new ArrayList<>();
    }

    private void initializePlayersList(Game game) {
        this.players = new ArrayList<>();
        this.userIndexMap = new HashMap<>();
        Color[] colors = Color.values();
        int playerNumber = 0;
        for (String playerName : game.getPlayers()) {
            Player player = new Player(playerName, colors[playerNumber], playerNumber);
            players.add(player);
            userIndexMap.put(playerName, playerNumber);
            playerNumber++;
        }
    }

    public void startGame() {
        this.currentPlayer = 0; //set current player to be the first
        for (Player p : players) {
            //Draw 4 train cards from the deck
            TrainCard[] drawnTrainCards = trainCardDeck.drawCards(4);
            p.addTrainCards(drawnTrainCards);

            //Create command for player drawing 4 train cards and add to game history
            clientProxy.drawTrainCards(p.getId(), 4, drawnTrainCards);
            gameHistoryCommands.add(clientProxy.getCommand());

            //Draw 3 destination cards from deck
            DestinationCard[] drawnDestCards = destCardDeck.drawCards(3);
            p.addDestCards(drawnDestCards);

            //Create command for player drawing 3 train cards and add to game history
            clientProxy.drawDestCards(p.getId(), 3, drawnDestCards);
            gameHistoryCommands.add(clientProxy.getCommand());
        }

        //Draw 5 cards from TrainCard deck and update the faceup cards with the new cards
        TrainCard[] newCards = trainCardDeck.drawCards(5); //draw 5 cards to be face up cards
        int[] pos = new int[newCards.length];
        for (int i = 0; i < newCards.length; i++) {
            faceUpTrainCards[i] = newCards[i];
            pos[i] = i;
        }

        //add the swappingFaceUpTrainCards to the gameHistroyCommand list
        clientProxy.swapFaceUpCards(pos, faceUpTrainCards);
        gameHistoryCommands.add(clientProxy.getCommand());
    }

    public String[] getPlayers() {
        String[] playerStrings = new String[players.size()];
        for (int i = 0; i < playerStrings.length; i++) {
            playerStrings[i] = players.get(i).getUser();
        }
        return playerStrings;
    }

    public Color[] getPlayerColors() {
        Color[] playerColors = new Color[players.size()];
        for (int i = 0; i < playerColors.length; i++) {
            playerColors[i] = players.get(i).getColor();
        }
        return playerColors;
    }
}