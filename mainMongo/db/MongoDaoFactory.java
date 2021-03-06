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
    MongoClient mongoClient;
    MongoDatabase database;
    String databaseName;
    public MongoDaoFactory() throws DatabaseException{
        System.out.println("Initialize Mongo Dao Factory");
    }

    public MongoDaoFactory(String databaseName) throws MongoException {
        this.databaseName = databaseName;
    }

    @Override
    public void setDBFilePath(String filepath){

    }

    @Override
    public void startTransaction() throws DatabaseException {

    }

    @Override
    public void commitTransaction() throws DatabaseException {
        setUserDao(null);
        setCommandsDao(null);
        setAuthTokenDao(null);
        setGameDao(null);
    }

    @Override
    public void rollbackTransaction() throws DatabaseException {

    }

    public void instantiateDaos() throws DatabaseException{

        mongoClient = new MongoClient();
        database = mongoClient.getDatabase(this.databaseName); // name of database
        System.out.println("Connected to Mongo Server");
        setUserDao(new MUserDao(database.getCollection("user")));
        setCommandsDao(new MCommandsDao(database.getCollection("command")));
        setAuthTokenDao(new MAuthTokenDao(database.getCollection("auth")));
        setGameDao(new MGameDao(database.getCollection("game")));
    }

}
