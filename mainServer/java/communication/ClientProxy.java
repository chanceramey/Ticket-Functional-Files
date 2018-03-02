package communication;

import java.util.List;

import command.Command;
import interfaces.IClient;
import model.Game;
import model.GameHistory;

/**
 * Created by Isaak on 2/2/2018.
 */

public class ClientProxy implements IClient {

    private Command command;

    public ClientProxy() {}

    @Override
    public void onLogin(String authToken) {
        String[] paramTypes = {authToken.getClass().getName()};
        Object[] params = {authToken};
        this.command = new Command(CLIENT_TARGET, "onLogin", paramTypes, params);
    }

    @Override
    public void onRegister(String authToken) {
        String[] paramTypes = {authToken.getClass().getName()};
        Object[] params = {authToken};
        this.command = new Command(CLIENT_TARGET, "onRegister", paramTypes, params);
    }

    @Override
    public void displayError(String message) {
        String[] paramTypes = {message.getClass().getName()};
        Object[] params = {message};
        this.command = new Command(CLIENT_TARGET, "displayError", paramTypes, params);
    }

    @Override
    public void onCreateGame(Game game) {
        String[] paramTypes = {game.getClass().getName()};
        Object[] params = {game};
        this.command = new Command(CLIENT_TARGET, "onCreateGame", paramTypes, params);
    }

    @Override
    public void onJoinGame(Game game) {
        String[] paramTypes = {game.getClass().getName()};
        Object[] params = {game};
        this.command = new Command(CLIENT_TARGET, "onJoinGame", paramTypes, params);
    }

    @Override
    public void onLeaveGame() {
        String[] paramTypes = {};
        Object[] params = {};
        this.command = new Command(CLIENT_TARGET, "onLeaveGame", paramTypes, params);
    }

    @Override
    public void onGetServerGameList(Game[] games) {
        String[] paramTypes = {games.getClass().getName()};
        Object[] params = {games};
        this.command = new Command(CLIENT_TARGET, "onGetServerGameList", paramTypes, params);    }

    @Override
    public void addGametoList(Game game) {
        String[] paramTypes = {game.getClass().getName()};
        Object[] params = {game};
        this.command = new Command(CLIENT_TARGET, "addGametoList", paramTypes, params);
    }

    @Override
    public void removeGameFromList(String gameID) {
        String[] paramTypes = {gameID.getClass().getName()};
        Object[] params = {gameID};
        this.command  = new Command(CLIENT_TARGET, "removeGameFromList", paramTypes, params);
    }

    @Override
    public void updateGame(Game game) {
        String[] paramTypes = {game.getClass().getName()};
        Object[] params = {game};
        this.command = new Command(CLIENT_TARGET, "updateGame", paramTypes, params);
    }

    @Override
    public void promptRenewSession() {
        this.command = new Command(CLIENT_TARGET, "promptRenewSession", null, null);
    }




    public Command getCommand() {
        return this.command;
    }

    private String CLIENT_TARGET = "com.team.jcti.ttr.communication.ClientFacade";

}
