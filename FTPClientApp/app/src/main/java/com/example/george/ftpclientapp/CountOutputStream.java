package com.example.george.ftpclientapp;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CountOutputStream extends FileOutputStream {

    public CountOutputStream(String path, boolean app) throws FileNotFoundException {
        super(path, app);
        bytesWrote = 0;
    }

    @Override
    public void write(int b) throws IOException {
        super.write(b);
        bytesWrote += sizeofInt;
    }

    @Override
    public void write(byte[] b) throws IOException {
        super.write(b);
        bytesWrote += b.length;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        super.write(b, off, len);
        bytesWrote += len;
    }

    public long bytesWrote;
    public static int sizeofInt = Integer.SIZE / Byte.SIZE;
}
