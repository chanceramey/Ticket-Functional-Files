package model;

import com.sun.security.ntlm.Server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import command.Command;

/**
 * Created by tjense25 on 2/2/18.
 */
public class ServerModel {

    //ServerModel singleton
    private static ServerModel SINGLETON;

    /**
     * Get Singleton to the ServerModel class. Instantiates the ServerModel if it is null, otherwise
     * just returns the singleton
     * @return The ServerModel Singleton Object.
     */
    public static ServerModel getInstance() {
        if (SINGLETON == null) {
            SINGLETON = new ServerModel();
        }
        return SINGLETON;
    }

    //Collection of all registered users
    //Store as a map of usernames to User objects for fast access
    private Map<String, User> users;

    //Map of authTokenIds to AuthToken objects. Collection of all currently active authtokens
    private Map<String, AuthToken> authTokens;

    //Set of clients who are in the the gameList Activity stored as a set of authTokens
    //These are the clients that need to be informed when a game is added or deleted
    private Set<String> gameListClients;

    //Collection of all games currently in the GameList Lobby waiting for players to start
    //Stored as a map of gameIds to Game objects for fast access
    private Map<String, Game> waitingGames;

    //Collection of all games that have been actually been started
    private Map<String, ServerGameModel> activeGames;

    //Queue of commands to be executed by
    private Map<String, List<Command>> sessionCommandQueue;

    /**
     * Constructor of ServerModel Main function is to initialize server data collections
     * and to schedule the ExpireAuthTask function to run every hour.
     * So each hour the server model will check to see if any authTokens have expired.
     */
    private ServerModel() {
        this.users = new HashMap<>();
        this.authTokens = new HashMap<>();
        this.gameListClients = new HashSet<>();
        this.waitingGames = new HashMap<>();
        this.activeGames = new HashMap<>();
        this.sessionCommandQueue = new HashMap<>();

        Timer timer = new Timer();
        int HOUR = 3600000; //3,600,000 milliseconds in one hour
        timer.schedule(new ExpireAuthsTask(), 0, HOUR);
    }

    /**
     * Adds User to the Server Model. Stores new user into user map
     * @param user User object to be added to map
     */
    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    /**
     * Gets the User object matching the passed in username and returns it
     * @param username username of the User to be accessed
     * @return User object matching the username param
     * @throws UserNotFoundException if no User with the given username is stored in the ServerModel
     */
    public User getUser(String username) throws UserNotFoundException {
        if (users.containsKey(username)) {
            return users.get(username);
        }
        else throw new UserNotFoundException();
    }

    /**
     * Creates an auth AuthToken and stores it into the authToken Map for the specific user
     * @param username The username for the User who is being logged in
     * @return Auth-Token UUID string
     */
    public String addAuthForUser(String username) {
        AuthToken newAuth = new AuthToken(username, UUID.randomUUID().toString());
        authTokens.put(newAuth.getToken(), newAuth);
        return newAuth.getToken();
    }

    /**
     * Returns a username for the user who is logged in with the passed in authToken
     * @param authToken authToken UUID string
     * @return Username of the User signed in with the passed in authToken
     */
    public String getUserFromAuth(String authToken) throws AuthTokenNotFoundException {
        if (authTokens.containsKey(authToken)) {
            return authTokens.get(authToken).getUser();
        }
        else throw new AuthTokenNotFoundException();
    }

    /**
     * Add a client to the list of Clients in the GameListActivity by storing their session AuthToken
     * in a set
     * @param authToken authToken of the client who entered the GameListActivity
     */
    public void addGameListClient(String authToken) {
        gameListClients.add(authToken);
    }

    public void removeGameListClient(String authToken) {
        if(gameListClients.contains(authToken)) {
            gameListClients.remove(authToken);
        }
    }

    public Map<String, Game> getWaitingGames() {
        return waitingGames;
    }

    public void setWaitingGames(Map<String, Game> waitingGames) {
        this.waitingGames = waitingGames;
    }

    /**
     * Adds a new Game to the Game list
     * @param game Game to be stored in the ServerModel
     */

    public void addWaitingGame(Game game) {
        waitingGames.put(game.getID(), game);
    }

    /**
     * Get the Game with the corresponding ID
     * @param gameID gameID of the game to be returned
     * @return Returns the game with matching gameID from either waiting or active games
     * @throws GameNotFoundException if no waiting or active game matches the gameID
     */
    public Game getGame(String gameID) throws GameNotFoundException {
        if (waitingGames.containsKey(gameID)) {
            return waitingGames.get(gameID);
        }
        else throw new GameNotFoundException();
    }

    /**
     * Delete a Game from the ServerModel
     * @param gameID gameID representing the game to be Deleted
     * @throws GameNotFoundException if the game is not in the waiting game or active game collections
     */
    public void deleteGame(String gameID) throws GameNotFoundException {
        if (waitingGames.containsKey(gameID)) {
            waitingGames.remove(gameID);
        }
        else if (activeGames.containsKey(gameID)) {
            activeGames.remove(gameID);
        }
        else throw new GameNotFoundException();
    }

    /**
     * Add a command to the sessionCommandQueue: commands server wants to execute on client side
     * @param auth the authToken or session ID of the client that needs to be updated
     * @param command The command to be added to the Queue
     */
    public void addCommand(String auth, Command command) {
        if(sessionCommandQueue.containsKey(auth)) {
            sessionCommandQueue.get(auth).add(command);
        } else {
            List<Command> list = new ArrayList<>();
            list.add(command);
            sessionCommandQueue.put(auth, list);
        }
    }

    /**
     * Returns the set of Clients in the GameListActivity
     * @return Set of Strings of SessionIDs
     */
    public Set<String> getGameListClients() {
        return this.gameListClients;
    }

    public List<Command> getCommandQueue(String auth) {
        return this.sessionCommandQueue.get(auth);
    }

    public void emptyCommandQueue(String auth) {
        this.sessionCommandQueue.remove(auth);
    }

    public boolean hasGameName(String gameName) {
        for (String gameID : waitingGames.keySet()) {
            if (gameName.equals(waitingGames.get(gameID).getGameName())) return true;
        }
        return false;
    }

    public void startGame(String gameId) {
        Game game = waitingGames.remove(gameId);
        ServerGameModel gameModel = new ServerGameModel(game);
        activeGames.put(gameId, gameModel);
        gameModel.startGame();
    }

    public ServerGameModel getActiveGame(String gameId) {
        return activeGames.get(gameId);
    }


    public class UserNotFoundException extends Exception {}
    public class AuthTokenNotFoundException extends Exception {}
    public class GameNotFoundException extends Exception {}

    /**
     * Task called every hour to delete old AuthTokens that are more than 3 hours old
     */
    private class ExpireAuthsTask extends TimerTask {
        private Date now;

        /**
         * Iterate through each of the authTokens in the AuthToken map at check if it has been
         * 3 hours since the Token was created. If it has been 3 hours, authToken expires and it
         * is deleted from the map.
         */
        @Override
        public void run() {
            //Initialize now with current time
            this.now = new Date();

            for(AuthToken at : authTokens.values()) {
                Calendar auth_time = at.getTimeCreated();
                auth_time.add(Calendar.HOUR, 3);
                if (now.after(auth_time.getTime())) {
                    authTokens.remove(at.getToken());
                    removeGameListClient(at.getToken());
                    sessionCommandQueue.remove(at.getToken());
                }
                auth_time.add(Calendar.HOUR, -3);
            }

        }
    }
}

