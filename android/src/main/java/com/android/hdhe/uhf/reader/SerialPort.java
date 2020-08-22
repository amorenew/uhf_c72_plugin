package com.android.hdhe.uhf.reader;

import android.util.Log;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPort {
    private static final String TAG = "SerialPort";
    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;
    private boolean trig_on = false;

    private static native FileDescriptor open(int i, int i2);

    public native void close(int i);

    public native void psampoweroff();

    public native void psampoweron();

    public SerialPort(int port, int baudrate, int flags) throws SecurityException, IOException {
        this.mFd = open(port, baudrate);
        if (this.mFd == null) {
            Log.e(TAG, "native open returns null");
            throw new IOException();
        }
        this.mFileInputStream = new FileInputStream(this.mFd);
        this.mFileOutputStream = new FileOutputStream(this.mFd);
    }

    public InputStream getInputStream() {
        return this.mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return this.mFileOutputStream;
    }

    public void uhfPowerOn() {
        psampoweron();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        byte[] bytes = new byte[8];
        try {
            this.mFileInputStream.read(bytes);
            System.out.println(new String(bytes));
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public void uhfPowerOff() {
        psampoweroff();
    }

    static {
        System.loadLibrary("devapi");
        System.loadLibrary("uhf");
    }
}
