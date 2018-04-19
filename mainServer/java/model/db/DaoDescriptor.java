package model.db;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Chance on 4/14/18.
 */

class DaoDescriptor {

    private String mName;
    private File mJar;
    private String mClassName;
    private String mDescription;
    private String filePath;

    public DaoDescriptor(String mName, String mJar, String mClassName, String mDescription, String filePath) {
        this.mName = mName;
        this.mJar = new File(mJar);
        this.mClassName = mClassName;
        this.mDescription = mDescription;
        this.filePath = filePath;
    }

    public URL getJarURL() throws MalformedURLException {
        return mJar.toURL();
    }

    public String getClassName() {
        return mClassName;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getName() {
        return mName;
    }

    public String getFilePath() {
        return filePath;
    }
}
