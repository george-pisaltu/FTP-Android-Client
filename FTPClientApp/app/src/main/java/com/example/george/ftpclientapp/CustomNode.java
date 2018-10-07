package com.example.george.ftpclientapp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class CustomNode {
    public static FTPClient ftpClient;
    public static CustomNode root;

    public String name;
    public String path;
    public CustomNode parent;
    public boolean isFile;
    public FTPFile fileLink;

    public CustomNode() {
        name = "";
        path = "";
        parent = null;
        isFile = false;
        fileLink = null;
    }

    public CustomNode(CustomNode parentNode, FTPFile f) {
        fileLink = f;
        parent = parentNode;
        if(f.isFile())
            isFile = true;
        else
            isFile = false;
        name = f.getName();
        path = parent.path + "/" + name;
    }

    public void expandNodes() {

    }

    public boolean isFile() {
        return isFile;
    }

    public long getSize() {
        return fileLink.getSize();
    }

}
