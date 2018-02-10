package com.team.jcti.ttr.login;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.team.jcti.ttr.R;
import com.team.jcti.ttr.gamelobby.GameLobbyActivity;

import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity implements ILoginActivity {
    private LoginPresenter mPresenter = new LoginPresenter(this);
    EditText mUsername;
    EditText mPassword;
    EditText mIpAddress;
    EditText mPortNumber;
    EditText mFirstName;
    EditText mLastName;
    Button mLoginButton;
    Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize input fields
        setContentView(R.layout.activity_login);
        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mIpAddress = findViewById(R.id.ipAddress);
        mPortNumber = findViewById(R.id.portNumber);
        mFirstName = findViewById(R.id.firstName);
        mLastName = findViewById(R.id.lastName);
        mLoginButton = findViewById(R.id.loginButton);
        mRegisterButton = findViewById(R.id.registerButton);

        //send login fields data to presenter
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPresenter.login(
                        mIpAddress.getText().toString(),
                        mPortNumber.getText().toString(),
                        mUsername.getText().toString(),
                        mPassword.getText().toString());
            }
        });

        //send register fields data to presenter
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.register(mIpAddress.getText().toString(),
                        mPortNumber.getText().toString(),
                        mUsername.getText().toString(),
                        mPassword.getText().toString(),
                        mFirstName.getText().toString(),
                        mLastName.getText().toString());
            }
        });


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
