package interfaces;

import java.util.List;

import model.Color;
import model.DestinationCard;
import model.Game;
import model.TrainCard;

/**
 * Created by Isaak on 2/2/2018.
 */

public interface IClient {

    public void onLogin(String authToken, String username);

    public void onRegister(String authToken, String username);

    public void displayError(String message);

    public void promptRenewSession();

    public void onCreateGame(Game game);

    public void onJoinGame(Game game);

    public void onLeaveGame();

    public void onGameStarted();

    public void onGetServerGameList(Game[] games);

    public void addGametoList(Game game);

    public void removeGameFromList(String gameID);

    public void updateGame(Game game);

    public void drawTrainCards(Integer player, Integer numCards, TrainCard[] cards);

    public void discardTrainCards(Integer player, Integer numCards, int[] pos);

    public void drawDestCards(Integer player, Integer numCards, DestinationCard[] cards);

    public void discardDestCards(Integer player, Integer numCards, int[] pos);

    public void swapFaceUpCards(int[] pos, TrainCard[] cards);
}
