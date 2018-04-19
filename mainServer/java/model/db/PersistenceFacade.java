package model.db;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import command.Command;
import database.AbstractDaoFactory;
import database.AbstractDaoFactory.DatabaseException;
import database.IAuthTokenDao;
import interfaces.IGame;
import model.AuthToken;
import model.Game;
import model.User;

/**
 * Created by Chance on 4/11/18.
 */

public class PersistenceFacade {
    DaoFactoryRegistry mDaoFactoryRegistry = new DaoFactoryRegistry();
    AbstractDaoFactory mDaoFactory;

    public int getmBackupInterval() {
        return mBackupInterval;
    }

    int mBackupInterval = 0;

    public PersistenceFacade() {

        try {
            mDaoFactory = mDaoFactoryRegistry.registerPlugin(mDaoFactoryRegistry.getChoice());
            mBackupInterval = mDaoFactoryRegistry.getInterval();
            System.out.println(mDaoFactory.getClass().getName() + " is ready. Backup interval is set to " + mBackupInterval + ".");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveGameState(IGame game){

        try{
            mDaoFactory.startTransaction();
            mDaoFactory.getGameDao().updateGame(game);
            mDaoFactory.commitTransaction();

        } catch (AbstractDaoFactory.DatabaseException e) {
            e.printStackTrace();
        }
    }

    public IGame restoreGame(String username){ //expected implementation?

        return getGame(getUserGameId(username));
    }

    public String getUserGameId(String username){

        try{
            mDaoFactory.startTransaction();
            String gameId = mDaoFactory.getUserDao().getUser(username).getGameID();
            mDaoFactory.commitTransaction();
            return gameId;

        } catch (AbstractDaoFactory.DatabaseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public IGame getGame(String gameId){ //not complete - wasn't sure what to do with json

        try{
            mDaoFactory.startTransaction();
            String[] serialized = mDaoFactory.getGameDao().getGame(gameId);
            mDaoFactory.commitTransaction();
            Class<?> klass = Class.forName(serialized[0]);
            return (IGame) new Gson().fromJson(serialized[1], klass);

        } catch (AbstractDaoFactory.DatabaseException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void addUser(User user) {
        try {
            mDaoFactory.startTransaction();
            mDaoFactory.getUserDao().addUser(user);
            mDaoFactory.commitTransaction();
        } catch (AbstractDaoFactory.DatabaseException e) {
            e.printStackTrace();
        }
    }

    public User getUser(String username) {
        try {
            mDaoFactory.startTransaction();
            User user = mDaoFactory.getUserDao().getUser(username);
            mDaoFactory.commitTransaction();
            return user;
        } catch (AbstractDaoFactory.DatabaseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean validateCredentials(String username, String password)  {

        try{
            mDaoFactory.startTransaction();
            User user = mDaoFactory.getUserDao().getUser(username);
            mDaoFactory.commitTransaction();
            if (user == null) return false;
            return password.equals(user.getPassword());

        } catch (AbstractDaoFactory.DatabaseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void registerAuthToken(AuthToken authToken){
        try {
            mDaoFactory.startTransaction();
            IAuthTokenDao authTokenDao = mDaoFactory.getAuthTokenDao();
            authTokenDao.addAuth(authToken);
            mDaoFactory.commitTransaction();

        } catch (AbstractDaoFactory.DatabaseException e) {
            e.printStackTrace();
        }
    }


    public boolean validateAuthToken(String authToken){

        try{
            mDaoFactory.startTransaction();
            String username = mDaoFactory.getAuthTokenDao().getUsername(authToken);
            mDaoFactory.commitTransaction();
            if (username != null && !username.equals("")) {
                return true;
            }

        } catch (AbstractDaoFactory.DatabaseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void addGame(IGame game) {
        try {
            mDaoFactory.startTransaction();
            mDaoFactory.getGameDao().addGame(game);
            mDaoFactory.commitTransaction();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    public void updateGame(IGame game) {
        try {
            mDaoFactory.startTransaction();
            mDaoFactory.getGameDao().updateGame(game);
            mDaoFactory.commitTransaction();
        } catch (AbstractDaoFactory.DatabaseException e) {
            e.printStackTrace();
        }
    }

    public void addGameIdToUser(String username, String gameId) {
        try {
            mDaoFactory.startTransaction();
            mDaoFactory.getUserDao().updateUser(username, gameId);
            mDaoFactory.commitTransaction();
        } catch (AbstractDaoFactory.DatabaseException e) {
            e.printStackTrace();
        }
    }

    public String getUsernameFromAuthToken(String authToken) {
        try{
            mDaoFactory.startTransaction();
            String username = mDaoFactory.getAuthTokenDao().getUsername(authToken);
            mDaoFactory.commitTransaction();
            return username;

        } catch (AbstractDaoFactory.DatabaseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void deleteGame(String gameId) {
        try {
            mDaoFactory.startTransaction();
            mDaoFactory.getGameDao().deleteGame(gameId);
            mDaoFactory.commitTransaction();
        } catch (AbstractDaoFactory.DatabaseException e) {
            e.printStackTrace();
        }
    }

    public void storeGameCommands(List<Command> gameCommands, String gameId){ //did I understand correctly?

        try{
            mDaoFactory.startTransaction();
            for(Command command : gameCommands){
                mDaoFactory.getCommandsDao().addCommand(gameId, command);
            }
            mDaoFactory.commitTransaction();

        } catch (AbstractDaoFactory.DatabaseException e) {
            e.printStackTrace();
        }
    }


    public Map<String, Game> getWaitingGames() {
        Map<String,Game> waitingGames = new HashMap<>();
        try {
            mDaoFactory.startTransaction();
            List<String> serializedGames = mDaoFactory.getGameDao().getWaitingGames(Game.class.getName());
            mDaoFactory.commitTransaction();
            for (int i = 0; i < serializedGames.size(); i++) {
                Class<?> klass = Class.forName(serializedGames.get(++i));
                Game game = (Game) new Gson().fromJson(serializedGames.get(i), klass);
                waitingGames.put(game.getID(), game);
            }
        } catch (ClassNotFoundException | DatabaseException e) {
            e.printStackTrace();
        }
        return waitingGames;
    }
}


