package server;

import command.Command;
import communication.ClientProxy;
import model.ServerGameModel;
import model.ServerModel;

/**
 * Created by Isaak on 3/7/2018.
 */

public class CardService extends AbstractService{
    private final String TARGET = this.getClass().getName();

    public Command[] drawDestinationCards(String auth, String gameId, boolean addCommand){

        String username;
        try {
            username = mServerModel.getUserFromAuth(auth);
            ServerGameModel game = mServerModel.getActiveGame(gameId);
            if(addCommand) {
                Command restoreCommand = Command.createCommand(TARGET, "drawDestCards", auth, gameId, false);
                mServerModel.addRestoreCommand(restoreCommand, gameId);
            }
            if(game.drawDestCards(username)) {
                return new Command[] {};
            } else {
                return displayError("Invalid action");
            }

        } catch (ServerModel.AuthTokenNotFoundException | ServerModel.GameNotFoundException e) {
            return promptRenewSession();
        }
    }

    public Command[] drawTrainCard(String auth, Integer numberCards, String gameId, boolean addCommand){

        try {
            String username = mServerModel.getUserFromAuth(auth);
            ServerGameModel game = mServerModel.getActiveGame(gameId);
            if (addCommand) {
                Command restoreCommand = Command.createCommand(TARGET, "drawDeckTrainCard", auth, numberCards, gameId, false);
                mServerModel.addRestoreCommand(restoreCommand, gameId);
            }
            if (game.drawDeckTrainCard(username)) {
                return new Command[] {};
            }
            else {
                return displayError("Invalid action");
            }

        } catch (ServerModel.AuthTokenNotFoundException | ServerModel.GameNotFoundException e) {
            return displayError("SERVER ERROR");
        }
    }

    public Command[] returnDestinationCards(String auth, String gameId, int[] rejectedCardPositions, boolean addCommand){

        String username;
        try {
            username = mServerModel.getUserFromAuth(auth);
            ServerGameModel game = mServerModel.getActiveGame(gameId);
            if (addCommand) {
                Command restoreCommand = Command.createCommand(TARGET, "returnDestinationCards", auth, gameId, rejectedCardPositions, false);
                mServerModel.addRestoreCommand(restoreCommand, gameId);
            }
            game.returnDestinationCards(username, rejectedCardPositions);

            return new Command[] {};

        } catch (ServerModel.AuthTokenNotFoundException | ServerModel.GameNotFoundException e) {
            return promptRenewSession();
        }


    }

    public Object drawFaceUp(String auth, String gameID, Integer i, boolean addCommand) {

        String username;
        try {
            username = mServerModel.getUserFromAuth(auth);
            ServerGameModel game = mServerModel.getActiveGame(gameID);
            if (addCommand) {
                Command restoreCommand = Command.createCommand(TARGET, "drawFaceUp", auth, gameID, i, false);
                mServerModel.addRestoreCommand(restoreCommand, gameID);
            }
            if (game.drawFaceUp(username, i)) {
                return new Command[] {};
            } else {
                return displayError("Invalid action");
            }
        } catch (ServerModel.AuthTokenNotFoundException | ServerModel.GameNotFoundException e) {
            return promptRenewSession();
        }

    }
}









