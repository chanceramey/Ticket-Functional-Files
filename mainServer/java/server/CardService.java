package server;

import command.Command;
import communication.ClientProxy;
import model.ServerGameModel;
import model.ServerModel;
import model.User;
import model.db.PersistenceFacade;

/**
 * Created by Isaak on 3/7/2018.
 */

public class CardService extends AbstractService{

    private ServerModel mServerModel = ServerModel.getInstance();
    private ClientProxy clientProxy = new ClientProxy();


    public Command[] drawDestinationCards(String auth, String gameId){

        PersistenceFacade persistenceFacade = mServerModel.getPersistenceFacade();
        String username = persistenceFacade.getUsernameFromAuthToken(auth);
        boolean validateAuthToken = (username != null && !username.equals("") );
        if (!validateAuthToken) {
            return promptRenewSession();
        }

        try {
            ServerGameModel game = mServerModel.getActiveGame(gameId);

            if(game.drawDestCards(username)) {
                return new Command[] {};
            } else {
                return displayError("Invalid action");
            }

        } catch (ServerModel.GameNotFoundException e) {
            return promptRenewSession();
        }
    }

    public Command[] drawTrainCard(String auth, Integer numberCards, String gameId){

        PersistenceFacade persistenceFacade = mServerModel.getPersistenceFacade();
        String username = persistenceFacade.getUsernameFromAuthToken(auth);
        User user = persistenceFacade.getUser(username);
        boolean validateAuthToken = (username != null && !username.equals("") );
        if (!validateAuthToken) {
            return promptRenewSession();
        }

        try {

            ServerGameModel game = mServerModel.getActiveGame(gameId);

            if (game.drawDeckTrainCard(username)) {
                return new Command[] {};
            }
            else {
                return displayError("Invalid action");
            }

        } catch (ServerModel.GameNotFoundException e) {
            return displayError("SERVER ERROR");
        }
    }

    public Command[] returnDestinationCards(String auth, String gameId, int[] rejectedCardPositions){

        PersistenceFacade persistenceFacade = mServerModel.getPersistenceFacade();
        String username = persistenceFacade.getUsernameFromAuthToken(auth);
        boolean validateAuthToken = (username != null && !username.equals("") );
        if (!validateAuthToken) {
            return promptRenewSession();
        }
        try {

            ServerGameModel game = mServerModel.getActiveGame(gameId);
            game.returnDestinationCards(username, rejectedCardPositions);

            return new Command[] {};

        } catch (ServerModel.GameNotFoundException e) {
            return promptRenewSession();
        }


    }

    public Object drawFaceUp(String auth, String gameID, Integer i) {

        PersistenceFacade persistenceFacade = mServerModel.getPersistenceFacade();
        String username = persistenceFacade.getUsernameFromAuthToken(auth);
        boolean validateAuthToken = (username != null && !username.equals("") );
        if (!validateAuthToken) {
            return promptRenewSession();
        }
        try {
            ServerGameModel game = mServerModel.getActiveGame(gameID);
            if (game.drawFaceUp(username, i)) {
                return new Command[] {};
            } else {
                return displayError("Invalid action");
            }
        } catch (ServerModel.GameNotFoundException e) {
            return promptRenewSession();
        }

    }
}









