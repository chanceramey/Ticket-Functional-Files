package server;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import command.Command;
import communication.ClientProxy;
import model.ServerModel;

/**
 * Created by Tanner Jensen on 2/10/2018.
 */

public class GetCommandsService {
    private ServerModel mServerModel = ServerModel.getInstance();
    private ClientProxy mClientProxy = new ClientProxy();

    public Command[] getCommands(String auth) {
        String user = null;
        try {
            user = mServerModel.getUserFromAuth(auth);
        } catch (ServerModel.AuthTokenNotFoundException e) {
             mClientProxy.promptRenewSession();
             Command[] commands = {mClientProxy.getCommand()};
             return commands;
        }
        List<Command> userCommands = mServerModel.getCommandQueue(auth);
        Command[] commands;
        if (userCommands == null) commands = new Command[] {};
        else{
            commands = new Command[userCommands.size()];
            for (int i = 0; i < userCommands.size(); i++) {
                commands[i] = userCommands.get(i);
            }
        }
        mServerModel.emptyCommandQueue(auth);
        return commands;
    }
}
