package com.team.jcti.ttr.login;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.team.jcti.ttr.R;

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
    public void onLogin(String username, String password) {

    }

    @Override
    public void onRegister(String username, String password) {

    }

    @Override
    public void displayErrorMessages(ArrayList<String> errorMessages){
        Toast toast = new Toast(this);
        StringBuilder sbr = new StringBuilder();

        for (String errorMessage: errorMessages) {
            sbr.append(errorMessage);
            sbr.append("\n");
        }

        toast.setText(sbr.toString());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();

    }
}
