package com.team.jcti.ttr.login;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.team.jcti.ttr.R;


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
    public void onLogin(String username, String password) {

    }

    @Override
    public void onRegister(String username, String password) {

    }
}
