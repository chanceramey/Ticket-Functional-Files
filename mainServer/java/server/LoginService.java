package server;

import java.util.UUID;

import command.Command;
import communication.ClientProxy;
import model.AuthToken;
import model.ServerModel;
import model.User;
import model.db.PersistenceFacade;

/**
 * Created by Isaak on 2/6/2018.
 */

public class LoginService {

    private ServerModel serverModel = ServerModel.getInstance();
    private ClientProxy clientProxy = new ClientProxy();
    private User user;

    public Command[] login(String username, String password){

        if(username == null || password == null || !password.equals(user.getPassword())){
            clientProxy.displayError("Username or password incorrect");
            Command[] commands = {clientProxy.getCommand()};
            return commands;
        }

        PersistenceFacade persistenceFacade = serverModel.getPersistenceFacade();

        boolean validateCredentials = persistenceFacade.validateCredentials(username, password);

        if (!validateCredentials) {
            clientProxy.displayError("Username or password incorrect");
            Command[] commands = {clientProxy.getCommand()};
            return commands;
        }

        AuthToken userAuthToken = new AuthToken(username, UUID.randomUUID().toString());
        persistenceFacade.registerAuthToken(userAuthToken);

        clientProxy.onLogin(userAuthToken.getToken(), username);
        Command[] commands = {clientProxy.getCommand()};
        return commands;
    }
}
