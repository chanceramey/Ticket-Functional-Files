package com.team.jcti.ttr.communication;

import android.os.AsyncTask;

import command.Command;

/**
 * Created by Jeff on 2/2/2018.
 */

public class SendCommandTask extends AsyncTask<Command, Void, Object>{
    ClientCommunicator mClientCommunicator = ClientCommunicator.getInstance();

    @Override
    protected Object doInBackground(Command... commands) {
        Command command = commands[0];
        Object result = mClientCommunicator.sendCommand(command);
        return result;
    }

    @Override
    protected void onPostExecute(Object commands) {
        Command[] responseCommands = (Command[]) commands;
        for (int i = 0; i < responseCommands.length; i++) {
            responseCommands[i].execute();
        }
    }
}
