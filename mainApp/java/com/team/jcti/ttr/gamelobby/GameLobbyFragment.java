package com.team.jcti.ttr.gamelobby;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.team.jcti.ttr.R;

/**
 * Created by Tanner Jensen on 2/4/2018.
 */

public class GameLobbyFragment extends Fragment{
    private GameLobbyPresenter gameLobbyPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_game_lobby, container, false);

        gameLobbyPresenter = new GameLobbyPresenter((GameLobbyActivity) getActivity());


        return v;
    }
}
