package server;

import interfaces.IServer;
import command.Command;

/**
 * Created by Isaak on 2/2/2018.
 */

public class ServerFacade implements IServer {


    @Override
    public Object login(String username, String password) {
        return null;
    }

    @Override
    public Object register(String username, String password, String firstName, String lastName) {
        return null;
    }

    @Override
    public Object createGame(int numPlayers, String gameName, String authToken) {
        CreateGameService service = new CreateGameService();
        Command command = service.createGame(numPlayers, gameName, authToken);
        return command;
    }

    @Override
    public Object joinGame(String username, String gameId) {
        return null;
    }

    @Override
    public Object getServerGames() {
        return null;
    }
}
