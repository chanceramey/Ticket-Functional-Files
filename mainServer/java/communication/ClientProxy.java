package communication;

import java.util.List;

import command.Command;
import interfaces.IClient;
import model.DestinationCard;
import model.FinalGamePoints;
import model.Game;
import model.GameHistory;
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
        this.command = createCommand("onLeaveGame");
    }

    @Override
    public void onGameStarted() { this.command = createCommand("onGameStarted"); }

    @Override
    public void onGetServerGameList(Game[] games) {
        String[] paramTypes = {games.getClass().getName()};
        Object[] params = {games};
        this.command = new Command(CLIENT_TARGET, "onGetServerGameList", paramTypes, params);    }

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
    public void receiveMessage(GameHistory gameHistory) {
        this.command = createCommand("receiveMessage", gameHistory);
    }
    @Override
    public void drawTrainCards(Integer player, Integer numCards, TrainCard[] cards, Integer size) {
        this.command = createCommand("drawTrainCards", player, numCards, cards, size);
    }

    @Override
    public void discardTrainCards(Integer player, Integer numCards, int[] pos, Integer size) {
        this.command = createCommand("discardTrainCards", player, numCards, pos, size);
    }

    @Override
    public void drawDestCards(Integer player, Integer numCards, DestinationCard[] cards, Integer size) {
        this.command = createCommand("drawDestCards", player, numCards, cards, size);
    }

    @Override
    public void discardDestCards(Integer player, Integer numCards, int[] pos, Integer size) {
        this.command = createCommand("discardDestCards", player, numCards, pos, size);
    }

    @Override
    public void swapFaceUpCards(int[] pos, TrainCard[] cards, Integer size) {
        this.command = createCommand("swapFaceUpCards", pos, cards, size);
    }

    @Override
    public void claimedRoute(Integer player, String routeID) {
        this.command = createCommand("claimedRoute", player, routeID);
    }

    @Override
    public void promptRenewSession() {
        this.command = createCommand("promptRenewSession");
    }

    private Command createCommand(String methodName, Object... params) {
        String CLIENT_TARGET = "com.team.jcti.ttr.communication.ClientFacade";

        String[] paramTypes = new String[params.length];
        for (int i = 0; i < paramTypes.length; i++) {
            paramTypes[i] = params[i].getClass().getName();

        }
        return new Command(CLIENT_TARGET, methodName, paramTypes, params);
    }

    public void onGameEnded() {
        this.command = createCommand("onGameEnded");
    }

    @Override
    public void setTurn(Integer player) {
        this.command = createCommand("setTurn", player);
    }

    @Override
    public void setLastTurn() {
        this.command = createCommand("setLastTurn");
    }

    @Override
    public void updateAllPlayerFinalPoints(FinalGamePoints[] allFinalPoints) {
        this.command = createCommand("updateAllPlayerFinalPoints", allFinalPoints);
    }

    public Command getCommand() {
        return this.command;
    }

    private String CLIENT_TARGET = "com.team.jcti.ttr.communication.ClientFacade";

}
