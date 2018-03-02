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
            String user = mServerModel.getUserFromAuth(auth);
            historyObj.setUser(user);
        } catch (ServerModel.AuthTokenNotFoundException e) {
            clientProxy.promptRenewSession();
            Command[] commands = {clientProxy.getCommand()};
            return commands;
        }
        try {
            ServerGameModel gameModel = mServerModel.getActiveGame(gameId); // implement this method in ServerGameModel
            Game game = mServerModel.getGame(gameId);

            gameModel.addGameHistory(historyObj);
            game.addGameHistory(historyObj);
            clientProxy.updateGame(game);
            // add command to list of commands in ServerGameModel
            Command[] commands = {clientProxy.getCommand()};
            return commands;

        } catch (ServerModel.GameNotFoundException e) {
            clientProxy.promptRenewSession();
            Command[] commands = {clientProxy.getCommand()};
            return commands;
        }
    }
}
