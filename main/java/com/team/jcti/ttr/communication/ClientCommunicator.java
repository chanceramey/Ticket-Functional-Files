package com.team.jcti.ttr.communication;


import android.icu.util.Output;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.team.jcti.ttr.models.ClientModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import command.Command;
import command.ResultTransferObject;

/**
 * Created by Jeff on 2/2/2018.
 */

public class ClientCommunicator extends AsyncTask<Command, Void, Object> {

    private Gson gson = new Gson();

    public ClientCommunicator() {}

    private HttpURLConnection openConnection(String contextDesignator) {
        HttpURLConnection result = null;
        try {
            URL url = new URL(URL_PREFIX + contextDesignator);
            result = (HttpURLConnection) url.openConnection();
            result.setRequestMethod("POST");
            result.setDoOutput(true);
            result.connect();
        } catch (MalformedURLException e) {
            System.err.println("Malformed URL Error");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void sendToServer(HttpURLConnection connection, Command command) {
        try {
            String json = gson.toJson(command);
            OutputStream reqBody = connection.getOutputStream();
            writeString(json, reqBody);
            reqBody.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

    private Object getResult(HttpURLConnection connection) {
        Object result = null;
        try {
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = connection.getInputStream();
                ResultTransferObject transferObject = (ResultTransferObject)
                        gson.fromJson(new InputStreamReader(respBody), ResultTransferObject.class);
                result = transferObject.getResult();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected Object doInBackground(Command... commands) {
        Command command = commands[0];

        HttpURLConnection connection = openConnection("/command");
        sendToServer(connection, command);
        Object result = getResult(connection);

        return result;
    }


    @Override
    protected void onPostExecute(Object result) {
        Command returnCommand = (Command) result;
        returnCommand.execute();
    }

    private static final String SERVER_HOST = "laptop-e1khfita";
    private static final int SERVER_PORT = 8080;
    private static final String URL_PREFIX = "http://" + SERVER_HOST + ":" + SERVER_PORT;
}
