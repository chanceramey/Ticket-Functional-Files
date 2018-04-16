package database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import database.AbstractDaoFactory;
import database.DaoFactoryRegistry;

/**
 * Created by Chance on 4/11/18.
 */

public class PersistenceFacade {
    DaoFactoryRegistry daoFactoryRegistry = new DaoFactoryRegistry();
    AbstractDaoFactory daoFactory;

    public PersistenceFacade(){

        try {
            daoFactory = daoFactoryRegistry.registerPlugin(daoFactoryRegistry.getChoice());
            System.out.println(daoFactory.getClass().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}


