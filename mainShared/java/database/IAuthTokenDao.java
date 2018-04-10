package database;

import model.AuthToken;

/**
 * Created by tjense25 on 4/9/18.
 */

public abstract class IAuthTokenDao implements IDao {
    public abstract void addAuth(AuthToken auth) throws AbstractDaoFactory.DatabaseException;
    public abstract void deleteAuth(String token) throws AbstractDaoFactory.DatabaseException;
    public abstract String getUsername(String token) throws AbstractDaoFactory.DatabaseException;
}
