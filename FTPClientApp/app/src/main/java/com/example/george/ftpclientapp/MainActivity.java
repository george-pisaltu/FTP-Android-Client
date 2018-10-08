package com.example.george.ftpclientapp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        appContext = getApplicationContext();
        ExplorerActivity.ftpClient = ftp;
        ExplorerActivity.pathToDownloadLocation =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/";

        isConnecting = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(ftp.isConnected()) {
            try {
                ftp.disconnect();
            } catch (IOException e) {
                ShowToast("Unknown error");
            }
        }

        Button connectBtn = (Button) findViewById(R.id.connectButton);
        ProgressBar prgBar = (ProgressBar) findViewById(R.id.progressBar);
        prgBar.setVisibility(View.GONE);

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                server = findViewById(R.id.serverNameEditText);
                user = findViewById(R.id.usernameEditText);
                pass = findViewById(R.id.passwordEditText);

                CustomNode.root = new CustomDirectory();

                if(!isConnecting) {
                    NetworkingThread connectTh = new NetworkingThread();
                    connectTh.execute("");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new DisconnectThread().execute();
        super.onBackPressed();
    }

    public void ShowToast(String str) {
        Toast.makeText(getBaseContext(), str, Toast.LENGTH_LONG).show();
    }

    public EditText server;
    public EditText user;
    public EditText pass;
    public FTPClient ftp = new FTPClient();
    public ArrayList<String> pathsToDownload;
    public Context appContext;
    public boolean isConnecting;



    private class NetworkingThread extends AsyncTask<String, Integer, CustomNode> {

        @Override
        protected CustomNode doInBackground(String... strings) {
            try {
                try {
                    ftp.connect(server.getText().toString());
                } catch (UnknownHostException e) {
                    error = e.getMessage();
                    return null;
                }
                int reply = ftp.getReplyCode();

                if (!FTPReply.isPositiveCompletion(reply))
                {
                    ftp.disconnect();
                    error = "FTP server refused connection.";
                    return null;
                }

                if(!ftp.login(user.getText().toString(), pass.getText().toString())) {
                    ftp.logout();
                    ftp.disconnect();
                    error = "User or password incorrect";
                    return null;
                }
            } catch (IOException e) {
                error = "Error connecting to the server";
                return null;
            }

            try {
                if (ftp.isConnected()) {
                    CustomNode.ftpClient = ftp;
                    CustomNode.root.expandNodes();
                }
            } catch (Exception e) {
                error = "Error 404";
                return null;
            }
            return CustomNode.root;
        }

        protected void onPostExecute(CustomNode root) {
            ProgressBar prgBar = (ProgressBar) findViewById(R.id.progressBar);
            prgBar.setProgress(0);
            prgBar.setVisibility(View.GONE);
            isConnecting = false;
            if(error.isEmpty())
            {
                Intent startIntent = new Intent(appContext, ExplorerActivity.class);
                startActivity(startIntent);
            } else {
                ShowToast(error);
            }
        }

        protected void onPreExecute() {
            ProgressBar prgBar = (ProgressBar) findViewById(R.id.progressBar);
            prgBar.setProgress(100);
            prgBar.setVisibility(View.VISIBLE);
        }


        public String error = "";
    }



}
