package com.team.jcti.ttr.login;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by Jeff on 2/2/2018.
 */

public interface ILoginActivity {
    void onLogin();
    void onRegister();
    void displayErrorMessages(ArrayList<String> errorMessages);
    void onLogin(String username, String password);
    void onRegister(String username, String password);
}
