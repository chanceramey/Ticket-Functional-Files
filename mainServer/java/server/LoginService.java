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

    public Command login(String username, String password){

        try {
            user = serverModel.getUser(username);

        } catch (ServerModel.UserNotFoundException e) {
            clientProxy.promptRenewSession();
            return clientProxy.getCommand();
        }

        if(username == null || password == null || !password.equals(user.getPassword())){
            clientProxy.promptRenewSession();
            return clientProxy.getCommand();
        }

        String authToken = serverModel.addAuthForUser(username);
        serverModel.addGameListClient(authToken);


        clientProxy.onLogin(authToken);
        return clientProxy.getCommand();
    }
}
