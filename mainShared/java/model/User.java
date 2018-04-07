package model;

/**
 * Created by Isaak on 2/2/2018.
 */

public class User {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String gameID;

    public User(String username, String password, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gameID = null;
    }

    public void setGame(String gameID) {
        this.gameID = gameID;
    }

    public String getGameID() {
        return this.gameID;
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
