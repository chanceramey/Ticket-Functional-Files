package server;


import command.Command;
import communication.ClientProxy;
import model.ServerModel;

/**
 * Created by tjense25 on 3/7/18.
 */

public abstract class AbstractService {

    protected ServerModel mServerModel = ServerModel.getInstance();
    protected ClientProxy mClientProxy = new ClientProxy();

    protected Command[] promptRenewSession() {
        mClientProxy.promptRenewSession();
        Command[] commands = {mClientProxy.getCommand()};
        return commands;
    }

    protected Command[] displayError(String message) {
        mClientProxy.displayError(message);
        Command[] commands = {mClientProxy.getCommand()};
        return commands;
    }

}
