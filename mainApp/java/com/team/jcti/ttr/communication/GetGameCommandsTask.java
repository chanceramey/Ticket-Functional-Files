package com.team.jcti.ttr.communication;

import android.os.AsyncTask;

import com.team.jcti.ttr.models.ClientGameModel;

import command.Command;
import command.ResultTransferObject;

/**
 * Created by Jeff on 2/2/2018.
 */

public class GetGameCommandsTask extends AsyncTask<Command, Void, Object>{
    ClientCommunicator mClientCommunicator = ClientCommunicator.getInstance();

    @Override
    protected Object doInBackground(Command... commands) {
        Command command = commands[0];
        Object result = mClientCommunicator.sendCommand(command);
        return result;
    }

    @Override
    protected void onPostExecute(Object result) {
        ResultTransferObject transferObject = (ResultTransferObject) result;
        Command[] responseCommands = (Command[]) transferObject.getResult();
        try {
            incrementGameHistoryPosition(transferObject.getGameHistoryPos(), responseCommands.length);
            for (int i = 0; i < responseCommands.length; i++) {
                responseCommands[i].execute();
            }
        } catch (SeenCommandsException e) {
            System.out.println("Already seen these commands");
        }
    }

    protected void incrementGameHistoryPosition(int gameHistoryPos, int numCommands) throws SeenCommandsException {
        if (ClientGameModel.getInstance().isActive()) {
            if (gameHistoryPos < ClientGameModel.getInstance().getGameHistoryPosition()) throw new SeenCommandsException();
            ClientGameModel.getInstance().increment(numCommands);
        }
    }

    private class SeenCommandsException extends Exception {
    }
}
