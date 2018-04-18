package server;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import command.Command;
import communication.ClientProxy;
import model.ServerGameModel;
import model.ServerModel;
import model.db.PersistenceFacade;

/**
 * Created by Tanner Jensen on 2/10/2018.
 */

public class GetCommandsService extends AbstractService {

    public Command[] getCommands(String auth) {

        PersistenceFacade persistenceFacade = mServerModel.getPersistenceFacade();
        String username = persistenceFacade.getUsernameFromAuthToken(auth);
        boolean validateAuthToken = (username != null && !username.equals(""));
        if (!validateAuthToken) {
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

    public Command[] getGameCommands(String auth, String gameID, Integer gameHistoryPosition) {
        PersistenceFacade persistenceFacade = mServerModel.getPersistenceFacade();
        String username = persistenceFacade.getUsernameFromAuthToken(auth);
        boolean validateAuthToken = (username != null && !username.equals(""));
        if (!validateAuthToken) {
            return promptRenewSession();
        }
        ServerGameModel game;
        try {
            game = mServerModel.getActiveGame(gameID);
        } catch (ServerModel.GameNotFoundException e) {
            return displayError("Game Not Found");
        }
        return game.getGameCommands(gameHistoryPosition);
    }
}
