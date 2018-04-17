package model.db;

import com.google.gson.Gson;

import java.util.List;

import command.Command;
import database.AbstractDaoFactory;
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
            String gameString = mDaoFactory.getGameDao().getGame(gameId);
            mDaoFactory.commitTransaction();
            Game game = new Gson().fromJson(gameString, Game.class);
            return game;

        } catch (AbstractDaoFactory.DatabaseException e) {
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

    public boolean validateCredentials(String username, String password){

        try{
            mDaoFactory.startTransaction();
            User user = mDaoFactory.getUserDao().getUser(username);
            mDaoFactory.commitTransaction();
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


    public boolean validateAuthToken(String username, String authToken){

        try{
            mDaoFactory.startTransaction();
            boolean authTokenValid =  username.equals(mDaoFactory.getAuthTokenDao().getUsername(authToken));
            mDaoFactory.rollbackTransaction();
            return authTokenValid;

        } catch (AbstractDaoFactory.DatabaseException e) {
            e.printStackTrace();
        }

        return false;
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
    

}


