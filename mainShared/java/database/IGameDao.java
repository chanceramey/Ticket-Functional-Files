package database;

import java.util.List;

import interfaces.IGame;

/**
 * Created by tjense25 on 4/9/18.
 */

public abstract class IGameDao implements IDao {
    abstract public void addGame(IGame game) throws AbstractDaoFactory.DatabaseException;
    abstract public void updateGame(IGame game) throws AbstractDaoFactory.DatabaseException;
    abstract public String[] getGame(String gameID) throws AbstractDaoFactory.DatabaseException;
    abstract public void deleteGame(String gameID) throws AbstractDaoFactory.DatabaseException;
    abstract public List<String> getWaitingGames(String className) throws AbstractDaoFactory.DatabaseException;
}
