package model.db;

import database.AbstractDaoFactory;

/**
 * Created by Chance on 4/11/18.
 */

public class PersistenceFacade {
    DaoFactoryRegistry daoFactoryRegistry = new DaoFactoryRegistry();
    AbstractDaoFactory daoFactory;

    public PersistenceFacade(){

        try {
            daoFactory = daoFactoryRegistry.registerPlugin(daoFactoryRegistry.getChoice());
            System.out.println(daoFactory.getClass().getName() + " is ready.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}


