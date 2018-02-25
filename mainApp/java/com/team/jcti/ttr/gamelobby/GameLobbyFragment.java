package com.team.jcti.ttr.gamelobby;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.team.jcti.ttr.R;
import com.team.jcti.ttr.models.ClientModel;

/**
 * Created by Tanner Jensen on 2/4/2018.
 */

public class GameLobbyFragment extends Fragment{
    private GameLobbyPresenter mGameLobbyPresenter;
    private TextView mGameNameText;
    private TextView mNumPlayersText;
    private TextView mPlayersNamesText;
    private Button mLeaveGameButton;
    private Button mStartGameButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_game_lobby, container, false);

        this.mGameLobbyPresenter = (GameLobbyPresenter) ClientModel.getInstance().getActivePresenter();
        mGameLobbyPresenter.setFragment(this);

        mGameNameText = (TextView) v.findViewById(R.id.game_lobby_name);
        mNumPlayersText = (TextView) v.findViewById(R.id.game_lobby_numPlayers);
        mPlayersNamesText = (TextView) v.findViewById(R.id.player_names);
        updateGameInfo();

        mLeaveGameButton = (Button) v.findViewById(R.id.leave_game_button);
        mLeaveGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onLeaveButtonPressed();
            }
        });

        mStartGameButton = (Button) v.findViewById(R.id.start_game_button);
        mStartGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStartButtonPressed();
            }
        });


        return v;
    }

    public void updateGameInfo() {
        mGameNameText.setText(mGameLobbyPresenter.getGame().getGameName());
        mNumPlayersText.setText(mGameLobbyPresenter.getGame().getNumPlayersString());
        mPlayersNamesText.setText(mGameLobbyPresenter.getGame().getPlayersNames());
    }

    private void onStartButtonPressed() {
        if(mGameLobbyPresenter.isHost()) {
            int currentPlayers = mGameLobbyPresenter.getGame().getPlayers().size();
            int maxPlayers = mGameLobbyPresenter.getGame().getNumPlayers();
            if (currentPlayers == 1) {
                Toast.makeText(getActivity(), "Must have at least two players to start a game", Toast.LENGTH_SHORT).show();
            } else if (currentPlayers < maxPlayers) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(String.format("Are you sure you want to start the game with only %d players", currentPlayers));
                builder.setCancelable(true);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(),"Starting Game!", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create().show();
            }
            else {
                Toast.makeText(getActivity(),"Starting Game!", Toast.LENGTH_SHORT).show();
                startGame();
            }
        } else {
            Toast.makeText(getActivity(), "Only the host can start the game", Toast.LENGTH_LONG).show();
        }
    }

    private void onLeaveButtonPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to leave this game? If you are the host, leaving this game will permanently delete game.");
        builder.setCancelable(true);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Leave Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mGameLobbyPresenter.leaveGame();
                Toast.makeText(getActivity(),"Leaving Game!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }

    private void startGame() {
        mGameLobbyPresenter.startGame();
    }

}
