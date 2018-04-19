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

        clientProxy.onLogin(authToken, username);
        Command[] commands = {clientProxy.getCommand()};
        return commands;
    }

    public Command[] register(String username, String password, String firstName, String lastName){

        if(username == null || password == null || firstName == null || lastName == null){
            clientProxy.displayError("Register credentials not valid. Please fill in all fields"); //recursive?
            Command[] commands = {clientProxy.getCommand()};
            return commands;
        }
        try {
            serverModel.getUser(username); //more checks? is this one too messy
            //USER ALREADY EXISTS SO DISPLAY ERROR
            clientProxy.displayError("Username already in use. Please choose another one");
            Command[] commands = {clientProxy.getCommand()};
            return commands;

        } catch (ServerModel.UserNotFoundException e) {
            //Username is not taken, so continue!
        }

        User user = new User(username, password, firstName, lastName);
        String authToken = serverModel.addAuthForUser(username);
        serverModel.addUser(user);

        clientProxy.onRegister(authToken, username);
        Command[] commands = {clientProxy.getCommand()};
        return commands;
    }
}
