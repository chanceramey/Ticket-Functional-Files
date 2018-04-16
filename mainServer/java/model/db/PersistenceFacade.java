package model.db;

import java.util.List;

import command.Command;
import database.AbstractDaoFactory;
import interfaces.IGame;
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
            mDaoFactory.getGameDao().updateGame(game);

        } catch (AbstractDaoFactory.DatabaseException e) {
            e.printStackTrace();
        }
    }

    public IGame restoreGame(String username){ //expected implementation?

        return getGame(getUserGameId(username));
    }

    public String getUserGameId(String username){

        try{
            return mDaoFactory.getUserDao().getUser(username).getGameID();

        } catch (AbstractDaoFactory.DatabaseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public IGame getGame(String gameId){ //not complete - wasn't sure what to do with json

        try{
            mDaoFactory.getGameDao().getGame(gameId);

        } catch (AbstractDaoFactory.DatabaseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean validateCredentials(String username, String password){

        try{
            User user = mDaoFactory.getUserDao().getUser(username);
            return password.equals(user.getPassword());

        } catch (AbstractDaoFactory.DatabaseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean validateAuthToken(String username, String authToken){

        try{
            return username.equals(mDaoFactory.getAuthTokenDao().getUsername(authToken));

        } catch (AbstractDaoFactory.DatabaseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void storeGameCommands(List<Command> gameCommands, String gameId){ //did I understand correctly?

        try{
            for(Command command : gameCommands){
                mDaoFactory.getCommandsDao().addCommand(gameId, command);
            }

        } catch (AbstractDaoFactory.DatabaseException e) {
            e.printStackTrace();
        }
    }

    public List<String> getPluginOptions(){

        return null;
        //return pRegistry.getAvailablePlugins();
    }

}


