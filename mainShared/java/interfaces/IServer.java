package interfaces;

/**
 * Created by Isaak on 2/2/2018.
 */

public interface IServer {

    public Object login(String username, String password);

    public Object register(String username, String password, String firstName, String lastName);

    public Object createGame(int numPlayers, String gameName, String authToken);

    public Object joinGame(String authtoken, String gameId);

    public Object getServerGames(String auth);

    public Object getCommands(String auth);
}
