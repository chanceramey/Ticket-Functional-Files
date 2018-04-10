package database;

/**
 * Created by tjense25 on 4/9/18.
 */

public abstract class AbstractDaoFactory {
    IUserDao userDao;
    IAuthTokenDao authTokenDao;
    ICommandsDao commandsDao;
    IGameDao gameDao;

    abstract public void startTransaction() throws DatabaseException;
    abstract public void commitTransaction() throws DatabaseException;
    abstract public void rollbackTransaction() throws DatabaseException;

    public IUserDao getUserDao() {
        return userDao;
    }

    public IAuthTokenDao getAuthTokenDao() {
        return authTokenDao;
    }

    public ICommandsDao getCommandsDao() {
        return commandsDao;
    }

    public IGameDao getGameDao() {
        return gameDao;
    }

    public void clear() throws DatabaseException {
        userDao.clear();
        authTokenDao.clear();
        commandsDao.clear();
        gameDao.clear();
    }

    protected void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }

    protected void setAuthTokenDao(IAuthTokenDao authTokenDao) {
        this.authTokenDao = authTokenDao;
    }

    protected void setCommandsDao(ICommandsDao commandsDao) {
        this.commandsDao = commandsDao;
    }

    protected void setGameDao(IGameDao gameDao) {
        this.gameDao = gameDao;
    }

    public static class DatabaseException extends Exception {
    }
}

