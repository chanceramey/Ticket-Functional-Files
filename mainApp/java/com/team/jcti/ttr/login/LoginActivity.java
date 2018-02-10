package com.team.jcti.ttr.login;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.team.jcti.ttr.R;
import com.team.jcti.ttr.gamelobby.GameLobbyActivity;


public class LoginActivity extends AppCompatActivity implements ILoginActivity {
    private LoginPresenter mPresenter = new LoginPresenter();
    EditText mUsername;
    EditText mPassword;
    EditText mIpAdress;
    Button mLoginButton;
    Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void onLogin() {
        toast("Login Successful");

        Intent intent = new Intent(this, GameLobbyActivity.class);
        startActivity(intent);

    }

    @Override
    public void onRegister() {
        toast("Register Successful");
        Intent intent = new Intent(this, GameLobbyActivity.class);
        startActivity(intent);

    }


    @Override
    public void displayErrorMessages(ArrayList<String> errorMessages){
        StringBuilder sbr = new StringBuilder();

        for (String errorMessage: errorMessages) {
            sbr.append(errorMessage);
            sbr.append("\n");
        }

        toast(sbr.toString());

    }

    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
