package server;

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
        Queue userCommands = mServerModel.getCommandQueue(auth);
        Command[] commands = (Command[]) userCommands.toArray();
        return commands;
    }
}
