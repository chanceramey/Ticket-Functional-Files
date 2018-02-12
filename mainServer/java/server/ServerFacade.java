package server;

import interfaces.IServer;
import command.Command;

/**
 * Created by Isaak on 2/2/2018.
 */

public class ServerFacade implements IServer {


    @Override
    public Object login(String username, String password) {
        LoginService service = new LoginService();
        Command[] commands = service.login(username, password);
        return commands;
    }

    @Override
    public Object register(String username, String password, String firstName, String lastName) {
        RegisterService service = new RegisterService();
        Command[] commands = service.register(username, password, firstName, lastName);
        return commands;
    }

    @Override
    public Object createGame(int numPlayers, String gameName, String authToken) {
        CreateGameService service = new CreateGameService();
        Command[] commands = service.createGame(numPlayers, gameName, authToken);
        return commands;
    }

    @Override
    public Object joinGame(String authToken, String gameId) {
        GameListService service = new GameListService();
        Command[] commands = service.joinGame(authToken, gameId);
        return commands;
    }

    @Override
    public Object leaveGame(String authToken, String gameId) {
        GameListService service = new GameListService();
        Command[] commands = service.leaveGame(authToken, gameId);
        return commands;
    }

    @Override
    public Object getServerGames(String auth) {
        return new GameListService().getGamesList(auth);
    }

    @Override
    public Object getCommands(String authtoken) {
        GetCommandsService service = new GetCommandsService();
        Command[] commands = service.getCommands(authtoken);
        return commands;
    }
}
