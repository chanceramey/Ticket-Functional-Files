package db.daos;

import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.AbstractDaoFactory.DatabaseException;
import database.IGameDao;
import interfaces.IGame;

/**
 * Created by tjense25 on 4/9/18.
 */

public class SQLGameDao extends IGameDao {

    Connection connection;
    Gson gson = new Gson();

    public SQLGameDao(Connection connection) throws DatabaseException {
        this.connection = connection;
        PreparedStatement stmt = null;
        try {
            String create = "CREATE TABLE IF NOT EXISTS game (gameid TEXT NOT NULL PRIMARY KEY, json TEXT NOT NULL);";
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
    public void addGame(IGame game) throws DatabaseException {
        PreparedStatement stmt = null;
         try {
             String insert = "INSERT INTO game (gameid, json) VALUES (?, ?);";
             stmt = connection.prepareStatement(insert);
             stmt.setString(1, game.getID());
             stmt.setString(2, gson.toJson(game));
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
    public void updateGame(IGame game) throws DatabaseException {
        PreparedStatement stmt = null;
        try {
            String update = "UPDATE game SET json = ? WHERE gameid = ?;";
            stmt = connection.prepareStatement(update);
            stmt.setString(1, gson.toJson(game));
            stmt.setString(2, game.getID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
    public String getGame(String gameID) throws DatabaseException {
        String json = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT json FROM game WHERE gameid = ?;";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, gameID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                json = rs.getString(1);
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
        return json;
    }

    @Override
    public void deleteGame(String gameID) throws DatabaseException {
        PreparedStatement stmt = null;
        try {
            String delete = "DELETE FROM game WHERE gameid = ?;";
            stmt = connection.prepareStatement(delete);
            stmt.setString(1, gameID);
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
    public int clear() throws DatabaseException {
        PreparedStatement stmt = null;
        int updates = 0;
        try {
            String delete = "DELETE FROM game;";
            stmt = connection.prepareStatement(delete);
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
