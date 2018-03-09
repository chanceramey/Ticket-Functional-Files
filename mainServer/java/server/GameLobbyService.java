package server;

import java.util.UUID;

import communication.ClientProxy;
import model.Color;
import model.ServerGameModel;
import model.ServerModel;
import command.Command;
import model.Game;

/**
 * Created by tjense25 on 2/2/18.
 */

public class GameLobbyService {

    private ServerModel mServerModel = ServerModel.getInstance();
    private ClientProxy clientProxy = new ClientProxy();

    public Command[] createGame(int numPlayers, String gameName, String authToken) {
        String username;
        try {
            username = mServerModel.getUserFromAuth(authToken);
        } catch (ServerModel.AuthTokenNotFoundException e) {
            clientProxy.promptRenewSession();
            Command[] commands = {clientProxy.getCommand()};
            return commands;
        }

        if (mServerModel.hasGameName(gameName)) {
            clientProxy.displayError("Game name already in use");
            Command[] commands = {clientProxy.getCommand()};
            return commands;
        }

        Game game = new Game(numPlayers, username, gameName, UUID.randomUUID().toString(), authToken);
        mServerModel.addWaitingGame(game);
        mServerModel.removeGameListClient(authToken);
        clientProxy.addGametoList(game);

        for (String auth : mServerModel.getGameListClients()) {
            mServerModel.addCommand(auth, clientProxy.getCommand());

        }
        clientProxy.onCreateGame(game);
        Command[] commands = {clientProxy.getCommand()};
        return commands;
    }

    public Command[] leaveGame(String authToken, String gameId) {
        String username;
        try {
            username = mServerModel.getUserFromAuth(authToken);
        } catch (ServerModel.AuthTokenNotFoundException e) {
            clientProxy.promptRenewSession();
            Command[] commands = {clientProxy.getCommand()};
            return commands;
        }

        Game game;
        try {
            game = mServerModel.getGame(gameId);
        } catch (ServerModel.GameNotFoundException e) {
            clientProxy.displayError("Game not found. Please try again.");
            Command[] commands = {clientProxy.getCommand()};
            return commands;

        }
        if (game.getHost().equals(authToken)) return removeGame(game, authToken);

        //Player can Leave Game!
        game.getPlayerIDs().remove(authToken);
        game.getPlayers().remove(username);
        clientProxy.updateGame(game);
        for (String auth : game.getPlayerIDs().keySet()) {
            mServerModel.addCommand(auth, clientProxy.getCommand());
        }
        for (String auth : mServerModel.getGameListClients()) {
            mServerModel.addCommand(auth, clientProxy.getCommand());
        }

        clientProxy.onLeaveGame();
        Command[] commands = {clientProxy.getCommand()};
        return commands;
    }

    private Command[] removeGame(Game game, String authtoken) {
        try {
            mServerModel.deleteGame(game.getID());
        } catch (ServerModel.GameNotFoundException e) {
            clientProxy.displayError("Error: Could not find game.");
            return new Command[] {clientProxy.getCommand()};
        }

        clientProxy.removeGameFromList(game.getID());
        for (String auth : mServerModel.getGameListClients()) {
            mServerModel.addCommand(auth, clientProxy.getCommand());
        }

        clientProxy.onLeaveGame();
        for (String auth : game.getPlayerIDs().keySet()) {
            if(auth.equals(authtoken)) continue;
            mServerModel.addCommand(auth, clientProxy.getCommand());
        }
        return new Command[] {clientProxy.getCommand()};

    }

    public Command[] startGame(String auth, String gameId) {

        try {
            mServerModel.getUserFromAuth(auth);
        } catch (ServerModel.AuthTokenNotFoundException e) {
            clientProxy.promptRenewSession();
            return new Command[] {clientProxy.getCommand()};
        }

        Game game;
        try {
            game = mServerModel.getGame(gameId);
        } catch (ServerModel.GameNotFoundException e) {
            clientProxy.displayError("Error: Could not find game.");
            return new Command[] {clientProxy.getCommand()};
        }

        mServerModel.startGame(gameId);

        clientProxy.removeGameFromList(gameId);
        for (String client : mServerModel.getGameListClients()) {
            mServerModel.addCommand(client, clientProxy.getCommand());
        }

        clientProxy.onGameStarted();
        for (String client : game.getPlayerIDs().keySet()) {
            if (client.equals(auth)) continue;
            mServerModel.addCommand(client, clientProxy.getCommand());
        }

        return new Command[] {clientProxy.getCommand()};
    }


}
