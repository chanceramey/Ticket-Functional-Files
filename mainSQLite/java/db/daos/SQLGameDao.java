package db.daos;

import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.AbstractDaoFactory;
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
            String create = "CREATE TABLE IF NOT EXISTS game (gameid TEXT NOT NULL PRIMARY KEY, class TEXT NOT NULL, json TEXT NOT NULL);";
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
             String insert = "INSERT INTO game (gameid, class, json) VALUES (?, ?, ?);";
             stmt = connection.prepareStatement(insert);
             stmt.setString(1, game.getID());
             stmt.setString(2, game.getClass().getName());
             stmt.setString(3, gson.toJson(game));
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
    public void updateGame(IGame game) throws DatabaseException {
        PreparedStatement stmt = null;
        try {
            String update = "UPDATE game SET json = ?, class = ? WHERE gameid = ?;";
            stmt = connection.prepareStatement(update);
            stmt.setString(1, gson.toJson(game));
            stmt.setString(2, game.getClass().getName());
            stmt.setString(3, game.getID());
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
    public String[] getGame(String gameID) throws DatabaseException {
        String json = null;
        String className = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT class, json FROM game WHERE gameid = ?;";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, gameID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                className = rs.getString(1);
                json = rs.getString(2);
            }
            return new String[] {className, json };
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (rs != null) rs.close();
            } catch (SQLException e) {
                throw new DatabaseException();
            }
        }
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
    public List<String> getWaitingGames(String className) throws DatabaseException {
        List<String> serializedGame = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int updates = 0;
        try {
            String query = "SELECT class, json FROM game WHERE class = ?";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, className);
            rs = stmt.executeQuery();
            while (rs.next()) {
                serializedGame.add(rs.getString(1));
                serializedGame.add(rs.getString(2));
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
        return serializedGame;
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
