package com.example.george.ftpclientapp;

import org.apache.commons.net.ftp.FTPFile;

public class CustomFile extends CustomNode {

    public CustomFile(CustomNode parentNode, FTPFile f) {
        super(parentNode, f);
    }
}

// DUMP

/*try {
                    try {
                        ftp.connect(server.getText().toString());
                    } catch (UnknownHostException e) {
                        ShowToast(e.getMessage());
                        return;
                    }
                    int reply = ftp.getReplyCode();

                    if (!FTPReply.isPositiveCompletion(reply))
                    {
                        ftp.disconnect();
                        ShowToast("FTP server refused connection.");
                        return;
                    }

                    if(!ftp.login(user.getText().toString(), pass.getText().toString())) {
                        ftp.logout();
                        ftp.disconnect();
                        ShowToast("User or password incorrect");
                        return;
                    }

                    ftp.setFileType(FTP.BINARY_FILE_TYPE);
                    ftp.enterLocalPassiveMode();



                } catch (IOException e) {
                    ShowToast("Error connecting to the server");
                }

                try {
                    if (ftp.isConnected()) {
                        CustomNode.ftpClient = ftp;
                        CustomNode.root = new CustomDirectory();
                        CustomNode.root.expandNodes();

                        Intent startIntent = new Intent(getApplicationContext(), ExplorerActivity.class);
                        startActivity(startIntent);

                        ftp.disconnect();
                    }
                } catch (Exception e) {
                    ShowToast("Error 404");
                }*/
