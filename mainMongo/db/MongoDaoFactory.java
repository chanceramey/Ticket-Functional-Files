package db;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoException;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;

import database.AbstractDaoFactory;
import db.daos.MAuthTokenDao;
import db.daos.MCommandsDao;
import db.daos.MGameDao;
import db.daos.MUserDao;
import jdk.nashorn.internal.runtime.ECMAErrors;

/**
 * Created by Jeff on 4/10/2018.
 */

public class MongoDaoFactory extends AbstractDaoFactory {
    public MongoDaoFactory() throws MongoException {
        try {
//            MongoClient mongoClient = new MongoClient();
//            MongoDatabase database = mongoClient.getDatabase("ticketDB"); // name of database
//            DBCollection authCol = database.getCollection("authCollection");
//            DBCollection commandCol = database.getCollection("commandCollection");
//            DBCollection gameCol = database.getCollection("gameCollection");
//            DBCollection userCol = database.getCollection("userCollection");
//            MongoCredential credential;
//            credential = MongoCredential.createCredential("sampleUser", "myDb", "password".toCharArray());
        } catch (MongoException e) {
        }
    }

    @Override
    public void startTransaction() throws DatabaseException {

    }

    @Override
    public void commitTransaction() throws DatabaseException {

    }

    @Override
    public void rollbackTransaction() throws DatabaseException {

    }

    public void instantiateDaos() {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("ticketDB"); // name of database
        System.out.println("Connected to Mongo Server");
        setUserDao(new MUserDao(database.getCollection("user")));
        setCommandsDao(new MCommandsDao(database.getCollection("command")));
        setAuthTokenDao(new MAuthTokenDao(database.getCollection("auth")));
        setGameDao(new MGameDao(database.getCollection("game")));
    }

}
