package interfaces;

import java.util.List;

import model.Game;
import model.GameHistory;

/**
 * Created by Isaak on 2/2/2018.
 */

public interface IClient {

    public void onLogin(String authToken);

    public void onRegister(String authToken);

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

}
