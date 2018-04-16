package database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.lang.reflect.*;

/**
 * Created by Chance on 4/11/18.
 */

public class DaoFactoryRegistry {
    Map<String, DaoDescriptor> plugins = new HashMap<>();
    String choice = null;

    public DaoFactoryRegistry(){
        parseConfig();
    }

    private void parseConfig() {

        try {

            BufferedReader reader = new BufferedReader(
                    new FileReader(
                            new File("shared/src/main/java/config/database.cfg")));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Use") || line.startsWith("use")) {
                    String[] col = line.trim().split(" ");
                    if (col[1] != null) {
                        choice = col[1];
                    }
                }
                else if (!line.startsWith("#")) {
                    String[] col = line.trim().split("--");
                    for (int i = 0; i < col.length; i++) {
                        plugins.put(col[0], new DaoDescriptor(col[0], col[1], col[2]));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getChoice() {
        return choice;
    }

    public Set<String> getAvailablePlugins() {
        return plugins.keySet();
    }

    public AbstractDaoFactory registerPlugin(String name) throws Exception{

        if (name == null) throw new ConfigFileSyntaxException();
        try {
            Class<?> klass = Class.forName(plugins.get(name).mClassName);
            return (AbstractDaoFactory) klass.newInstance();
        } catch (ClassNotFoundException e) {
            System.out.println(plugins.get(name).mClassName + " is not a class");
            e.printStackTrace();
        } catch (InstantiationException e) {
            System.out.println(plugins.get(name).mClassName + " could not be instantiated");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.out.println(plugins.get(name).mClassName + " could not be accessed");
            e.printStackTrace();
        }

        return null;
    }

    public class ConfigFileNotFoundException extends Exception{

    }

    public class ConfigFileSyntaxException extends Exception {}

}
