package server;

import java.util.UUID;

import communication.ClientProxy;
import model.ServerModel;
import command.Command;
import model.Game;

/**
 * Created by tjense25 on 2/2/18.
 */

public class CreateGameService {

    private ServerModel serverModel = ServerModel.getInstance();
    private ClientProxy clientProxy = new ClientProxy();

    public Command[] createGame(int numPlayers, String gameName, String authToken) {
        String username = null;
        try {
            username = serverModel.getUserFromAuth(authToken);
        } catch (ServerModel.AuthTokenNotFoundException e) {
            clientProxy.promptRenewSession();
            Command[] commands = {clientProxy.getCommand()};
            return commands;
        }
        if (serverModel.hasGameName(gameName)) {
            clientProxy.displayError("Game name already in use");
            Command[] commands = {clientProxy.getCommand()};
            return commands;
        }
        Game game = new Game(numPlayers, username, gameName, UUID.randomUUID().toString(), authToken);
        serverModel.addWaitingGame(game);
        serverModel.removeGameListClient(authToken);
        clientProxy.addGametoList(game);
        for (String auth : serverModel.getGameListClients()) {
            serverModel.addCommand(auth, clientProxy.getCommand());
        }
        clientProxy.onCreateGame(game);
        Command[] commands = {clientProxy.getCommand()};
        return commands;
    }
}
