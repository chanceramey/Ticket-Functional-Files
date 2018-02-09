package com.team.jcti.ttr.login;

/**
 * Created by Jeff on 2/2/2018.
 */

public interface ILoginActivity {
    void onLogin(String username, String password);
    void onRegister(String username, String password);
}
