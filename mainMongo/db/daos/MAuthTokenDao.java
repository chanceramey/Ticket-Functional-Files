package db.daos;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;

import org.bson.Document;

import database.AbstractDaoFactory;
import database.IAuthTokenDao;
import model.AuthToken;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by Jeff on 4/11/2018.
 */

public class MAuthTokenDao extends IAuthTokenDao {
    MongoCollection<Document> mCollection;
    public MAuthTokenDao(MongoCollection<Document> collection) {
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
    public void addAuth(AuthToken auth) throws AbstractDaoFactory.DatabaseException {
        try {
            Document doc = new Document("token", auth.getToken())
                    .append("user", auth.getUser());
            mCollection.insertOne(doc);
        } catch(MongoException e) {
            throw new AbstractDaoFactory.DatabaseException();
        }

    }

    @Override
    public void deleteAuth(String token) throws AbstractDaoFactory.DatabaseException {
        try {
            DeleteResult deleteResult = mCollection.deleteOne(eq("token", token));
            System.out.println(deleteResult);
        } catch (MongoException e) {
            throw new AbstractDaoFactory.DatabaseException();
        }
    }

    @Override
    public String getUsername(String token) throws AbstractDaoFactory.DatabaseException {
        try {FindIterable<Document> docList = mCollection.find(eq("token", token));
            Document doc = docList.first();
            if (doc == null) return null;
            String username = (String) doc.get("user");
            return username;
        } catch (MongoException e) {
            throw new AbstractDaoFactory.DatabaseException();
        }
    }
}
