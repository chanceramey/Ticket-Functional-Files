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

public class CardService extends AbstractService{

    private ServerModel mServerModel = ServerModel.getInstance();
    private ClientProxy clientProxy = new ClientProxy();


    public Command[] drawDestinationCards(String auth, String gameId){

        String username;
        try {
            username = mServerModel.getUserFromAuth(auth);
            ServerGameModel game = mServerModel.getActiveGame(gameId);

            if(game.drawDestCards(username)) {
                return new Command[] {};
            } else {
                return displayError("No destination cards left in deck");
            }

        } catch (ServerModel.AuthTokenNotFoundException | ServerModel.GameNotFoundException e) {
            return promptRenewSession();
        }
    }

    public Command[] drawTrainCard(String auth, Integer numberCards, String gameId){

        try {
            String username = mServerModel.getUserFromAuth(auth);
            ServerGameModel game = mServerModel.getActiveGame(gameId);

            if (game.drawTrainCards(username, numberCards)) {
                return new Command[] {};
            }
            else {
                return displayError("No cards left in deck");
            }

        } catch (ServerModel.AuthTokenNotFoundException | ServerModel.GameNotFoundException e) {
            clientProxy.displayError("501 SERVER ERROR");
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

            return new Command[] {};

        } catch (ServerModel.AuthTokenNotFoundException | ServerModel.GameNotFoundException e) {
            return promptRenewSession();
        }


    }

    public Object drawFaceUp(String auth, String gameID, Integer i) {

        String username;
        try {
            username = mServerModel.getUserFromAuth(auth);
            ServerGameModel game = mServerModel.getActiveGame(gameID);
            if (game.drawFaceUp(username, i)) {
                return new Command[] {};
            } else {
                return displayError("No Train Card in this position");
            }
        } catch (ServerModel.AuthTokenNotFoundException | ServerModel.GameNotFoundException e) {
            return promptRenewSession();
        }

    }
}









