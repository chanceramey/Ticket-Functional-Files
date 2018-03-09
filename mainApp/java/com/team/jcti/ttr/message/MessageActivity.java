package com.team.jcti.ttr.message;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.team.jcti.ttr.R;

import java.util.List;

import model.GameHistory;

public class MessageActivity extends AppCompatActivity implements IMessageActivity {
    MessagePresenter mPresenter;
    Button mSendButton;
    EditText mMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        mPresenter = new MessagePresenter(this);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container_message);

        if (fragment == null) {
            fragment = new MessageFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container_message, fragment)
                    .commit();
        }
//        mPresenter.update();
        mMessage = (EditText) findViewById(R.id.chat_message);
        mMessage.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                        keyCode == KeyEvent.KEYCODE_ENTER) {
                    sendMessage();
                    return true;
                }
                return false;
            }
        });

        mSendButton = (Button) findViewById(R.id.send_message_button);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

    }

    @Override
    public void sendMessage() {
        if (mMessage != null) {
            mPresenter.sendMessage(mMessage.getText().toString());
            mMessage.setText("");
        } else {} // do nothing if there is no message typed out
    }

    @Override
    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}
