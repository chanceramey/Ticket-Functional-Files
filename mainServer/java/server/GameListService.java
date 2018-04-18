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
import model.db.PersistenceFacade;

/**
 * Created by Jeff on 2/9/2018.
 */

public class GameListService {
    ClientProxy clientProxy = new ClientProxy();
    ServerModel mServerModel = ServerModel.getInstance();

    public Command[] getGamesList(String auth) {
        PersistenceFacade persistenceFacade = mServerModel.getPersistenceFacade();
        String username = persistenceFacade.getUsernameFromAuthToken(auth);
        User user = persistenceFacade.getUser(username);
        boolean validateAuthToken = (username != null && !username.equals("") );
        if (!validateAuthToken) {
            clientProxy.promptRenewSession();
            Command[] commands = {clientProxy.getCommand()};
            return commands;
        }

        try {
            if (user.getGameID() != null) {
                clientProxy.promptRestoreGame(mServerModel.getActiveGame(user.getGameID()).convertToGame());
                return new Command[]{clientProxy.getCommand()};
            }
        } catch (ServerModel.GameNotFoundException e) {
            e.printStackTrace();
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
        PersistenceFacade persistenceFacade = mServerModel.getPersistenceFacade();
        String username = persistenceFacade.getUsernameFromAuthToken(authToken);
        boolean validateAuthToken = (username != null && !username.equals("") );
        if (!validateAuthToken) {
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
        persistenceFacade.addGameIdToUser(username, gameId);
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
        PersistenceFacade persistenceFacade = mServerModel.getPersistenceFacade();
        String username = persistenceFacade.getUsernameFromAuthToken(authToken);
        boolean validateAuthToken = (username != null && !username.equals("") );
        if (!validateAuthToken) {
            clientProxy.promptRenewSession();
            Command[] commands = {clientProxy.getCommand()};
            return commands;
        }
        return getGamesList(authToken);
    }
}
