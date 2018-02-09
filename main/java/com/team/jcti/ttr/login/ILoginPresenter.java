package com.team.jcti.ttr.login;


/**
 * Created by Jeff on 2/2/2018.
 */

public interface ILoginPresenter {
    void login(String username, String password);
    void register(String username, String password);
}
