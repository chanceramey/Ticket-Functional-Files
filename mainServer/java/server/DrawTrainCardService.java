package server;

import command.Command;
import communication.ClientProxy;
import model.DestCardDeck;
import model.DestinationCard;
import model.Player;
import model.ServerGameModel;
import model.ServerModel;
import model.TrainCard;
import model.TrainCardDeck;

/**
 * Created by Chance on 3/10/18.
 */

public class DrawTrainCardService {

    private ServerModel mServerModel = ServerModel.getInstance();
    private ClientProxy clientProxy = new ClientProxy();


    public Command[] drawTrainCard(String auth, String gameId){

        String username;
        try {
            username = mServerModel.getUserFromAuth(auth);
            ServerGameModel game = mServerModel.getActiveGame(gameId);

            TrainCardDeck trainCardDeck = game.getTrainCardDeck();
            Player player = game.getPlayerFromUsername(username); //active player? checkback

            TrainCard drawnTrainCard = trainCardDeck.drawCard();

            if(drawnTrainCard == null){
                clientProxy.displayError("No train cards left.");
                Command[] commands = {clientProxy.getCommand()};
                return commands;
            }

            player.addTrainCard(drawnTrainCard);

            clientProxy.drawTrainCard(player.getId(), drawnTrainCard);
            Command[] commands = {clientProxy.getCommand()};
            return commands;


        } catch (ServerModel.AuthTokenNotFoundException | ServerModel.GameNotFoundException e) {
            clientProxy.promptRenewSession();
            Command[] commands = {clientProxy.getCommand()};
            return commands;
        }
    }
}
