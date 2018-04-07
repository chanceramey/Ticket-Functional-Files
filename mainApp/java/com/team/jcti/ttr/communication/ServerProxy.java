package com.team.jcti.ttr.communication;

import command.Command;
import interfaces.IServer;
import model.FinalGamePoints;
import model.GameHistory;

/**
 * Created by Jeff on 2/2/2018.
 */
public class ServerProxy implements IServer {
    private static ServerProxy SINGLETON;

    public static ServerProxy getInstance() {
        if (SINGLETON == null) {
            SINGLETON = new ServerProxy();
        }
        return SINGLETON;
    }

    private ServerProxy() {
    }

    @Override
    public Object login(String username, String password) {
        Object[] params = {username, password};
        String[] paramTypes = {username.getClass().getName(), password.getClass().getName()};
        Command command = new Command(SERVER_TARGET, "login", paramTypes, params);
        new SendCommandTask().execute(command);
        return null;
    }

    @Override
    public Object register(String username, String password, String firstName, String lastName) {
        Object[] params = {username, password, firstName, lastName};
        String[] paramTypes = {username.getClass().getName(), password.getClass().getName(), firstName.getClass().getName(), lastName.getClass().getName()};
        Command command = new Command(SERVER_TARGET, "register", paramTypes, params);
        new SendCommandTask().execute(command);
        return null;
    }

    @Override
    public Object createGame(int numPlayers, String gameName, String authToken) {
        Object[] params = {numPlayers, gameName, authToken};
        String[] paramTypes = {"int", gameName.getClass().getName(), authToken.getClass().getName()};
        Command command = new Command(SERVER_TARGET, "createGame", paramTypes, params);
        new SendCommandTask().execute(command);
        return null;
    }

    @Override
    public Object joinGame(String authToken, String gameId) {
        Object[] params = {authToken, gameId};
        String[] paramTypes = {authToken.getClass().getName(), gameId.getClass().getName()};
        Command command = new Command(SERVER_TARGET, "joinGame", paramTypes, params);
        new SendCommandTask().execute(command);
        return null;
    }

    @Override
    public Object leaveGame(String authToken, String gameId) {
        Object[] params = {authToken, gameId};
        String[] paramTypes = {authToken.getClass().getName(), gameId.getClass().getName()};
        Command command = new Command(SERVER_TARGET, "leaveGame", paramTypes, params);
        new SendCommandTask().execute(command);
        return null;
    }

    @Override
    public Object getServerGames(String auth) {
        Object[] params = {auth};
        String[] paramTypes = {auth.getClass().getName()};
        Command command = new Command(SERVER_TARGET, "getServerGames", paramTypes, params);
        new SendCommandTask().execute(command);
        return null;
    }

    @Override
    public Object getCommands(String auth) {
        Object[] params = {auth};
        String[] paramTypes = {auth.getClass().getName()};
        Command command = new Command(SERVER_TARGET, "getCommands", paramTypes, params);
        new SendCommandTask().execute(command);
        return null;
    }

    @Override
    public Object startGame(String auth, String gameId) {
        Object[] params = {auth, gameId};
        String[] paramTypes = {auth.getClass().getName(), gameId.getClass().getName()};
        Command command = new Command(SERVER_TARGET, "startGame", paramTypes, params);
        new SendCommandTask().execute(command);
        return null;
    }

    public Object sendMessage(String auth, String gameId, GameHistory historyObj) {
        Object[] params = {auth, gameId, historyObj};
        String[] paramTypes = {auth.getClass().getName(), gameId.getClass().getName(), historyObj.getClass().getName()};
        Command command = new Command(SERVER_TARGET, "sendMessage", paramTypes, params);
        new SendCommandTask().execute(command);
        return null;
    }

    public Object claimRoute(String auth, String gameId, String routeId, Integer length, int[] cardPos) {
        Object[] params = {auth, gameId, routeId, length, cardPos};
        String[] paramTypes = {auth.getClass().getName(), gameId.getClass().getName(), routeId.getClass().getName(), length.getClass().getName(), cardPos.getClass().getName()};
        Command command = new Command(SERVER_TARGET, "claimRoute", paramTypes, params);
        new SendCommandTask().execute(command);
        return null;
    }

    @Override
    public Object drawDestinationCards(String auth, String gameId) {
        Object[] params = {auth, gameId};
        String[] paramTypes = {auth.getClass().getName(), gameId.getClass().getName()};
        Command command = new Command(SERVER_TARGET, "drawDestinationCards", paramTypes, params);
        new SendCommandTask().execute(command);
        return null;
    }

    @Override
    public Object drawTrainCards(String auth, Integer numberCards, String gameId) {
        Object[] params = {auth, numberCards, gameId};
        String[] paramTypes = {auth.getClass().getName(), numberCards.getClass().getName(), gameId.getClass().getName()};
        Command command = new Command(SERVER_TARGET, "drawTrainCards", paramTypes, params);
        new SendCommandTask().execute(command);
        return null;
    }

    @Override
    public Object returnDestinationCards(String auth, String gameId, int[] rejectedCardPositions) {
        Object[] params = {auth, gameId, rejectedCardPositions};
        String[] paramTypes = {auth.getClass().getName(), gameId.getClass().getName(), rejectedCardPositions.getClass().getName()};
        Command command = new Command(SERVER_TARGET, "returnDestinationCards", paramTypes, params);
        new SendCommandTask().execute(command);
        return null;
    }

    @Override
    public Object getGameCommands(String auth, String gameID, Integer gameHistoryPosition) {
        Object[] params = {auth, gameID, gameHistoryPosition};
        String[] paramTypes = {auth.getClass().getName(), gameID.getClass().getName(), gameHistoryPosition.getClass().getName() };
        Command command = new Command(SERVER_TARGET, "getGameCommands", paramTypes, params);
        new GetGameCommandsTask().execute(command);
        return null;
    }

    private final String SERVER_TARGET = "server.ServerFacade";

    @Override
    public Object drawFaceUp(String auth, String gameID, Integer i) {
        Object[] params = {auth, gameID, i};
        String[] paramTypes = {auth.getClass().getName(), gameID.getClass().getName(), i.getClass().getName()};
        Command command = new Command(SERVER_TARGET, "drawFaceUp", paramTypes, params);
        new SendCommandTask().execute(command);
        return null;
    }

    @Override
    public Object sendFinalPoints(String auth, String gameID, FinalGamePoints finalGamePoints) {
        Object[] params = {auth, gameID, finalGamePoints};
        String[] paramTypes = {auth.getClass().getName(), gameID.getClass().getName(), finalGamePoints.getClass().getName()};
        Command command = new Command(SERVER_TARGET, "sendFinalPoints", paramTypes, params);
        new SendCommandTask().execute(command);
        return null;
    }

    @Override
    public Object rejectRestore(String authToken) {
        Object[] params = {authToken};
        String[] paramTypes = {authToken.getClass().getName()};
        Command command = new Command(SERVER_TARGET, "rejectRestore", paramTypes, params);
        return null;
    }
}

