package server;

import command.Command;
import communication.ClientProxy;
import model.ServerModel;
import model.User;

/**
 * Created by Isaak on 2/6/2018.
 */

public class RegisterService {

    private ServerModel serverModel = ServerModel.getInstance();
    private ClientProxy clientProxy = new ClientProxy();

    public Command register(String username, String password, String firstName, String lastName){

        if(username == null || password == null || firstName == null || lastName == null){
            clientProxy.promptRenewSession(); //recursive?
            return clientProxy.getCommand();
        }
        try {
            serverModel.getUser(username); //more checks? is this one too messy
            clientProxy.promptRenewSession();
            return clientProxy.getCommand();

        } catch (ServerModel.UserNotFoundException e) {

        }

        User user = new User(username, password, firstName, lastName);
        String authToken = serverModel.addAuthForUser(username);
        serverModel.addUser(user);
        serverModel.addGameListClient(authToken);

        clientProxy.onRegister(authToken);
        return clientProxy.getCommand();
    }
}
