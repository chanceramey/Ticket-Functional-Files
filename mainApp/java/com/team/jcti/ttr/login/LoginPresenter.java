package com.team.jcti.ttr.login;



import android.app.Activity;

import com.team.jcti.ttr.IPresenter;
import com.team.jcti.ttr.models.ClientModel;
import com.team.jcti.ttr.models.ClientModel;

/**
 * Created by Jeff on 2/2/2018.
 */

public class LoginPresenter implements ILoginPresenter{
public class LoginPresenter implements ILoginPresenter, IPresenter {

    private ClientModel mClientModel = ClientModel.getInstance();
<<<<<<< HEAD:mainApp/java/com/team/jcti/ttr/login/LoginPresenter.java
    private ServerProxy mServerProxy = ServerProxy.getInstance();

    private LoginActivity mLoginActivity;
    String BAD_PASSWORD = "Invalid Password";
    String BAD_USERNAME = "Invalid Username";
    String BAD_IPADDRESS = "Invalid IPAddress";
    String BAD_PORT = "Invalid Port Number";
    ArrayList<String> inputErrors;

    public LoginPresenter(LoginActivity loginActivity){

        mClientModel.setActivePresenter(this);
        this.mLoginActivity = loginActivity;

    }

    @Override
    public void onLogin() {
        mLoginActivity.onLogin();
    }

    @Override
    public void onRegister() {
        mLoginActivity.onRegister();
    }

    @Override
    public void login(String ipAddress, String port, String username, String password) {
	inputErruros = new ArrayList<>();


        //validate login credential format
        if (!ipAddress.matches(mClientModel.getProperForms().get("ipAddress"))){
            inputErrors.add(BAD_IPADDRESS);
        }
        if (!port.matches(mClientModel.getProperForms().get("portNumber"))){
            inputErrors.add(BAD_PORT);
        }
        if (!username.matches(mClientModel.getProperForms().get("username"))){
            inputErrors.add(BAD_USERNAME);
        }
        if (!password.matches(mClientModel.getProperForms().get("password"))){
            inputErrors.add(BAD_PASSWORD);
        }

        if(inputErrors.size() > 0) {
            //create toast
            mLoginActivity.displayErrorMessages(inputErrors);

        } else {

            //send to serverProxy
            mServerProxy.login(username, password);


        }

=======
    private LoginActivity mLoginActivity = new LoginActivity();
    @Override
    public void login(String username, String password) {
>>>>>>> master:main/java/com/team/jcti/ttr/login/LoginPresenter.java

    }

    @Override
<<<<<<< HEAD:mainApp/java/com/team/jcti/ttr/login/LoginPresenter.java
    public void register(String ipAddress, String port, String username, String password, String firstName, String lastName) {
	inputErruros = new ArrayList<>();


        //validate login credential format
        if (!ipAddress.matches(mClientModel.getProperForms().get("ipAddress"))){
            inputErrors.add(BAD_IPADDRESS);
        }
        if (!port.matches(mClientModel.getProperForms().get("portNumber"))){
            inputErrors.add(BAD_PORT);
        }
        if (!username.matches(mClientModel.getProperForms().get("username"))){
            inputErrors.add(BAD_USERNAME);
        }
        if (!password.matches(mClientModel.getProperForms().get("password"))){
            inputErrors.add(BAD_PASSWORD);
        }

        if(inputErrors.size() > 0) {
            //create toast
            mLoginActivity.displayErrorMessages(inputErrors);


        } else {

            //send to serverProxy
            mServerProxy.register(username, password, firstName, lastName);

        }
=======
    public void register(String username, String password) {
>>>>>>> master:main/java/com/team/jcti/ttr/login/LoginPresenter.java

    }


    @Override
    public void displayError(String message) {
        mLoginActivity.toast(message);
    }

}