package interfaces;

import model.GameHistory;

/**
 * Created by Isaak on 2/2/2018.
 */

public interface IServer {

    Object login(String username, String password);

    Object register(String username, String password, String firstName, String lastName);

    Object createGame(int numPlayers, String gameName, String authToken);

    Object joinGame(String authtoken, String gameId);

    Object leaveGame(String authToken, String gameId);

    Object getServerGames(String auth);

    Object getCommands(String auth);

    Object startGame(String auth, String gameId);

    Object sendMessage(String auth, String gameId, GameHistory historyObj); // sends message from the chat activity

    Object getGameCommands(String auth, String gameID, Integer gameHistoryPosition);

    Object claimRoute(String auth, String gameID, String routeID);

    Object drawTrainCards(String auth, Integer numberToDraw, String gameId);

    Object drawDestinationCards(String auth, String gameId);

    Object returnDestinationCards(String auth, String gameId, int[] rejectedCardPositions);

    Object drawFaceUp(String auth, String gameID, Integer i);
}
