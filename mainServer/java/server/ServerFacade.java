package server;

import interfaces.IServer;
import command.Command;
import model.FinalGamePoints;
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
    public Object claimRoute(String auth, String gameId, String routeId, Integer length, int[] cardPos) {
        GameService service = new GameService();
        return service.claimRoute(auth, gameId, routeId, length, cardPos);
    }

    @Override
    public Object drawDestinationCards(String auth, String gameId) {
        CardService service = new CardService();
        return service.drawDestinationCards(auth, gameId);
    }

    @Override
    public Object drawTrainCards(String auth, Integer numberCards, String gameId) {
        CardService service = new CardService();
        return service.drawTrainCard(auth, numberCards, gameId);
    }

    @Override
    public Object returnDestinationCards(String auth, String gameId, int[] rejectedCardPositions) {
        CardService service = new CardService();
        return service.returnDestinationCards(auth, gameId, rejectedCardPositions);
    }

    @Override
    public Object drawFaceUp(String auth, String gameID, Integer i) {
       CardService service = new CardService();
        return service.drawFaceUp(auth, gameID, i);
    }

    @Override
    public Object sendFinalPoints(String auth, String gameID, FinalGamePoints finalGamePoints) {
        GameService service = new GameService();
        return service.updatePlayerFinalPoints(auth, gameID, finalGamePoints);
    }

    @Override
    public Object rejectRestore(String authToken) {
        GameListService service = new GameListService();
        return service.rejectRestore(authToken);
    }
}
