package interfaces;

import java.util.List;

import model.Game;

/**
 * Created by Isaak on 2/2/2018.
 */

public interface IClient {

    public void onLogin(String authToken);

    public void onRegister(String authToken);

    public void displayError();

    public void promptRenewSession();

    public void onCreateGame(Game game);

    public void onJoinGame(Game game);

    public void onGetServerGameList(Game[] games);

    public void addGametoList(Game game);

    public void removeGameFromList(String gameID);

    public void updateGameInList(Game game);
}
