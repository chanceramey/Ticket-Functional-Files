package server;

import java.util.List;

import javax.swing.DefaultSingleSelectionModel;

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

public class ReturnDestCardsService { //ikes

    private ServerModel mServerModel = ServerModel.getInstance();
    private ClientProxy clientProxy = new ClientProxy();

    public Command[] returnDestinationCards(String auth, String gameId, int[] rejectedCardPositions){

        String username;
        try {
            username = mServerModel.getUserFromAuth(auth);

            ServerGameModel game = mServerModel.getActiveGame(gameId);

            DestCardDeck destCardDeck = game.getDestCardDeck();
            Player player = game.getPlayerFromUsername(username);

            if(rejectedCardPositions.length == 0){
                Command[] commands = {clientProxy.getCommand()};
                return commands;
            }

            destCardDeck.discard(player.removeDestCards(rejectedCardPositions));


            clientProxy.discardDestCards(player.getId(), rejectedCardPositions.length, rejectedCardPositions); //comeback
            Command[] commands = {clientProxy.getCommand()};
            return commands;

        } catch (ServerModel.AuthTokenNotFoundException | ServerModel.GameNotFoundException e) {
            clientProxy.promptRenewSession();
            Command[] commands = {clientProxy.getCommand()};
            return commands;
        }


    }
}

