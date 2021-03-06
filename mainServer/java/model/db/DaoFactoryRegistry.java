package model.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import database.AbstractDaoFactory;


/**
 * Created by Chance on 4/11/18.
 */

public class DaoFactoryRegistry {
    private Map<String, DaoDescriptor> plugins = new HashMap<>();
    private String choice = null;
    private int interval = 0;

    public DaoFactoryRegistry(){
        parseConfig();
    }

    private void parseConfig() {

        try {
            System.out.println("Parsing config file...");
            BufferedReader reader = new BufferedReader(
                    new FileReader(
                            new File("server/src/main/java/model/db/database.cfg")));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#")) {
                    // Do nothing. This is a comment.
                } else if (line.toLowerCase().startsWith("use")) {
                    // "use" is key word to identify db selection
                    String[] col = line.trim().split(" ");
                    if (col[1] != null) {
                        choice = col[1];
                        System.out.println(choice + " chosen as database option...");

                    }
                } else if (line.toLowerCase().startsWith("interval")) {
                    // "interval" is key word to declare game database backup interval
                    String[] col = line.trim().split(" ");
                    if (col[1] != null) {
                        try {
                            interval = Integer.parseInt(col[1]);
                        } catch (NumberFormatException e) {
                            System.out.println("Interval provided is not a number");
                            e.printStackTrace();
                        }
                        System.out.println(choice + " chosen as database option...");

                    }
                } else {
                    String[] col = line.trim().split("--");
                    for (int i = 0; i < col.length; i++) {
                        plugins.put(col[0], new DaoDescriptor(col[0], col[1], col[2], col[3]));
                    }
                }
            }

            if (interval == 0) {
                System.out.println("Interval cannot equal zero. Interval is not specified correctly in config file.");
                throw new ConfigFileSyntaxException();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public String getChoice() {
        return choice;
    }

    public int getInterval() {
        return interval;
    }

    public Set<String> getAvailablePlugins() {
        return plugins.keySet();
    }

    public AbstractDaoFactory registerPlugin(String name) throws Exception{

        if (name == null) throw new ConfigFileSyntaxException();
        DaoDescriptor plugin = plugins.get(name);
        try {
            URLClassLoader child = new URLClassLoader(new URL[] {plugin.getJarURL()}, this.getClass().getClassLoader());
            Class<?> klass = Class.forName(plugin.getClassName(), true, child);
            System.out.println(plugin.getClassName() + " class was found. Instantiating...");
            AbstractDaoFactory daoFactory = (AbstractDaoFactory) klass.newInstance();
            daoFactory.setDBFilePath("server/DB/db.sql");
            return daoFactory;
        } catch (ClassNotFoundException e) {
            System.out.println(plugin.getClassName() + " is not a class");
            e.printStackTrace();
        } catch (InstantiationException e) {
            System.out.println(plugin.getClassName() + " could not be instantiated");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.out.println(plugin.getClassName() + " could not be accessed");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            System.out.println("Jar file for " + plugin.getName() + "could not be loaded");
            e.printStackTrace();
        }

        return null;
    }

    public class ConfigFileNotFoundException extends Exception{

    }

    public class ConfigFileSyntaxException extends Exception {}

}
