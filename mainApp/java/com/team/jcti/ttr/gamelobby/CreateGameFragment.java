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
import com.team.jcti.ttr.models.ClientModel;

/**
 * Created by Tanner Jensen on 2/4/2018.
 */

public class CreateGameFragment extends Fragment {
    private GameLobbyPresenter gameLobbyPresenter;

    private EditText gameNameField;
    private RadioButton numPlayersButton;
    private RadioGroup numPlayersGroup;
    private Button createGameButton;
    private Button addComputerPlayerButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_create_game, container, false);

        this.gameLobbyPresenter = (GameLobbyPresenter) ClientModel.getInstance().getActivePresenter();

        gameNameField = (EditText) v.findViewById(R.id.game_name_field);
        numPlayersGroup = (RadioGroup) v.findViewById(R.id.num_players_group);

        createGameButton = (Button) v.findViewById(R.id.create_game_button);

        createGameButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String gameName = gameNameField.getText().toString();
                if(numPlayersGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getActivity(), "Please select number of players", Toast.LENGTH_SHORT).show();
                    return;
                };
                int numPlayersId = numPlayersGroup.getCheckedRadioButtonId();
                numPlayersButton = (RadioButton) v.findViewById(numPlayersId);

                int numPlayer = 0;
                switch(numPlayersButton.getText().toString()) {
                    case "2": numPlayer = 2; break;
                    case "3": numPlayer = 3; break;
                    case "4": numPlayer = 4; break;
                    case "5": numPlayer = 5; break;
                    default: numPlayer = 0; break;
                }
                gameLobbyPresenter.createNewGame(numPlayer, gameName);
            }
        });

        return v;
    }
}
