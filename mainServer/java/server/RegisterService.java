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

public class RegisterService {

    private ServerModel serverModel = ServerModel.getInstance();
    private ClientProxy clientProxy = new ClientProxy();

    public Command[] register(String username, String password, String firstName, String lastName){

        if(username == null || password == null || firstName == null || lastName == null){
            clientProxy.displayError("Register credentials not valid. Please fill in all fields"); //recursive?
            Command[] commands = {clientProxy.getCommand()};
            return commands;
        }

        PersistenceFacade persistenceFacade = serverModel.getPersistenceFacade();

        User existingUser = persistenceFacade.getUser(username);
        if (existingUser != null) {
            //USER ALREADY EXISTS SO DISPLAY ERROR
            clientProxy.displayError("Username already in use. Please choose another one");
            Command[] commands = {clientProxy.getCommand()};
            return commands;
        }


        User user = new User(username, password, firstName, lastName);
        AuthToken userAuthToken = new AuthToken(username, UUID.randomUUID().toString());
        persistenceFacade.addUser(user);
        persistenceFacade.registerAuthToken(userAuthToken);


        clientProxy.onRegister(userAuthToken.getToken(), username);
        Command[] commands = {clientProxy.getCommand()};
        return commands;
    }
}
