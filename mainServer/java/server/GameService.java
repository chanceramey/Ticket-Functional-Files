package server;

import command.Command;
import communication.ClientProxy;
import model.Game;
import model.GameHistory;
import model.ServerGameModel;
import model.ServerModel;

/**
 * Created by Jeff on 2/28/2018.
 */

public class GameService extends AbstractService {


    public GameService() {
    }

    public Command[] sendMessage(String auth, String gameId, GameHistory historyObj) {
        try {
            String userName = mServerModel.getUserFromAuth(auth);
            historyObj.setUser(userName);
        } catch (ServerModel.AuthTokenNotFoundException e) {
            mClientProxy.promptRenewSession();
            Command[] commands = {mClientProxy.getCommand()};
            return commands;
        }
        try {
            ServerGameModel gameModel = mServerModel.getActiveGame(gameId); // implement this method in ServerGameModel

            gameModel.sendMessage(historyObj);
            return new Command[]{};

        } catch (ServerModel.GameNotFoundException e) {
            mClientProxy.promptRenewSession();
            Command[] commands = {mClientProxy.getCommand()};
            return commands;
        }
    }

    public Command[] claimRoute(String auth, String gameID, String routeID) {
        String user;
        try {
            user = mServerModel.getUserFromAuth(auth);
        } catch(ServerModel.AuthTokenNotFoundException e) {
            return promptRenewSession();
        }

        try {
            ServerGameModel gameModel = mServerModel.getActiveGame(gameID);

            gameModel.claimRoute(user, routeID);

        } catch (ServerModel.GameNotFoundException e) {
            return displayError("Could Not Find Game");
        }

        return new Command[]{};
    }
}
