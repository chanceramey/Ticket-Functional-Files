package communication;

import java.util.List;

import command.Command;
import database.AbstractDaoFactory;
import interfaces.IGame;
import model.User;

/**
 * Created by Isaak on 4/14/2018.
 */

public class PersistenceFacade {
    //PersistenceFactoryRegistry pRegistry = new PersistenceFactoryRegistry();
    AbstractDaoFactory persistencePlugin = null;

    public void saveGameState(IGame game){

        try{
            persistencePlugin.getGameDao().updateGame(game);

        } catch (AbstractDaoFactory.DatabaseException e) {
            e.printStackTrace();
        }
    }

    public IGame restoreGame(String username){ //expected implementation?

        return getGame(getUserGameId(username));
    }

    public String getUserGameId(String username){

        try{
            return persistencePlugin.getUserDao().getUser(username).getGameID();

        } catch (AbstractDaoFactory.DatabaseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public IGame getGame(String gameId){ //not complete - wasn't sure what to do with json

        try{
            persistencePlugin.getGameDao().getGame(gameId);

        } catch (AbstractDaoFactory.DatabaseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean validateCredentials(String username, String password){

        try{
            User user = persistencePlugin.getUserDao().getUser(username);
            return password.equals(user.getPassword());

        } catch (AbstractDaoFactory.DatabaseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean validateAuthToken(String username, String authToken){

        try{
            return username.equals(persistencePlugin.getAuthTokenDao().getUsername(authToken));

        } catch (AbstractDaoFactory.DatabaseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void storeGameCommands(List<Command> gameCommands, String gameId){ //did I understand correctly?

        try{
            for(Command command : gameCommands){
                persistencePlugin.getCommandsDao().addCommand(gameId, command);
            }

        } catch (AbstractDaoFactory.DatabaseException e) {
            e.printStackTrace();
        }
    }

    public List<String> getPluginOptions(){

        return null;
        //return pRegistry.getAvailablePlugins();
    }

    public void selectPersistencePlugin(String name){

        //persistencePlugin = pRegistry.registerPlugin(name); is this correct? Not sure by UML
    }
}
