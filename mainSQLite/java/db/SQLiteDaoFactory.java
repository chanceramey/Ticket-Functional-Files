package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import database.AbstractDaoFactory;
import db.daos.SQLAuthTokenDao;
import db.daos.SQLCommandsDao;
import db.daos.SQLGameDao;
import db.daos.SQLUserDao;

/**
 * Created by tjense25 on 4/9/18.
 */

public class SQLiteDaoFactory extends AbstractDaoFactory {
    private String dbFilePath = null;
    private Connection connection = null;

    public SQLiteDaoFactory() throws DatabaseException {
        assert(dbFilePath != null);
        try {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR. Server could not load Database Driver");
            e.printStackTrace();
            throw new DatabaseException();
        }
    }

    @Override
    public void setDBFilePath(String filepath) {
        this.dbFilePath = filepath;
    }

    @Override
    public void startTransaction() throws DatabaseException {
        String connectionURL = "jdbc:sqlite:" + dbFilePath;
        try {
            connection = DriverManager.getConnection(connectionURL);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DatabaseException();
        }
        instantiateDaos();
    }

    @Override
    public void commitTransaction() throws DatabaseException{
        try {
            connection.commit();
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                throw new DatabaseException();
            }
        }
        connection = null;
    }

    @Override
    public void rollbackTransaction() throws DatabaseException {
        try{
            connection.rollback();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        finally{
            try {
                if(connection != null) connection.close();
            } catch (SQLException e) {
                throw new DatabaseException();
            }
        }
        connection = null;
    }

    private void instantiateDaos() throws DatabaseException {

        setUserDao( new SQLUserDao(connection));
        setAuthTokenDao( new SQLAuthTokenDao(connection));
        setGameDao( new SQLGameDao(connection));
        setCommandsDao(new SQLCommandsDao(connection));
    }
}
