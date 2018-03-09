package server;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import command.Command;
import communication.ClientProxy;
import model.ServerGameModel;
import model.ServerModel;

/**
 * Created by Tanner Jensen on 2/10/2018.
 */

public class GetCommandsService extends AbstractService {

    public Command[] getCommands(String auth) {
        String user = null;
        try {
            user = mServerModel.getUserFromAuth(auth);
        } catch (ServerModel.AuthTokenNotFoundException e) {
           return promptRenewSession();
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

    public Command[] getGameCommands(String auth, String gameID, int gameHistoryPosition) {
        String user = null;
        try {
            user = mServerModel.getUserFromAuth(auth);
        } catch (ServerModel.AuthTokenNotFoundException e) {
            return promptRenewSession();
        }
        ServerGameModel game = null;
        try {
            mServerModel.getActiveGame(gameID);
        } catch (ServerModel.GameNotFoundException e) {
            return displayError("Game Not Found");
        }

        return game.getGameCommands(gameHistoryPosition);

    }
}
