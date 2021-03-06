package db.daos;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import command.Command;
import database.AbstractDaoFactory;
import database.IAuthTokenDao;
import database.ICommandsDao;
import model.AuthToken;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.gte;

/**
 * Created by Jeff on 4/11/2018.
 */

public class MCommandsDao extends ICommandsDao {
    MongoCollection<Document> mCollection;
    DBCollection mDBCollection;
    Gson gson = new Gson();

    public MCommandsDao(MongoCollection<Document> collection) {
        mCollection = collection;
    }

    @Override
    public int clear() throws AbstractDaoFactory.DatabaseException {
        try {
            DeleteResult deleteResult = mCollection.deleteMany(new Document()); // not sure this works
            Long count = deleteResult.getDeletedCount();  // not sure this will actually show how many were deleted
            return count.intValue();
        } catch (MongoException e) {
            throw new AbstractDaoFactory.DatabaseException();
        }
    }

    @Override
    public void addCommand(String gameID, Command command) throws AbstractDaoFactory.DatabaseException {
        try {

            Document doc = new Document("gameId", gameID)
                                .append("command", gson.toJson(command));
            mCollection.insertOne(doc);


        } catch (MongoException e) {
            throw new AbstractDaoFactory.DatabaseException();
        }

    }

    @Override
    public int getCommandCount(String gameID) throws AbstractDaoFactory.DatabaseException {
        try {
            MongoCursor<Document> cursor = mCollection.find(eq("gameId", gameID)).iterator();
            int numCommands = 0;
            while (cursor.hasNext()) {
                cursor.next();
                numCommands++;
            }
            return numCommands;
        } catch (MongoException e) {
            throw new AbstractDaoFactory.DatabaseException();
        }
    }

    @Override
    public List<Command> getCommands(String gameID) throws AbstractDaoFactory.DatabaseException {
        try {

            List<Command> commands = new ArrayList<>();
            MongoCursor<Document> cursor = mCollection.find().iterator();
            Document doc = new Document();
            while (cursor.hasNext()) {
                doc = cursor.next();
                if (doc.get("gameId").equals(gameID)) {
                    String comJson = (String) doc.get("command");
                    commands.add((Command) gson.fromJson(comJson, Command.class));
                }
            }
            return commands;

        } catch (MongoException e) {
            throw new AbstractDaoFactory.DatabaseException();
        }
    }

    @Override
    public void clearCommands(String gameID) throws AbstractDaoFactory.DatabaseException {
        try {
            mCollection.deleteMany(gte("gameId", gameID));
        } catch (MongoException e) {
            throw new AbstractDaoFactory.DatabaseException();
        }
    }
}

