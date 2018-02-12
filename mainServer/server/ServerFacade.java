package server;

import interfaces.IServer;
import command.Command;
import services.GameListService;

/**
 * Created by Isaak on 2/2/2018.
 */

public class ServerFacade implements IServer {


    @Override
    public Object login(String username, String password) {
        LoginService service = new LoginService();
        Command command = service.login(username, password);
        return command;
    }

    @Override
    public Object register(String username, String password, String firstName, String lastName) {
        RegisterService service = new RegisterService();
        Command command = service.register(username, password, firstName, lastName);
        return command;
    }

    @Override
    public Object createGame(int numPlayers, String gameName, String authToken) {
        CreateGameService service = new CreateGameService();
        Command command = service.createGame(numPlayers, gameName, authToken);
        return command;
    }

    @Override
    public Object joinGame(String userId, String gameId) {
        return new GameListService().joinGame(userId, gameId);
    }

    @Override
    public Object getServerGames() {
        return new GameListService().getGamesList();
    }
}
