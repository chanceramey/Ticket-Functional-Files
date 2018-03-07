package com.team.jcti.ttr.communication;

import com.team.jcti.ttr.models.ClientGameModel;
import com.team.jcti.ttr.models.ClientModel;

import java.util.TimerTask;

/**
 * Created by Jeff on 2/2/2018.
 */

public class Poller extends TimerTask{
    private ClientModel mClientModel;
    private ClientGameModel mGameModel;
    private ServerProxy mServerProxy = ServerProxy.getInstance();

    public Poller(ClientModel cm) {
        this.mClientModel = cm;
    }

    @Override
    public void run() {
        if (mClientModel.getAuthToken() == null) return;
        if(!mGameModel.isActive()) mServerProxy.getCommands(mClientModel.getAuthToken());
        else mServerProxy.getGameCommands(mGameModel.getGameID(), mGameModel.getGameHistoryPosition());
    }
}
