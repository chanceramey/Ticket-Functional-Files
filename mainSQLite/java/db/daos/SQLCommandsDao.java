package db.daos;

import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import command.Command;
import database.AbstractDaoFactory.DatabaseException;
import database.ICommandsDao;

/**
 * Created by tjense25 on 4/9/18.
 */

public class SQLCommandsDao extends ICommandsDao {

    private Connection connection;
    private final String createCommand = "CREATE TABLE IF NOT EXISTS commands (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            "gameid TEXT NOT NULL, classname TEXT NOT NULL, obj TEXT NOT NULL, timestamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP);";
    Gson gson = new Gson();

    public SQLCommandsDao(Connection connection) throws DatabaseException {
        this.connection = connection;
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(createCommand);
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
    public void addCommand(String gameID, Command command) throws DatabaseException {
        PreparedStatement stmt = null;
        try {
            String insert = "INSERT INTO commands (gameid, classname, obj) VALUES (?, ?, ?);";
            stmt = connection.prepareStatement(insert);
            stmt.setString(1, gameID);
            stmt.setString(2, command.getClass().getName());
            stmt.setString(3, gson.toJson(command));
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
    public List<Command> getCommands(String gameID) throws DatabaseException {
        List<Command> commands = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT obj, classname FROM commands WHERE gameid = ? ORDER BY timestamp ASC;";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, gameID);
            rs = stmt.executeQuery();
            while(rs.next()) {
                commands.add((Command) gson.fromJson(rs.getString(1), Class.forName(rs.getString(2))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException();
        } catch (ClassNotFoundException e) {
           e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (rs != null) rs.close();
            } catch (SQLException e) {
                throw new DatabaseException();
            }
        }
        return commands;
    }

    @Override
    public void clearCommands(String gameID) throws DatabaseException {
        PreparedStatement stmt = null;
        try {
            String delete = "DELETE FROM commands WHERE gameid = ?;";
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
            String delete = "DELETE FROM commands;";
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
