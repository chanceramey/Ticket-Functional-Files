package server;

import command.Command;
import communication.ClientProxy;
import model.FinalGamePoints;
import model.Game;
import model.GameHistory;
import model.ServerGameModel;
import model.ServerModel;
import model.User;
import model.db.PersistenceFacade;

/**
 * Created by Jeff on 2/28/2018.
 */

public class GameService extends AbstractService {


    public GameService() {
    }

    public Command[] sendMessage(String auth, String gameId, GameHistory historyObj) {
        PersistenceFacade persistenceFacade = mServerModel.getPersistenceFacade();
        String username = persistenceFacade.getUsernameFromAuthToken(auth);
        boolean validateAuthToken = (username != null && !username.equals(""));
        if (!validateAuthToken) {
            return promptRenewSession();
        }
        try {
            ServerGameModel gameModel = mServerModel.getActiveGame(gameId); // implement this method in ServerGameModel

            gameModel.sendMessage(historyObj);
            return new Command[]{};

        } catch (ServerModel.GameNotFoundException e) {
            return displayError("SERVER ERROR: Game not found");
        }
    }

    public Command[] claimRoute(String auth, String gameID, String routeID, int length, int[] cardPos) {
        PersistenceFacade persistenceFacade = mServerModel.getPersistenceFacade();
        String username = persistenceFacade.getUsernameFromAuthToken(auth);
        boolean validateAuthToken = (username != null && !username.equals(""));
        if (!validateAuthToken) {
            return promptRenewSession();
        }

        try {
            ServerGameModel gameModel = mServerModel.getActiveGame(gameID);

            if (!gameModel.claimRoute(username, routeID, length, cardPos)) {
                return displayError("Invalid move");
            }

        } catch (ServerModel.GameNotFoundException e) {
            return displayError("Could Not Find Game");
        }

        return new Command[0];
    }

    public Command[] updatePlayerFinalPoints(String auth, String gameID, FinalGamePoints finalGamePoints) {
        User user;
        PersistenceFacade persistenceFacade = mServerModel.getPersistenceFacade();
        String username = persistenceFacade.getUsernameFromAuthToken(auth);
        user = persistenceFacade.getUser(username);
        boolean validateAuthToken = (username != null && !username.equals(""));
        if (!validateAuthToken || user == null) {
            return promptRenewSession();
        }

        try {
            ServerGameModel gameModel = mServerModel.getActiveGame(gameID);

            if(!gameModel.addToAllPlayersPoints(finalGamePoints)){
                return displayError("Already added to points");
            }

        } catch (ServerModel.GameNotFoundException e) {
            return displayError("Could Not Find Game");
        }
        user.setGame(null);
        return new Command[0];
    }
}
