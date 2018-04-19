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
        this.command = Command.createCommand(CLIENT_TARGET, "onLogin", authToken, username);
    }

    @Override
    public void onRegister(String authToken, String username) {
        this.command = Command.createCommand(CLIENT_TARGET, "onRegister", authToken, username);
    }

    @Override
    public void displayError(String message) {
        this.command = Command.createCommand(CLIENT_TARGET, "displayError", message);
    }

    @Override
    public void onCreateGame(Game game) {
        this.command = Command.createCommand(CLIENT_TARGET, "onCreateGame", game);
    }

    @Override
    public void onJoinGame(Game game) {
        this.command = Command.createCommand(CLIENT_TARGET, "onJoinGame", game);
    }

    @Override
    public void onLeaveGame() {
        this.command = Command.createCommand(CLIENT_TARGET, "onLeaveGame");
    }

    @Override
    public void onGameStarted() { this.command = Command.createCommand(CLIENT_TARGET, "onGameStarted"); }

    @Override
    public void onGetServerGameList(Game[] games) {
        String[] paramTypes = {games.getClass().getName()};
        Object[] params = {games};
        this.command = new Command(CLIENT_TARGET, "onGetServerGameList", paramTypes, params);    }

    @Override
    public void addGametoList(Game game) {
        this.command = Command.createCommand(CLIENT_TARGET, "addGametoList", game);
    }

    @Override
    public void removeGameFromList(String gameID) {
        this.command = Command.createCommand(CLIENT_TARGET, "removeGameFromList", gameID);
    }

    @Override
    public void updateGame(Game game) {
        this.command = Command.createCommand(CLIENT_TARGET, "updateGame", game);
    }

    @Override
    public void receiveMessage(GameHistory gameHistory) {
        this.command = Command.createCommand(CLIENT_TARGET, "receiveMessage", gameHistory);
    }
    @Override
    public void drawTrainCards(Integer player, Integer numCards, TrainCard[] cards, Integer size) {
        this.command = Command.createCommand(CLIENT_TARGET, "drawTrainCards", player, numCards, cards, size);
    }

    @Override
    public void discardTrainCards(Integer player, Integer numCards, int[] pos, Integer size) {
        this.command = Command.createCommand(CLIENT_TARGET, "discardTrainCards", player, numCards, pos, size);
    }

    @Override
    public void drawDestCards(Integer player, Integer numCards, DestinationCard[] cards, Integer size) {
        this.command = Command.createCommand(CLIENT_TARGET, "drawDestCards", player, numCards, cards, size);
    }

    @Override
    public void discardDestCards(Integer player, Integer numCards, int[] pos, Integer size) {
        this.command = Command.createCommand(CLIENT_TARGET, "discardDestCards", player, numCards, pos, size);
    }

    @Override
    public void swapFaceUpCards(int[] pos, TrainCard[] cards, Integer size) {
        this.command = Command.createCommand(CLIENT_TARGET, "swapFaceUpCards", pos, cards, size);
    }

    @Override
    public void claimedRoute(Integer player, String routeID) {
        this.command = Command.createCommand(CLIENT_TARGET, "claimedRoute", player, routeID);
    }

    @Override
    public void promptRenewSession() {
        this.command = Command.createCommand(CLIENT_TARGET, "promptRenewSession");
    }

    public void onGameEnded() {
        this.command = Command.createCommand(CLIENT_TARGET, "onGameEnded");
    }

    @Override
    public void setTurn(Integer player) {
        this.command = Command.createCommand(CLIENT_TARGET, "setTurn", player);
    }

    @Override
    public void setLastTurn() {
        this.command = Command.createCommand(CLIENT_TARGET, "setLastTurn");
    }

    @Override
    public void updateAllPlayerFinalPoints(FinalGamePoints[] allFinalPoints) {
        String[] paramTypes = {allFinalPoints.getClass().getName()};
        Object[] params = {allFinalPoints};
        this.command = new Command(CLIENT_TARGET, "updateAllPlayerFinalPoints", paramTypes, params);
    }

    @Override
    public void promptRestoreGame(Game game) {
        this.command = Command.createCommand(CLIENT_TARGET, "promptRestoreGame", game);
    }

    public Command getCommand() {
        return this.command;
    }

    private final String CLIENT_TARGET = "com.team.jcti.ttr.communication.ClientFacade";

}
