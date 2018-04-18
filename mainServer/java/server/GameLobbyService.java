package server;

import java.util.UUID;

import communication.ClientProxy;
import model.Color;
import model.ServerGameModel;
import model.ServerModel;
import command.Command;
import model.Game;
import model.User;
import model.db.PersistenceFacade;

/**
 * Created by tjense25 on 2/2/18.
 */

public class GameLobbyService {

    private ServerModel mServerModel = ServerModel.getInstance();
    private ClientProxy clientProxy = new ClientProxy();

    public Command[] createGame(int numPlayers, String gameName, String authToken) {

        PersistenceFacade persistenceFacade = mServerModel.getPersistenceFacade();
        String username = persistenceFacade.getUsernameFromAuthToken(authToken);
        boolean validateAuthToken = (username != null && !username.equals("") );
        if (!validateAuthToken) {
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
        persistenceFacade.saveGameState(game);
        persistenceFacade.addGameIdToUser(username, game.getID());
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

        PersistenceFacade persistenceFacade = mServerModel.getPersistenceFacade();
        persistenceFacade.deleteGame(game.getID());
        String username = persistenceFacade.getUsernameFromAuthToken(authtoken);
        persistenceFacade.addGameIdToUser(username, "");

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
        PersistenceFacade persistenceFacade = mServerModel.getPersistenceFacade();
        String username = persistenceFacade.getUsernameFromAuthToken(auth);
        User user = persistenceFacade.getUser(username);
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
            clientProxy.displayError("Error: Could not find game.");
            return new Command[] {clientProxy.getCommand()};
        }
        user.setGame(gameId);
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
