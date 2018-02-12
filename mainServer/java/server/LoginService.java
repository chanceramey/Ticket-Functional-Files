package server;

import command.Command;
import communication.ClientProxy;
import model.ServerModel;
import model.User;

/**
 * Created by Isaak on 2/6/2018.
 */

public class LoginService {

    private ServerModel serverModel = ServerModel.getInstance();
    private ClientProxy clientProxy = new ClientProxy();
    private User user;

    public Command[] login(String username, String password){
        try {
            user = serverModel.getUser(username);

        } catch (ServerModel.UserNotFoundException e) {
            clientProxy.displayError("Username or password incorrect");
            Command[] commands = {clientProxy.getCommand()};
            return commands;
        }

        if(username == null || password == null || !password.equals(user.getPassword())){
            clientProxy.displayError("Username or password incorrect");
            Command[] commands = {clientProxy.getCommand()};
            return commands;
        }

        String authToken = serverModel.addAuthForUser(username);

        clientProxy.onLogin(authToken);
        Command[] commands = {clientProxy.getCommand()};
        return commands;
    }
}
