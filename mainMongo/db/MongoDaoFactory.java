package db;

import com.mongodb.ClientSessionOptions;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.ListDatabasesIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoIterable;
import com.mongodb.session.ClientSession;

import org.bson.Document;

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
    public MongoDaoFactory() throws DatabaseException{
        System.out.println("Initialize Mongo Dao Factory");
        try {
            instantiateDaos();
        } catch (Exception e) {
            e.printStackTrace();
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

    public void instantiateDaos() throws DatabaseException{
        System.out.println("Instantiating Mongo Daos");
        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("ticketDB"); // name of database
        System.out.println("Connected to Mongo Server");
        setUserDao(new MUserDao(database.getCollection("user")));
        setCommandsDao(new MCommandsDao(database.getCollection("command")));
        setAuthTokenDao(new MAuthTokenDao(database.getCollection("auth")));
        setGameDao(new MGameDao(database.getCollection("game")));
    }

}
