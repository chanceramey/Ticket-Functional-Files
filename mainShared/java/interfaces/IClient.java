package interfaces;

import model.DestinationCard;
import model.Game;
import model.GameHistory;
import model.TrainCard;

/**
 * Created by Isaak on 2/2/2018.
 */

public interface IClient {

    public void onLogin(String authToken, String username);

    public void onRegister(String authToken, String username);

    public void displayError(String message);

    void claimedRoute(Integer player, String routeID);

    public void promptRenewSession();

    public void onCreateGame(Game game);

    public void onJoinGame(Game game);

    public void onLeaveGame();

    public void onGameStarted();

    public void onGetServerGameList(Game[] games);

    public void addGametoList(Game game);

    public void removeGameFromList(String gameID);

    public void updateGame(Game game);

    public void receiveMessage(GameHistory gameHistory);

    public void drawTrainCards(Integer player, Integer numCards, TrainCard[] cards, Integer deckSize);

    public void discardTrainCards(Integer player, Integer numCards, int[] pos, Integer deckSize);

    public void drawDestCards(Integer player, Integer numCards, DestinationCard[] cards, Integer deckSize);

    public void discardDestCards(Integer player, Integer numCards, int[] pos, Integer deckSize);

    public void swapFaceUpCards(int[] pos, TrainCard[] cards, Integer deckSize);

    public void onGameEnded();

    public void setTurn(Integer player);

    public void setLastTurn();
}
