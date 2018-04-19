package database;

import java.util.List;

import command.Command;

/**
 * Created by tjense25 on 4/9/18.
 */

public abstract class ICommandsDao implements IDao {
    abstract public void addCommand(String gameID, Command command) throws AbstractDaoFactory.DatabaseException;
    abstract public int getCommandCount(String gameID) throws AbstractDaoFactory.DatabaseException;
    abstract public  List<Command> getCommands(String gameID) throws AbstractDaoFactory.DatabaseException;
    abstract public void clearCommands(String gameID) throws AbstractDaoFactory.DatabaseException;
}
