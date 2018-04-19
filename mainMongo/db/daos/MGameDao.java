package db.daos;

import com.google.gson.Gson;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;

import database.AbstractDaoFactory;
import database.IGameDao;
import database.IUserDao;
import interfaces.IGame;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by Jeff on 4/11/2018.
 */
public class MGameDao extends IGameDao {
    MongoCollection<Document> mCollection;
    Gson gson = new Gson();

    public MGameDao(MongoCollection<Document> collection) {
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
    public void addGame(IGame game) throws AbstractDaoFactory.DatabaseException {
        try {
            Document doc = new Document("gameId", game.getID())
                    .append("game", gson.toJson(game));
            mCollection.insertOne(doc);
        } catch(MongoException e) {
            throw new AbstractDaoFactory.DatabaseException();
        }

    }

    @Override
    public void updateGame(IGame game) throws AbstractDaoFactory.DatabaseException {
        try {
            mCollection.updateOne(eq("gameId", game.getID()), new Document("$set", new Document("game", gson.toJson(game))));
        } catch(MongoException e) {
            throw new AbstractDaoFactory.DatabaseException();
        }
    }

    @Override
    public String getGame(String gameID) throws AbstractDaoFactory.DatabaseException {
        Document doc = mCollection.find(eq("gameId", gameID)).first();
        String game = (String) doc.get("game");
        return game;
    }

    @Override
    public void deleteGame(String gameID) throws AbstractDaoFactory.DatabaseException {
        try {
            mCollection.deleteOne(eq("gameId", gameID));
        } catch (MongoException e) {
            throw new AbstractDaoFactory.DatabaseException();
        }
    }

    @Override
    public List<String> getWaitingGames(String className) throws AbstractDaoFactory.DatabaseException {
        MongoCursor<Document> cursor = mCollection.find(eq("class", className)).iterator();
        try {
            List<String> games = new ArrayList<>();
            Document doc;
            if (!cursor.hasNext()) return null;
            while (cursor.hasNext()) {
                doc = cursor.next();
                String tempClassName = (String) doc.get("class");
                String tempJson = (String) doc.get("game");
                games.add(tempClassName);
                games.add(tempJson);
            }
            return games;
        } catch (MongoException e) {
            throw new AbstractDaoFactory.DatabaseException();
        } finally {
            cursor.close();
        }

    }

}
