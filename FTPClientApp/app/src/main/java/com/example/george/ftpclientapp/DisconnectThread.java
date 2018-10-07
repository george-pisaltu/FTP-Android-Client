package com.example.george.ftpclientapp;

import android.os.AsyncTask;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;

public class DisconnectThread extends AsyncTask<Void, Void, Void> {

    public static FTPClient ftpClient;
    public static String error;

    @Override
    protected Void doInBackground(Void... voids) {
        if(ftpClient == null) {
            error = "Invalid use of disconnect";
            return null;
        } else if(!ftpClient.isConnected()) {
            error = "FTP is not connected";
            return null;
        } else {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                error = e.getMessage();
            }
        }

        return null;
    }
}
