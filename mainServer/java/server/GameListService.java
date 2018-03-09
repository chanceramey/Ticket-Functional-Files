package server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import command.Command;
import communication.ClientProxy;
import model.AuthToken;
import model.Game;
import model.ServerModel;

/**
 * Created by Jeff on 2/9/2018.
 */

public class GameListService {
    ClientProxy clientProxy = new ClientProxy();
    ServerModel mServerModel = ServerModel.getInstance();

    public Command[] getGamesList(String auth) {
        try {
            ServerModel.getInstance().getUserFromAuth(auth);
        } catch (ServerModel.AuthTokenNotFoundException e) {
            clientProxy.promptRenewSession();
            Command[] commands = {clientProxy.getCommand()};
            return commands;
        }
        mServerModel.addGameListClient(auth);
        Map<String, Game> gameMap = ServerModel.getInstance().getWaitingGames();
        Game[] games = new Game[gameMap.size()];
        int index = 0;
        for (Game g : gameMap.values()) {
            games[index] = g;
            index++;
        }
        clientProxy.onGetServerGameList(games);
        Command[] commands = {clientProxy.getCommand()};
        return commands;
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

}
