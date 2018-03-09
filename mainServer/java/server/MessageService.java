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

public class MessageService {
    ServerModel mServerModel = ServerModel.getInstance();
    ClientProxy clientProxy = new ClientProxy();
    public MessageService() {
    }

    public Command[] sendMessage(String auth, String gameId, GameHistory historyObj) {
        try {
            String userName = mServerModel.getUserFromAuth(auth);
            historyObj.setUser(userName);
        } catch (ServerModel.AuthTokenNotFoundException e) {
            clientProxy.promptRenewSession();
            Command[] commands = {clientProxy.getCommand()};
            return commands;
        }
        try {
            ServerGameModel gameModel = mServerModel.getActiveGame(gameId); // implement this method in ServerGameModel

            gameModel.sendMessage(historyObj);
            return new Command[]{};

        } catch (ServerModel.GameNotFoundException e) {
            clientProxy.promptRenewSession();
            Command[] commands = {clientProxy.getCommand()};
            return commands;
        }
    }
}
