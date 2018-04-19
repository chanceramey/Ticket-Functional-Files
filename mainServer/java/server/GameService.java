package server;

import command.Command;
import communication.ClientProxy;
import model.FinalGamePoints;
import model.Game;
import model.GameHistory;
import model.ServerGameModel;
import model.ServerModel;
import model.User;

/**
 * Created by Jeff on 2/28/2018.
 */

public class GameService extends AbstractService {

    private final String TARGET = this.getClass().getName();

    public GameService() {
    }

    public Command[] sendMessage(String auth, String gameId, GameHistory historyObj, boolean addCommand) {
        try {
            String userName = mServerModel.getUserFromAuth(auth);
            historyObj.setUser(userName);
        } catch (ServerModel.AuthTokenNotFoundException e) {
            return promptRenewSession();
        }
        try {
            ServerGameModel gameModel = mServerModel.getActiveGame(gameId); // implement this method in ServerGameModel
            if (addCommand) {
                Command restoreCommand = Command.createCommand(TARGET, "sendMessage", auth, gameId, historyObj, false);
                mServerModel.addRestoreCommand(restoreCommand, gameId);
                gameModel.sendMessage(historyObj);
            }
            return new Command[]{};

        } catch (ServerModel.GameNotFoundException e) {
            return displayError("SERVER ERROR: Game not found");
        }
    }

    public Command[] claimRoute(String auth, String gameID, String routeID, int length, int[] cardPos, boolean addCommand) {
        String user;
        try {
            user = mServerModel.getUserFromAuth(auth);
        } catch(ServerModel.AuthTokenNotFoundException e) {
            return promptRenewSession();
        }

        try {
            ServerGameModel gameModel = mServerModel.getActiveGame(gameID);
            if (addCommand) {
                Command restoreCommand = Command.createCommand(TARGET, "claimRoute", auth, gameID, routeID, length, cardPos, false);
                mServerModel.addRestoreCommand(restoreCommand, gameID);
            }
            if (!gameModel.claimRoute(user, routeID, length, cardPos)) {
                return displayError("Invalid move");
            }

        } catch (ServerModel.GameNotFoundException e) {
            return displayError("Could Not Find Game");
        }

        return new Command[0];
    }

    public Command[] updatePlayerFinalPoints(String auth, String gameID, FinalGamePoints finalGamePoints, boolean addCommand) {
        User user;
        try {
            String username = mServerModel.getUserFromAuth(auth);
            user = mServerModel.getUser(username);
        } catch(ServerModel.AuthTokenNotFoundException | ServerModel.UserNotFoundException e) {
            return promptRenewSession();
        }

        try {
            ServerGameModel gameModel = mServerModel.getActiveGame(gameID);
            if (addCommand) {
                Command restoreCommand = Command.createCommand(TARGET, "updatePlayerFinalPoints", auth, gameID, finalGamePoints, false);
                mServerModel.addRestoreCommand(restoreCommand, gameID);
            }
            if(!gameModel.addToAllPlayersPoints(finalGamePoints)){
                return displayError("Already added to points");
            }

        } catch (ServerModel.GameNotFoundException e) {
            return displayError("Could Not Find Game");
        }
        user.setGame(null);
        return new Command[0];
    }
}
