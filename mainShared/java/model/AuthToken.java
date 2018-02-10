package model;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by tjense25 on 2/2/18.
 */

public class AuthToken {

    private String user;
    private String token;
    private Calendar time_created;

    /**
     * AuthToken Constructor
     * @param user username of User who has this authToken
     * @param token UUID string of authToken ID
     */
    public AuthToken(String user, String token) {
        this.token = token;
        this.user = user;
        this.time_created = Calendar.getInstance();  //Initialize date to the current time
    }

    public String getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    public Calendar getTimeCreated() {
        return time_created;
    }
}
