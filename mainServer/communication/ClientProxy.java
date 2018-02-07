package communication;

import command.Command;
import interfaces.IClient;
import model.Game;

/**
 * Created by Isaak on 2/2/2018.
 */

public class ClientProxy implements IClient {

    private Command command;

    public ClientProxy() {}

    @Override
    public void onLogin(String authToken) {

    }

    @Override
    public void onRegister(String authToken) {

    }

    @Override
    public void displayError() {

    }

    @Override
    public void onCreateGame(Game game) {
        String[] paramTypes = {game.getClass().getName()};
        Object[] params = {game};
        this.command = new Command(CLIENT_TARGET, "onCreateGame", paramTypes, params);
    }

    @Override
    public void onJoinGame(String gameID) {

    }

    @Override
    public void onGetServerGameList(Game[] games) {

    }

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
    public void updateGameInList(Game game) {
        String[] paramTypes = {game.getClass().getName()};
        Object[] params = {game};
        this.command = new Command(CLIENT_TARGET, "updateGameInList", paramTypes, params);
    }

    @Override
    public void promptRenewSession() {

    }

    public Command getCommand() {
        return this.command;
    }

    private String CLIENT_TARGET = "com.team.jcti.ttr.communication.ClientFacade";

}
