package com.team.jcti.ttr.communication;

import command.Command;
import interfaces.IServer;

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

    private ClientCommunicator clientCommunicator;

    private ServerProxy() {}

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
        Object[] params = {numPlayers, gameName, authToken};
        String[] paramTypes = {"int", gameName.getClass().getName(), authToken.getClass().getName()};
        Command command = new Command(SERVER_TARGET, "createGame", paramTypes, params);
        clientCommunicator = new ClientCommunicator();
        clientCommunicator.execute(command);
        return null;
    }

    @Override
    public Object joinGame(String username, String gameId) {
        Object[] params = {username, gameId};
        String[] paramTypes = {username.getClass().getName(), gameId.getClass().getName()};
        Command command = new Command(SERVER_TARGET, "joinGame", paramTypes, params);
        clientCommunicator = new ClientCommunicator();
        clientCommunicator.execute(command);
        return null;
    }

    @Override
    public Object getServerGames() {
        Command command = new Command(SERVER_TARGET, "getServerGames", null, null);
        clientCommunicator = new ClientCommunicator();
        clientCommunicator.execute(command);
        return null;
    }

    private final String SERVER_TARGET = "server.ServerFacade";
}

