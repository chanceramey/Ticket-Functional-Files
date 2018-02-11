package com.team.jcti.ttr.login;


/**
 * Created by Jeff on 2/2/2018.
 */

public interface ILoginPresenter {

    void login(String ipAddress, String port, String username, String password);
    void register(String ipAddress, String port, String username, String password, String firstName, String lastName);
    void onLogin();
    void onRegister();
}
