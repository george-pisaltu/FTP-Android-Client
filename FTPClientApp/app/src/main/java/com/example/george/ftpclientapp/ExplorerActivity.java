package com.example.george.ftpclientapp;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.UnknownHostException;

public class ExplorerActivity extends AppCompatActivity {

    ListView explorerListView;
    CustomNode explorerRoot;
    CustomDirectory currentDir;
    CustomNode targetedFile;
    Context thisContext;

    boolean isDownloading;
    boolean permissionGranted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisContext = this;
        setContentView(R.layout.activity_explorer);
        explorerListView = (ListView) findViewById(R.id.explorerListView);
        explorerRoot = CustomNode.root;
        currentDir = (CustomDirectory) explorerRoot;
        isDownloading = false;

        ProgressBar downloadProgressBar = (ProgressBar) findViewById(R.id.downloadProgressBar);
        downloadProgressBar.setProgress(100);
        downloadProgressBar.setVisibility(View.GONE);

        NodeAdapter adapter = new NodeAdapter(this, currentDir, explorerRoot);
        explorerListView.setAdapter(adapter);

        explorerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomNode clickedNode = (CustomNode)explorerListView.getItemAtPosition(position);
                if(currentDir != explorerRoot && position == 0) {
                    currentDir = (CustomDirectory) currentDir.parent;
                    NodeAdapter nextAdapter = new NodeAdapter(thisContext, currentDir, explorerRoot);
                    explorerListView.setAdapter(nextAdapter);
                } else if(!clickedNode.isFile()) {
                    currentDir = (CustomDirectory)clickedNode;
                    NodeAdapter nextAdapter = new NodeAdapter(thisContext, currentDir, explorerRoot);
                    explorerListView.setAdapter(nextAdapter);
                } else {
                    if(!isDownloading) {
                        targetedFile = currentDir.contents.get(position);
                        permissionGranted = (ContextCompat.checkSelfPermission(thisContext,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED);
                        if(!permissionGranted) {
                            ActivityCompat.requestPermissions(ExplorerActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
                        } else {
                            isDownloading = true;
                            DownloadingThread downloadTh = new DownloadingThread();
                            downloadTh.execute(targetedFile);
                        }

                    } else {
                        Toast.makeText(getBaseContext(), "Already downloading", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1001:
                if(grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = true;
                    isDownloading = true;
                    DownloadingThread downloadTh = new DownloadingThread();
                    downloadTh.execute(targetedFile);
                } else {
                    permissionGranted = false;
                    Toast.makeText(getBaseContext(), "Download failed", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onBackPressed() {
        new DisconnectThread().execute();
        super.onBackPressed();
    }


    public static FTPClient ftpClient;
    public static String pathToDownloadLocation;

    private class DownloadingThread extends AsyncTask<CustomNode, Integer, Boolean> {

        public String error = "";

        @Override
        protected Boolean doInBackground(CustomNode... customNodes) {
            try {
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                if(!ftpClient.isConnected()) {
                    error = "Host unreachable";
                    return false;
                } else {
                    OutputStream downloadedFileStream = new CountOutputStream(
                            pathToDownloadLocation + customNodes[0].name, false);
                    try {
                        ftpClient.retrieveFile(customNodes[0].path, downloadedFileStream);
                        downloadedFileStream.close();
                    } catch (IOException e) {
                        error = e.getMessage();
                    } finally {
                        downloadedFileStream.close();
                    }
                    return true;
                }
            } catch (IOException e) {
                error = "Error connecting to the server";
                return false;
            }
        }

        @Override
        protected void onPreExecute() {
            ProgressBar downloadProgressBar = (ProgressBar) findViewById(R.id.downloadProgressBar);
            downloadProgressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            ProgressBar downloadProgressBar = (ProgressBar) findViewById(R.id.downloadProgressBar);
            downloadProgressBar.setProgress(0);
            downloadProgressBar.setVisibility(View.GONE);
            if(aBoolean) {
                Toast.makeText(getBaseContext(), "Download finished", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseContext(), "Download failed", Toast.LENGTH_LONG).show();
            }
            isDownloading = false;
            super.onPostExecute(aBoolean);
        }
    }
}
