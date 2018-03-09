package server;

import java.util.List;

import command.Command;
import communication.ClientProxy;
import model.DestCardDeck;
import model.DestinationCard;
import model.Game;
import model.Player;
import model.ServerGameModel;
import model.ServerModel;

/**
 * Created by Isaak on 3/7/2018.
 */

public class DrawDestCardsService { //ikes

    private ServerModel mServerModel = ServerModel.getInstance();
    private ClientProxy clientProxy = new ClientProxy();


    public Command[] drawDestinationCards(String auth, String gameId){

        String username;
        try {
            username = mServerModel.getUserFromAuth(auth);
            ServerGameModel game = mServerModel.getActiveGame(gameId);

            DestCardDeck destCardDeck = game.getDestCardDeck();
            Player player = game.getPlayerFromUsername(username); //active player? checkback

            DestinationCard[] drawnDestCards = destCardDeck.drawCards(3);

            if(drawnDestCards.length == 0){
                clientProxy.displayError("No destination cards left.");
                Command[] commands = {clientProxy.getCommand()};
                return commands;
            }

            player.addDestCards(drawnDestCards);

            clientProxy.drawDestCards(player.getId(), drawnDestCards.length, drawnDestCards);
            Command[] commands = {clientProxy.getCommand()};
            return commands;


        } catch (ServerModel.AuthTokenNotFoundException | ServerModel.GameNotFoundException e) {
            clientProxy.promptRenewSession();
            Command[] commands = {clientProxy.getCommand()};
            return commands;
        }
    }
}









