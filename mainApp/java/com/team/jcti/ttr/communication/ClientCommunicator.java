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
public class ClientCommunicator {
    private static ClientCommunicator SINGLETON;
    public static ClientCommunicator getInstance() {
        if (SINGLETON == null) {
            SINGLETON = new ClientCommunicator();
        }
        return SINGLETON;
    }

    private Gson gson = new Gson();

    public ClientCommunicator() {}

    private HttpURLConnection openConnection(String contextDesignator) {
        HttpURLConnection result = null;
        try {
            URL url = new URL(URL_PREFIX + contextDesignator);
            result = (HttpURLConnection) url.openConnection();
            result.setRequestMethod("POST");
            result.setDoOutput(true);
            result.setConnectTimeout(1000); //connect timeOut after 1 second
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

    protected Object sendCommand(Command command) {
        HttpURLConnection connection = openConnection("/command");
        sendToServer(connection, command);
        Object result = getResult(connection);
        if(result == null) {
            Command[] commands = {createErrorCommand("Could not connect with Server")};
            result = commands;
        }
        return result;
    }

    private Command createErrorCommand(String message) {
        String CLIENT_TARGET = "com.team.jcti.ttr.communication.ClientFacade";
        String[] paramTypes = {message.getClass().getName()};
        Object[] params = {message};
        return new Command(CLIENT_TARGET, "displayError", paramTypes, params);
    }

    private static final String SERVER_HOST = "10.24.64.105";
    private static final int SERVER_PORT = 8080;
    private static final String URL_PREFIX = "http://" + SERVER_HOST + ":" + SERVER_PORT;
}
