package db.daos;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;


import org.bson.Document;

import database.AbstractDaoFactory;
import database.IUserDao;
import model.User;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by Jeff on 4/11/2018.
 */

public class MUserDao extends IUserDao {
    MongoCollection<Document> mCollection;
    public MUserDao(MongoCollection<Document> collection) {
        mCollection = collection;
    }

    @Override
    public int clear() throws AbstractDaoFactory.DatabaseException {
        try {
            DeleteResult deleteResult = mCollection.deleteMany(new Document());
            Long count = deleteResult.getDeletedCount();
            return count.intValue();
        } catch (MongoException e) {
            throw new AbstractDaoFactory.DatabaseException();
        }
    }

    @Override
    public int addUser(User user) throws AbstractDaoFactory.DatabaseException {
        try {
            Document doc = new Document("username", user.getUsername())
                    .append("password", user.getPassword())
                    .append("firstname", user.getFirstName())
                    .append("lastname", user.getLastName())
                    .append("gameId", "0");
            mCollection.insertOne(doc);
            return 0; // ???
        } catch (MongoException e) {
            throw new AbstractDaoFactory.DatabaseException();
        }
    }

    @Override
    public int updateUser(String username, String gameID) throws AbstractDaoFactory.DatabaseException {
        try {
            mCollection.updateOne(eq("username", username), new Document("$set", new Document("gameId", gameID)));
            return 0; // ???
        } catch(MongoException e) {
            throw new AbstractDaoFactory.DatabaseException();
        }
    }

    @Override
    public User getUser(String username) throws AbstractDaoFactory.DatabaseException {
        try {
            Document doc = mCollection.find(eq("username", username)).first();
            User user;
            String password = (String) doc.get("password");
            String firstname = (String) doc.get("firstname");
            String lastname = (String) doc.get("lastname");
            String gameId = (String) doc.get("gameId");
            user = new User(username, password, firstname, lastname);
            user.setGame(gameId);
            return user;
        } catch (MongoException e) {
            throw new AbstractDaoFactory.DatabaseException();
        }
    }
}
