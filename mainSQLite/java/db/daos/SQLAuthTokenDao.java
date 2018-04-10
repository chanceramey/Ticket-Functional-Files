package db.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.AbstractDaoFactory.DatabaseException;
import database.IAuthTokenDao;
import model.AuthToken;

/**
 * Created by tjense25 on 4/9/18.
 */

public class SQLAuthTokenDao extends IAuthTokenDao {

    private Connection connection;

    public SQLAuthTokenDao(Connection connection) throws DatabaseException {
        this.connection = connection;
        PreparedStatement stmt = null;
        try {
            String create = "CREATE TABLE IF NOT EXISTS authtoken (token TEXT PRIMARY KEY NOT NULL, " +
                    "user TEXT NOT NULL, timestamp DATETIME DEFAULT CURRENT_TIMESTAMP);";
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
    public void addAuth(AuthToken auth) throws DatabaseException {
        PreparedStatement stmt = null;
        try {
            String insert = "INSERT INTO authtoken (token, user) VALUES (?, ?);";
            stmt = connection.prepareStatement(insert);
            stmt.setString(1, auth.getToken());
            stmt.setString(2, auth.getUser());
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
    public void deleteAuth(String token) throws DatabaseException {
        PreparedStatement stmt = null;
        try {
            String delete = "DELETE FROM authtoken WHERE token = ?;";
            stmt = connection.prepareStatement(delete);
            stmt.setString(1, token);
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
    public String getUsername(String token) throws DatabaseException {
        PreparedStatement stmt = null;
        String userName = null;
        ResultSet rs = null;
        try {
            String query = "SELECT user FROM authtoken WHERE token = ?;";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, token);
            rs = stmt.executeQuery();
            if(rs.next()) {
                userName = rs.getString(1);
            }
        } catch (SQLException e) {
            throw new DatabaseException();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (rs != null) rs.close();
            } catch (SQLException e) {
                throw new DatabaseException();
            }
        }
        return userName;

    }

    @Override
    public int clear() throws DatabaseException {
        PreparedStatement stmt = null;
        int updates = 0;
        try {
            String delete = "DELETE FROM authtoken;";
            stmt = connection.prepareStatement(delete);;
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


}
