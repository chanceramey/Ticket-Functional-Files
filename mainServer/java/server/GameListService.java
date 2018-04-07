package server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import command.Command;
import communication.ClientProxy;
import model.AuthToken;
import model.Game;
import model.ServerModel;
import model.User;

/**
 * Created by Jeff on 2/9/2018.
 */

public class GameListService {
    ClientProxy clientProxy = new ClientProxy();
    ServerModel mServerModel = ServerModel.getInstance();

    public Command[] getGamesList(String auth) {
        try {
            String username = mServerModel.getUserFromAuth(auth);
            User user = mServerModel.getUser(username);
            if (user.getGameID() != null) {
                clientProxy.promptRestoreGame(mServerModel.getActiveGame(user.getGameID()).convertToGame());
                return new Command[] {clientProxy.getCommand()};
            }
        } catch (ServerModel.AuthTokenNotFoundException | ServerModel.UserNotFoundException | ServerModel.GameNotFoundException e) {
            clientProxy.promptRenewSession();
            Command[] commands = {clientProxy.getCommand()};
            return commands;
        }
        mServerModel.addGameListClient(auth);
        Map<String, Game> gameMap = mServerModel.getWaitingGames();
        Game[] games = new Game[gameMap.size()];
        int index = 0;
        for (Game g : gameMap.values()) {
            games[index++] = g;
        }
        clientProxy.onGetServerGameList(games);
        return new Command[] {clientProxy.getCommand()};
    }

    public Command[] joinGame(String authToken, String gameId) {
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
        if (game.getPlayerIDs().size() == game.getNumPlayers()) {
            clientProxy.displayError("Game is full. Cannot join!");
            Command[] commands = {clientProxy.getCommand()};
            return commands;
        }

        //Player can Join Game!
        game.joinGame(username, authToken);
        mServerModel.removeGameListClient(authToken);
        clientProxy.updateGame(game);
        for (String auth : game.getPlayerIDs().keySet()) {
            if(auth.equals(authToken)) continue;
            mServerModel.addCommand(auth, clientProxy.getCommand());
        }
        for (String auth : mServerModel.getGameListClients()) {
            mServerModel.addCommand(auth, clientProxy.getCommand());
        }

        clientProxy.onJoinGame(game);
        Command[] commands = {clientProxy.getCommand()};
        return commands;
    }

    public Command[] rejectRestore(String authToken) {
        try {
            String username = mServerModel.getUserFromAuth(authToken);
            mServerModel.getUser(username).setGame(null);
        } catch (ServerModel.AuthTokenNotFoundException | ServerModel.UserNotFoundException e) {
            clientProxy.promptRenewSession();
            return new Command[] { clientProxy.getCommand() };
        }
        return getGamesList(authToken);
    }
}
