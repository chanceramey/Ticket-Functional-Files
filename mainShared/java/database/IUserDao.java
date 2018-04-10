package database;

import model.User;

/**
 * Created by tjense25 on 4/9/18.
 */

public abstract class IUserDao implements IDao {
    abstract public int addUser(User user) throws AbstractDaoFactory.DatabaseException;
    abstract public int updateUser(String username, String gameID) throws AbstractDaoFactory.DatabaseException;
    abstract public User getUser(String username) throws AbstractDaoFactory.DatabaseException;
}
