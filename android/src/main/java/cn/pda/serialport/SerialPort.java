package cn.pda.serialport;

import android.util.Log;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPort {
    private static final String TAG = "SerialPort";
    public static int TNCOM_EVENPARITY = 0;
    public static int TNCOM_ODDPARITY = 1;
    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;
    byte[] test;
    private boolean trig_on = false;

    private static native FileDescriptor open(int i, int i2);

    private static native FileDescriptor open(int i, int i2, int i3);

    public native void close(int i);

    public native void irdapoweroff();

    public native void irdapoweron();

    public native void power3v3off();

    public native void power3v3on();

    public native void psampoweroff();

    public native void psampoweron();

    public native void rfidPoweroff();

    public native void rfidPoweron();

    public native void scanerpoweroff();

    public native void scanerpoweron();

    public native void scanertrigeroff();

    public native void scanertrigeron();

    public native void setGPIOhigh(int i);

    public native void setGPIOlow(int i);

    public native void test(byte[] bArr);

    public native void usbOTGpowerOff();

    public native void usbOTGpowerOn();

    public native void zigbeepoweroff();

    public native void zigbeepoweron();

    static {
        System.loadLibrary("devapi");
        System.loadLibrary("irdaSerialPort");
    }

    public SerialPort() {
    }

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

    public void power_5Von() {
        zigbeepoweron();
    }

    public void power_5Voff() {
        zigbeepoweroff();
    }

    public void power_3v3on() {
        power3v3on();
    }

    public void power_3v3off() {
        power3v3off();
    }

    public void rfid_poweron() {
        rfidPoweron();
    }

    public void rfid_poweroff() {
        rfidPoweroff();
    }

    public void psam_poweron() {
        psampoweron();
    }

    public void psam_poweroff() {
        psampoweroff();
    }

    public void scaner_poweron() {
        scanerpoweron();
        scaner_trigoff();
    }

    public void scaner_poweroff() {
        scanerpoweroff();
    }

    public void scaner_trigon() {
        scanertrigeron();
        this.trig_on = true;
    }

    public void scaner_trigoff() {
        scanertrigeroff();
        this.trig_on = false;
    }

    public boolean scaner_trig_stat() {
        return this.trig_on;
    }
}
