package com.example.george.ftpclientapp;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CustomDirectory extends CustomNode {
    public ArrayList<CustomNode> contents;
    public static FTPFile[] lastUsed;

    public CustomDirectory() {
        super();
        contents = new ArrayList<CustomNode>();
    }

    public CustomDirectory(CustomNode parentNode, FTPFile f) {
        super(parentNode, f);
        contents = new ArrayList<CustomNode>();
    }

    @Override
    public void expandNodes() {
        ArrayList<FTPFile> fileList;
        try {
            if(path.isEmpty()) {
                lastUsed = ftpClient.listFiles();
                fileList = new ArrayList<>(Arrays.asList(lastUsed));
            } else {
                lastUsed = ftpClient.listFiles(path);
                fileList = new ArrayList<>(Arrays.asList(lastUsed));
            }
        } catch (IOException e) { return; }
        for(FTPFile f : fileList) {
            CustomNode newNode;
            if(f.isFile())
                newNode = new CustomFile(this, f);
            else
                newNode = new CustomDirectory(this, f);
            contents.add(newNode);
            newNode.expandNodes();
        }
    }
}
