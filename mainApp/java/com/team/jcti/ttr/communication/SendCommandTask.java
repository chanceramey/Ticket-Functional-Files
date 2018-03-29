package com.team.jcti.ttr.communication;

import android.os.AsyncTask;

import com.team.jcti.ttr.models.ClientGameModel;

import command.Command;
import command.ResultTransferObject;

/**
 * Created by Jeff on 2/2/2018.
 */

public class SendCommandTask extends AsyncTask<Command, Void, Object>{
    ClientCommunicator mClientCommunicator = ClientCommunicator.getInstance();

    @Override
    protected Object doInBackground(Command... commands) {
        Command command = commands[0];
        return mClientCommunicator.sendCommand(command);
    }

    @Override
    protected void onPostExecute(Object result) {
        ResultTransferObject transferObject = (ResultTransferObject) result;
        Command[] responseCommands = (Command[]) transferObject.getResult();
        for (Command command : responseCommands) {
                command.execute();
        }
    }

}
