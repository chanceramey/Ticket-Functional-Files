package server;

import java.util.List;

import command.Command;
import communication.ClientProxy;
import model.DestCardDeck;
import model.DestinationCard;
import model.Game;
import model.Player;
import model.ServerGameModel;
import model.ServerModel;
import model.TrainCard;
import model.TrainCardDeck;

/**
 * Created by Isaak on 3/7/2018.
 */

public class CardService {

    private ServerModel mServerModel = ServerModel.getInstance();
    private ClientProxy clientProxy = new ClientProxy();


    public Command[] drawDestinationCards(String auth, String gameId){

        String username;
        try {
            username = mServerModel.getUserFromAuth(auth);
            ServerGameModel game = mServerModel.getActiveGame(gameId);

            boolean success = game.drawDestCards(username);

            if(!success){
                clientProxy.displayError("No destination cards left.");
                return new Command[] {clientProxy.getCommand()};
            }

            return new Command[] {};


        } catch (ServerModel.AuthTokenNotFoundException | ServerModel.GameNotFoundException e) {
            clientProxy.promptRenewSession();
            Command[] commands = {clientProxy.getCommand()};
            return commands;
        }
    }

    public Command[] drawTrainCard(String auth, Integer numberCards, String gameId){

        String username;
        try {
            username = mServerModel.getUserFromAuth(auth);
            ServerGameModel game = mServerModel.getActiveGame(gameId);

            TrainCardDeck trainCardDeck = game.getTrainCardDeck();
            Player player = game.getPlayerFromUsername(username); //active player? checkback

            TrainCard[] drawnTrainCards = trainCardDeck.drawCards(numberCards);

            if(drawnTrainCards == null){
                clientProxy.displayError("No train cards left.");
                Command[] commands = {clientProxy.getCommand()};
                return commands;
            }

            player.addTrainCards(drawnTrainCards);

            clientProxy.drawTrainCards(player.getId(), drawnTrainCards.length, drawnTrainCards);
            Command[] commands = {clientProxy.getCommand()};
            return commands;


        } catch (ServerModel.AuthTokenNotFoundException | ServerModel.GameNotFoundException e) {
            clientProxy.promptRenewSession();
            Command[] commands = {clientProxy.getCommand()};
            return commands;
        }
    }

    public Command[] returnDestinationCards(String auth, String gameId, int[] rejectedCardPositions){

        String username;
        try {
            username = mServerModel.getUserFromAuth(auth);

            ServerGameModel game = mServerModel.getActiveGame(gameId);
            game.returnDestinationCards(username, rejectedCardPositions);

            DestCardDeck destCardDeck = game.getDestCardDeck();
            Player player = game.getPlayerFromUsername(username);

            return new Command[] {};

        } catch (ServerModel.AuthTokenNotFoundException | ServerModel.GameNotFoundException e) {
            clientProxy.promptRenewSession();
            Command[] commands = {clientProxy.getCommand()};
            return commands;
        }


    }

    public Object drawFaceUp(String auth, String gameID, Integer i) {

        String username;
        try {
            username = mServerModel.getUserFromAuth(auth);
            ServerGameModel game = mServerModel.getActiveGame(gameID);
            boolean success = game.drawFaceUp(username, i);

            if(!success){
                clientProxy.displayError("No Train Card in this position");
                return new Command[] {clientProxy.getCommand()};

            }
            return new Command[] {};


        } catch (ServerModel.AuthTokenNotFoundException | ServerModel.GameNotFoundException e) {
            clientProxy.promptRenewSession();
            Command[] commands = {clientProxy.getCommand()};
            return commands;
        }

    }
}









