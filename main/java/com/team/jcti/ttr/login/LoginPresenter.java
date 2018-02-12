package com.team.jcti.ttr.login;



import android.app.Activity;

import com.team.jcti.ttr.IPresenter;
import com.team.jcti.ttr.models.ClientModel;
import com.team.jcti.ttr.models.ClientModel;

/**
 * Created by Jeff on 2/2/2018.
 */

public class LoginPresenter implements ILoginPresenter{

    private ClientModel mClientModel = ClientModel.getInstance();
    private LoginActivity mLoginActivity = new LoginActivity();
    @Override
    public void login(String username, String password) {

    }

    @Override
    public void register(String username, String password) {

    }



}
