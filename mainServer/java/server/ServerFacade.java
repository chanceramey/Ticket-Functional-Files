package server;

import interfaces.IServer;
import command.Command;
import model.GameHistory;

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
        GameLobbyService service = new GameLobbyService();
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
        GameLobbyService service = new GameLobbyService();
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

    @Override
    public Object startGame(String auth, String gameId) {
        GameLobbyService service = new GameLobbyService();
         return service.startGame(auth, gameId);
    }

    @Override
    public Object sendMessage(String auth, String gameId, GameHistory historyObj) {
        GameService service = new GameService();
        return service.sendMessage(auth, gameId, historyObj);
    }

    @Override
    public Object getGameCommands(String auth, String gameID, Integer gameHistoryPosition) {
        GetCommandsService service = new GetCommandsService();
        return service.getGameCommands(auth, gameID, gameHistoryPosition);
    }

    @Override
    public Object claimRoute(String auth, String gameID, String routeID) {
        GameService service = new GameService();
        return service.claimRoute(auth, gameID, routeID);
    }

    @Override
    public Object drawDestinationCards(String auth, String gameId) {
        DrawDestCardsService service = new DrawDestCardsService();
        return service.drawDestinationCards(auth, gameId);
    }

    @Override
    public Object returnDestinationCards(String auth, String gameId, int[] rejectedCardPositions) {
        ReturnDestCardsService service = new ReturnDestCardsService();
        return service.returnDestinationCards(auth, gameId, rejectedCardPositions);
    }
}
