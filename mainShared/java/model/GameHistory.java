package model;

/**
 * Created by Jeff on 2/27/2018.
 */

public class GameHistory {
    String mUser;
    String mMessage;

    public String getUser() {
        return mUser;
    }

    public void setUser(String user) {
        mUser = user;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public GameHistory(String user, String message) {
        this.mUser = user;
        this.mMessage = message;
    }

    public  String getPlayerName() {
        return mUser;
    }

    public String toString() {
        return mMessage;
    }
}
