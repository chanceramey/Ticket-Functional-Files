package communication;

import command.Command;
import interfaces.IClient;
import model.Color;
import model.DestinationCard;
import model.Game;
import model.TrainCard;

/**
 * Created by Isaak on 2/2/2018.
 */

public class ClientProxy implements IClient {

    private Command command;

    public ClientProxy() {
    }

    @Override
    public void onLogin(String authToken, String username) {
        this.command = createCommand("onLogin", authToken, username);
    }

    @Override
    public void onRegister(String authToken, String username) {
        this.command = createCommand("onRegister", authToken, username);
    }

    @Override
    public void displayError(String message) {
        this.command = createCommand("displayError", message);
    }

    @Override
    public void onCreateGame(Game game) {
        this.command = createCommand("onCreateGame", game);
    }

    @Override
    public void onJoinGame(Game game) {
        this.command = createCommand("onJoinGame", game);
    }

    @Override
    public void onLeaveGame() {
        this.command = createCommand("onJoinGame");
    }

    @Override
    public void onGameStarted() { this.command = createCommand("onGameStarted"); }

    @Override
    public void onGetServerGameList(Game[] games) {
        this.command = createCommand("onGetServerGameList", games);
    }

    @Override
    public void addGametoList(Game game) {
        this.command = createCommand("addGametoList", game);
    }

    @Override
    public void removeGameFromList(String gameID) {
        this.command = createCommand("removeGameFromList", gameID);
    }

    @Override
    public void updateGame(Game game) {
        this.command = createCommand("updateGame", game);
    }

    @Override
    public void drawTrainCards(Integer player, Integer numCards, TrainCard[] cards) {
        this.command = createCommand("drawTrainCards", player, numCards, cards);
    }

    @Override
    public void discardTrainCards(Integer player, Integer numCards, int[] pos) {
        this.command = createCommand("discardTrainCards", player, numCards, pos);
    }

    @Override
    public void drawDestCards(Integer player, Integer numCards, DestinationCard[] cards) {
        this.command = createCommand("drawDestCards", player, numCards, cards);
    }

    @Override
    public void discardDestCards(Integer player, Integer numCards, int[] pos) {
        this.command = createCommand("discardDestCards", player, numCards, pos);
    }

    @Override
    public void swapFaceUpCards(int[] pos, TrainCard[] cards) {
        this.command = createCommand("swapFaceUpCards", pos, cards);
    }

    @Override
    public void promptRenewSession() {
        createCommand("promptRenewSession");
    }

    private Command createCommand(String methodName, Object... params) {
        String CLIENT_TARGET = "com.team.jcti.ttr.communication.ClientFacade";

        String[] paramTypes = new String[params.length];
        for (int i = 0; i < paramTypes.length; i++) {
            paramTypes[i] = params[i].getClass().getName();

        }
        return new Command(CLIENT_TARGET, methodName, paramTypes, params);
    }


    public Command getCommand() {
        return this.command;
    }

}
