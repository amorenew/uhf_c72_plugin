package com.android.hdhe.uhf.reader;

import android.util.Log;
import android.view.MotionEvent;

import com.android.hdhe.uhf.consts.Constants;
import com.android.hdhe.uhf.readerInterface.CommendManager;
import com.android.hdhe.uhf.readerInterface.TagModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.pda.serialport.Tools;

public class SendCommendManager implements CommendManager {
    private InputStream in;
    private OutputStream out;

    public SendCommendManager() {
    }

    public SendCommendManager(InputStream in2, OutputStream out2) {
        this.in = in2;
        this.out = out2;
    }

    public boolean setBaudrate() {
        byte[] cmd = new byte[6];
        cmd[0] = Constants.HEAD;
        cmd[1] = 4;
        cmd[2] = -1;
        cmd[3] = Constants.CMD_SET_BAUD;
        cmd[4] = 4;
        cmd[5] = checkSum(cmd, cmd.length - 1);
        sendToReader(cmd);
        byte[] recv = read();
        if (recv == null) {
            Log.i("readFrom6C", "指令超时");
            return false;
        } else if (isRecvData(recv) == 0 && Integer.valueOf(recv[3]).intValue() == 113 && Integer.valueOf(recv[4]).intValue() == 16) {
            return true;
        } else {
            return false;
        }
    }

    public byte[] getFirmware() {
        byte[] cmd = new byte[5];
        cmd[0] = Constants.HEAD;
        cmd[1] = 3;
        cmd[2] = 1;
        cmd[3] = Constants.CMD_GET_FIRMWARE_VERSION;
        cmd[4] = checkSum(cmd, cmd.length - 1);
        sendToReader(cmd);
        byte[] recv = read();
        if (recv != null) {
            System.out.println(new String(recv));
        }
        return recv;
    }

    public byte[] getFrequency() {
        sendToReader(new byte[]{Constants.HEAD, 3, 1, Constants.CMD_GET_FREQUENCY_REGION, -29});
        byte[] recv = read();
        if (recv == null) {
            return null;
        }
        System.out.println(Tools.Bytes2HexString(recv, recv.length));
        return null;
    }

    public int setFrequency(int startFrequency, int freqSpace, int freqQuality) {
        byte[] cmd = new byte[11];
        cmd[0] = Constants.HEAD;
        cmd[1] = 9;
        cmd[2] = 1;
        cmd[3] = Constants.CMD_SET_FREQUENCY_REGION;
        cmd[4] = 4;
        cmd[5] = (byte) (freqSpace / 10);
        cmd[6] = (byte) freqQuality;
        cmd[7] = (byte) ((16711680 & startFrequency) >> 16);
        cmd[8] = (byte) ((65280 & startFrequency) >> 8);
        cmd[9] = (byte) (startFrequency & MotionEvent.ACTION_MASK);
        cmd[10] = checkSum(cmd, cmd.length - 1);
        System.out.println(Tools.Bytes2HexString(cmd, cmd.length));
        sendToReader(cmd);
        byte[] recv = read();
        if (recv != null) {
            System.out.println(Tools.Bytes2HexString(recv, recv.length));
        }
        return 0;
    }

    public void setWorkAntenna() {
        byte[] cmd = new byte[6];
        cmd[0] = Constants.HEAD;
        cmd[1] = 4;
        cmd[2] = 1;
        cmd[3] = Constants.CMD_SET_WORK_ANTENNA;
        cmd[4] = 0;
        cmd[5] = checkSum(cmd, cmd.length - 1);
        sendToReader(cmd);
    }

    public boolean setOutputPower(int value) {
        byte[] cmd = new byte[6];
        cmd[0] = Constants.HEAD;
        cmd[1] = 4;
        cmd[2] = 1;
        cmd[3] = Constants.CMD_SET_OUTPUT_POWER;
        cmd[4] = (byte) value;
        cmd[5] = checkSum(cmd, cmd.length - 1);
        sendToReader(cmd);
        read();
        return true;
    }

    public List<TagModel> inventoryRealTime() {
        List<TagModel> epcList = new ArrayList<>();
        byte[] cmd = new byte[6];
        cmd[0] = Constants.HEAD;
        cmd[1] = 4;
        cmd[2] = 1;
        cmd[3] = Constants.CMD_6C_REAL_TIME_INVENTORY;
        cmd[4] = 1;
        cmd[5] = checkSum(cmd, cmd.length - 1);
        sendToReader(cmd);
        Log.i("", "send inventory real time***");
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        byte[] recv = read();
        if (recv != null) {
            int length = recv.length;
            new ArrayList();
            int start = 0;
            while (length > 0 && recv[start] == -96) {
                byte[] data = new byte[((recv[start + 1] & MotionEvent.ACTION_MASK) + 2)];
                if (data.length > 12 && data.length <= length) {
                    System.arraycopy(recv, start, data, 0, data.length);
                    byte[] epc = new byte[(data.length - 9)];
                    System.arraycopy(data, 7, epc, 0, data.length - 9);
                    System.arraycopy(data, 2, epc, 0, 1);
                    epcList.add(new TagModel(epc, (byte) -1));
                }
                start += data.length;
                length -= data.length;
            }
        } else {
            Log.i("realTimeInventory", "指令超时");
        }
        return epcList;
    }

    public void selectEPC(byte[] epc) {
        int epcLen = epc.length;
        byte[] cmd = new byte[(epcLen + 7)];
        cmd[0] = Constants.HEAD;
        cmd[1] = Constants.CMD_FAIL;
        cmd[2] = 1;
        cmd[3] = Constants.CMD_6C_SET_ACCESS_EPC_MATCH;
        cmd[4] = 0;
        cmd[5] = 12;
        System.arraycopy(epc, 0, cmd, 6, epcLen);
        cmd[epcLen + 6] = checkSum(cmd, cmd.length - 1);
        sendToReader(cmd);
        if (read() == null) {
            Log.i("selectEPC", "指令超时");
        }
    }

    public byte[] readFrom6C(int memBank, int startAddr, int length) {
        byte[] data = null;
        byte[] cmd = new byte[8];
        cmd[0] = Constants.HEAD;
        cmd[1] = 6;
        cmd[2] = 1;
        cmd[3] = Constants.CMD_6C_READ;
        cmd[4] = (byte) memBank;
        cmd[5] = (byte) startAddr;
        cmd[6] = (byte) length;
        cmd[7] = checkSum(cmd, cmd.length - 1);
        sendToReader(cmd);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        byte[] recv = read();
        if (recv != null) {
            int len = recv.length;
            if (recv[0] != -96) {
                return null;
            }
            if (checkSum(recv, len - 1) != recv[len - 1]) {
                Log.i("read data ", "checksum error");
                return null;
            } else if (len == 6) {
                Log.i("read data ", "read fail!!");
                return null;
            } else {
                int i = ((recv[4] & 255) * NewSendCommendManager.RESPONSE_OK) + (recv[5] & 255);
                int dataLen = recv[6] & MotionEvent.ACTION_MASK;
                byte readLen = recv[len - 4];
                int epcLen = (dataLen - readLen) - 4;
                if (dataLen <= readLen || dataLen < epcLen || epcLen < 0) {
                    return null;
                }
                data = new byte[(readLen + epcLen)];
                System.arraycopy(recv, 9, data, 0, epcLen);
                System.arraycopy(recv, epcLen + 11, data, epcLen, readLen);
            }
        } else {
            Log.i("read data ", "指令超时");
        }
        return data;
    }

    public byte[] readFrom6C(int memBank, int startAddr, int length, byte[] password) {
        byte[] data = null;
        byte[] cmd = new byte[12];
        cmd[0] = Constants.HEAD;
        cmd[1] = 10;
        cmd[2] = 1;
        cmd[3] = Constants.CMD_6C_READ;
        cmd[4] = (byte) memBank;
        cmd[5] = (byte) startAddr;
        cmd[6] = (byte) length;
        if (password.length != 4) {
            Log.e("readFrom6C", "password error");
            return null;
        }
        cmd[7] = password[0];
        cmd[8] = password[1];
        cmd[9] = password[2];
        cmd[10] = password[3];
        cmd[11] = checkSum(cmd, cmd.length - 1);
        sendToReader(cmd);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        byte[] recv = read();
        if (recv != null) {
            int len = recv.length;
            if (recv[0] != -96) {
                return null;
            }
            if (checkSum(recv, len - 1) != recv[len - 1]) {
                Log.i("read data ", "checksum error");
                return null;
            } else if (len == 6) {
                Log.i("read data ", "read fail!!");
                return null;
            } else {
                int i = ((recv[4] & 255) * NewSendCommendManager.RESPONSE_OK) + (recv[5] & 255);
                int dataLen = recv[6] & MotionEvent.ACTION_MASK;
                byte readLen = recv[len - 4];
                int epcLen = (dataLen - readLen) - 4;
                if (dataLen <= readLen || dataLen < epcLen || epcLen < 0) {
                    return null;
                }
                if (recv.length < epcLen + 9) {
                    return null;
                }
                data = new byte[(readLen + epcLen)];
                System.arraycopy(recv, 9, data, 0, epcLen);
                System.arraycopy(recv, epcLen + 11, data, epcLen, readLen);
            }
        } else {
            Log.i("read data ", "指令超时");
        }
        return data;
    }

    public boolean writeTo6C(byte[] password, int memBank, int wordAdd, int dataLen, byte[] data) {
        boolean writeFlag = false;
        int dataLen2 = dataLen / 2;
        int cmdLen = (dataLen2 * 2) + 12;
        byte[] cmd = new byte[cmdLen];
        cmd[0] = Constants.HEAD;
        cmd[1] = (byte) (cmdLen - 2);
        cmd[2] = 1;
        cmd[3] = Constants.CMD_6C_WRITE;
        System.arraycopy(password, 0, cmd, 4, password.length);
        cmd[8] = (byte) memBank;
        cmd[9] = (byte) wordAdd;
        cmd[10] = (byte) dataLen2;
        System.arraycopy(data, 0, cmd, 11, data.length);
        cmd[data.length + 11] = checkSum(cmd, data.length + 11);
        sendToReader(cmd);
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        byte[] recv = read();
        if (recv == null) {
            Log.i("write data", "指令超时");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
            read();
        } else if (recv.length == 6) {
            return false;
        } else {
            if (isRecvData(recv) == 0) {
                writeFlag = true;
            }
        }
        return writeFlag;
    }

    public boolean lock6C(byte[] password, int memBank, int lockType) {
        boolean lockFlag = false;
        byte[] cmd = new byte[11];
        cmd[0] = Constants.HEAD;
        cmd[1] = 9;
        cmd[2] = 1;
        cmd[3] = Constants.CMD_6C_LOCK;
        System.arraycopy(password, 0, cmd, 4, password.length);
        cmd[8] = (byte) memBank;
        cmd[9] = (byte) lockType;
        cmd[10] = checkSum(cmd, cmd.length - 1);
        sendToReader(cmd);
        byte[] recv = read();
        if (recv == null) {
            Log.i("Lock..6c", "超时");
        } else if (recv.length == 6) {
            return false;
        } else {
            if (isRecvData(recv) == 0) {
                lockFlag = true;
            }
        }
        return lockFlag;
    }

    public boolean kill6C(byte[] password) {
        boolean killFlag = false;
        byte[] cmd = new byte[9];
        cmd[0] = Constants.HEAD;
        cmd[1] = 7;
        cmd[2] = -1;
        cmd[3] = Constants.CMD_6C_KILL;
        System.arraycopy(password, 0, cmd, 4, password.length);
        cmd[8] = checkSum(cmd, cmd.length - 1);
        byte[] recv = read();
        if (recv == null) {
            Log.i("Kill ***", "超时");
        } else if (recv.length == 6) {
            return false;
        } else {
            if (isRecvData(recv) == 0) {
                killFlag = true;
            }
        }
        return killFlag;
    }

    public void inventory6B() {
        byte[] bArr = new byte[5];
        bArr[0] = Constants.HEAD;
        bArr[1] = 3;
        bArr[2] = 1;
        bArr[3] = Constants.CMD_ISO18000_6B_INVENTORY;
    }

    private byte[] read() {
        int count = 0;
        int index = 0;
        byte[] resp = null;
        while (count < 3) {
            try {
                count = this.in.available();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (index > 50) {
                return null;
            }
            index++;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(50);
            int allCount = this.in.available();
            resp = new byte[allCount];
            Log.i("read allCount****", new StringBuilder(String.valueOf(allCount)).toString());
            int mcount = 0;
            while (count != 0) {
                byte[] bytes = new byte[2];
                this.in.read(bytes);
                byte length = bytes[1];
                Log.i("read count", new StringBuilder(String.valueOf(this.in.available())).toString());
                byte[] data = new byte[length];
                int read = this.in.read(data);
                System.arraycopy(bytes, 0, resp, mcount, 2);
                System.arraycopy(data, 0, resp, mcount + 2, length);
                mcount = mcount + 2 + length;
                count = this.in.available();
            }
        } catch (Exception e2) {
        }
        return resp;
    }

    public boolean sendToReader(byte[] cmd) {
        try {
            this.out.write(cmd);
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public byte checkSum(byte[] uBuff, int uBuffLen) {
        byte crc = 0;
        for (int i = 0; i < uBuffLen; i++) {
            crc = (byte) (uBuff[i] + crc);
        }
        return (byte) (((crc ^ -1) + 1) & MotionEvent.ACTION_MASK);
    }

    public int isRecvData(byte[] recv) {
        if (recv.length < 5) {
            return -1;
        }
        String data = Tools.Bytes2HexString(recv, recv.length);
        if (recv[0] != -96) {
            return -2;
        }
        if (Integer.parseInt(data.substring(2, 4), 16) != recv.length - 2) {
            return -3;
        }
        if (checkSum(recv, recv.length - 1) != recv[recv.length - 1]) {
            return -4;
        }
        return 0;
    }

    public void setSensitivity(int value) {
    }

    public void close() {
    }

    public byte checkSum(byte[] data) {
        return 0;
    }
}
