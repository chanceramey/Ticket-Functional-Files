package db.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.AbstractDaoFactory.DatabaseException;
import database.IUserDao;
import model.User;

/**
 * Created by tjense25 on 4/9/18.
 */

public class SQLUserDao extends IUserDao {

    private Connection connection;

    public SQLUserDao(Connection connection ) throws DatabaseException {
        this.connection = connection;
        PreparedStatement stmt = null;
        try {
            String create = "CREATE TABLE IF NOT EXISTS user (username TEXT NOT NULL PRIMARY KEY, password TEXT NOT NULL," +
                    "firstname TEXT NOT NULL, lastname TEXT NOT NULL, gameid TEXT);";
            stmt = connection.prepareStatement(create);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException();
        } finally {
            if (stmt != null) try {
                stmt.close();
            } catch (SQLException e) {
                throw new DatabaseException();
            }
        }
    }
    @Override
    public int addUser(User user) throws DatabaseException{
        PreparedStatement stmt = null;
        int updates = 0;
        try {
            String insert = "INSERT INTO user (username, password, firstname, lastname) values (?, ?, ?, ?);";
            stmt = connection.prepareStatement(insert);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());
            updates = stmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new DatabaseException();
        } finally {
            if (stmt != null) try {
                stmt.close();
            } catch (SQLException e) {
                throw new DatabaseException();
            }
        }
        return updates;
    }

    @Override
    public int updateUser(String username, String gameID) throws DatabaseException {
        PreparedStatement stmt = null;
        int updates = 0;
        try {
            String update = "UPDATE user SET gameid = ? WHERE username = ?;";
            stmt = connection.prepareStatement(update);
            stmt.setString(1, gameID);
            stmt.setString(2, username);
            updates = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException();
        } finally {
            if (stmt != null) try {
                stmt.close();
            } catch (SQLException e) {
                throw new DatabaseException();
            }
        }
        return updates;
    }

    @Override
    public User getUser(String username) throws DatabaseException {
        User user = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM user WHERE username = ?";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
                user.setGame(rs.getString(5));
            }
        } catch (SQLException e) {
            throw new DatabaseException();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                throw new DatabaseException();
            }
        }
        return user;
    }

    @Override
    public int clear() throws DatabaseException {
        PreparedStatement stmt = null;
        int updates = 0;
        try {
            String clear = "DELETE FROM user;";
            stmt = connection.prepareStatement(clear);
            updates = stmt.executeUpdate();
        } catch(SQLException e) {
            throw new DatabaseException();
        } finally {
            if (stmt != null) try {
                stmt.close();
            } catch (SQLException e) {
                throw new DatabaseException();
            }
        }
        return updates;
    }

}
