package com.android.hdhe.uhf.reader;

import com.android.hdhe.uhf.readerInterface.CommendManager;
import com.android.hdhe.uhf.readerInterface.TagModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;


public class UhfReader implements CommendManager {
    public static final int LOCK_MEM_ACCESS = 1;
    public static final int LOCK_MEM_EPC = 2;
    public static final int LOCK_MEM_KILL = 0;
    public static final int LOCK_MEM_TID = 3;
    public static final int LOCK_MEM_USER = 4;
    public static final int LOCK_TYPE_LOCK = 2;
    public static final int LOCK_TYPE_OPEN = 0;
    public static final int LOCK_TYPE_PERMA_LOCK = 3;
    public static final int LOCK_TYPE_PERMA_OPEN = 1;
    public static final int MEMBANK_EPC = 1;
    public static final int MEMBANK_RESEVER = 0;
    public static final int MEMBANK_TID = 2;
    public static final int MEMBANK_USER = 3;
    private static int baudRate = 115200;
    private static InputStream in;
    private static NewSendCommendManager manager;
    private static OutputStream os;
    private static int port = 13;
    private static UhfReader reader;
    private static SerialPort serialPort;

    private UhfReader() {
    }

    public static UhfReader getInstance() {
        if (serialPort == null) {
            try {
                serialPort = new SerialPort(port, baudRate, 0);
                serialPort.psampoweron();
                in = serialPort.getInputStream();
                os = serialPort.getOutputStream();
            } catch (Exception e) {
                return null;
            }
        }
        if (manager == null) {
            manager = new NewSendCommendManager(in, os);
        }
        if (reader == null) {
            reader = new UhfReader();
        }
        try {
            Thread.sleep(200);
            manager.setWorkArea(1);//1 China2 - 2 USA - 3 Europe - 4 China1 - Korea 5
            //manager.setWorkArea(3);//1 China2 - 2 USA - 3 Europe - 4 China1 - Korea 5
        } catch (Exception var1) {
            var1.printStackTrace();
        }
        return reader;
    }

    public void powerOn() {
        serialPort.psampoweron();
    }

    public void powerOff() {
        serialPort.psampoweroff();
    }

    public boolean setBaudrate() {
        return manager.setBaudrate();
    }

    public byte[] getFirmware() {
        return manager.getFirmware();
    }

    public boolean setOutputPower(int value) {
        // public String[] powers = {"26dbm", "24dbm", "20dbm", "18dbm", "17dbm", "16dbm"};
        return manager.setOutputPower(value);
    }

    public int setWorkArea(int value) {
        //1 China2 - 2 USA - 3 Europe - 4 China1 - Korea 5
        return manager.setWorkArea(value);
    }

    public List<TagModel> inventoryRealTime() {
        return manager.inventoryRealTime();
    }

    public void selectEPC(byte[] epc) {
        manager.selectEPC(epc);
    }

    public byte[] readFrom6C(int memBank, int startAddr, int length, byte[] accessPassword) {
        return manager.readFrom6C(memBank, startAddr, length, accessPassword);
    }

    public boolean writeTo6C(byte[] password, int memBank, int startAddr, int dataLen, byte[] data) {
        return manager.writeTo6C(password, memBank, startAddr, dataLen, data);
    }

    public void setSensitivity(int value) {
        manager.setSensitivity(value);
    }

    public boolean lock6C(byte[] password, int memBank, int lockType) {
        return manager.lock6C(password, memBank, lockType);
    }

    public void close() {
        if (manager != null) {
            manager.close();
            manager = null;
        }
        if (serialPort != null) {
            serialPort.psampoweroff();
            serialPort.close(port);
            serialPort = null;
        }
        if (reader != null) {
            reader = null;
        }
    }

    public byte checkSum(byte[] data) {
        return 0;
    }

    public int setFrequency(int startFrequency, int freqSpace, int freqQuality) {
        return manager.setFrequency(startFrequency, freqSpace, freqQuality);
    }

    public void setDistance(int distance) {
    }

    public void close(InputStream input, OutputStream output) {
        if (manager != null) {
            manager = null;
            try {
                input.close();
                output.close();
            } catch (IOException var4) {
                var4.printStackTrace();
            }
        }
    }

    public List<byte[]> inventoryMulti() {
        return manager.inventoryMulti();
    }

    public void stopInventoryMulti() {
        manager.stopInventoryMulti();
    }

    public int getFrequency() {
        return manager.getFrequency();
    }

    public int unSelect() {
        return manager.unSelectEPC();
    }

    public void setRecvParam(int mixer_g, int if_g, int trd) {
        manager.setRecvParam(mixer_g, if_g, trd);
    }

    public boolean kill6C(byte[] killPassword) {
        return manager.kill6C(killPassword);
    }
}
