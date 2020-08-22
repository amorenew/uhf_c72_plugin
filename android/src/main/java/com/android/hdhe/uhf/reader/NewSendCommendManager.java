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

public class NewSendCommendManager implements CommendManager {
    public static final byte ERROR_CODE_ACCESS_FAIL = 22;
    public static final byte ERROR_CODE_NO_CARD = 9;
    public static final byte ERROR_CODE_READ_SA_OR_LEN_ERROR = -93;
    public static final byte ERROR_CODE_WRITE_SA_OR_LEN_ERROR = -77;
    public static final int LOCK_MEM_ACCESS = 1;
    public static final int LOCK_MEM_EPC = 2;
    public static final int LOCK_MEM_KILL = 0;
    public static final int LOCK_MEM_TID = 3;
    public static final int LOCK_MEM_USER = 4;
    public static final int LOCK_TYPE_LOCK = 2;
    public static final int LOCK_TYPE_OPEN = 0;
    public static final int LOCK_TYPE_PERMA_LOCK = 3;
    public static final int LOCK_TYPE_PERMA_OPEN = 1;
    public static final byte RESPONSE_OK = 0;
    public static final int SENSITIVE_HIHG = 3;
    public static final int SENSITIVE_LOW = 1;
    public static final int SENSITIVE_MIDDLE = 2;
    public static final int SENSITIVE_VERY_LOW = 0;
    private final byte END = 126;
    private final byte HEAD = -69;
    private InputStream in;
    private OutputStream out;
    private byte[] selectEPC = null;
   // private static NewSendCommendManager manager;

    public NewSendCommendManager(InputStream serialPortInput, OutputStream serialportOutput) {
        this.in = serialPortInput;
        this.out = serialportOutput;
    }
   /* public static NewSendCommendManager getInstance() throws Exception {
        if (manager == null) {
            manager = new NewSendCommendManager(serialportPath, port);
        }
        return manager;
    }*/

    private void sendCMD(byte[] cmd) {
        try {
            this.out.write(cmd);
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean setBaudrate() {
        byte[] bArr = new byte[0];
        return false;
    }

    public byte[] getFirmware() {
        byte[] cmd = new byte[8];
        cmd[0] = -69;
        cmd[2] = 3;
        cmd[4] = 1;
        cmd[6] = 4;
        cmd[7] = 126;
        sendCMD(cmd);
        byte[] response = read();
        if (response == null) {
            return null;
        }
        Log.e("", Tools.Bytes2HexString(response, response.length));
        byte[] resolve = handlerResponse(response);
        if (resolve == null || resolve.length <= 1) {
            return null;
        }
        byte[] version = new byte[(resolve.length - 1)];
        System.arraycopy(resolve, 1, version, 0, resolve.length - 1);
        return version;
    }

    public void setSensitivity(int value) {
        byte[] cmd = new byte[11];
        cmd[0] = -69;
        cmd[2] = -16;
        cmd[4] = 4;
        cmd[5] = 2;
        cmd[6] = 6;
        cmd[8] = Constants.HEAD;
        cmd[9] = -100;
        cmd[10] = 126;
        cmd[5] = (byte) value;
        cmd[cmd.length - 2] = checkSum(cmd);
        sendCMD(cmd);
        byte[] response = read();
        if (response != null) {
            Log.e("setSensitivity ", Tools.Bytes2HexString(response, response.length));
        }
    }

    private byte[] read() {
        int available = 0;
        int headIndex = 0;
        for (int index = 0; index < 10; index++) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                available = this.in.available();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (available > 7) {
                break;
            }
        }
        if (available <= 0) {
            return null;
        }
        try {
            byte[] responseData = new byte[available];
            this.in.read(responseData);
            int i = 0;
            while (true) {
                if (i >= available) {
                    break;
                } else if (responseData[i] == -69) {
                    headIndex = i;
                    break;
                } else {
                    i++;
                }
            }
            byte[] response = new byte[(available - headIndex)];
            System.arraycopy(responseData, headIndex, response, 0, response.length);
            return response;
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0027, code lost:
        r1 = 2;
        r0 = 6;
        r2 = 560;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000f, code lost:
        return setRecvParam(r1, r0, r2);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
   /* public boolean setOutputPower(int r5) {
        /*
            r4 = this;
            r1 = 1
            r0 = 3
            r2 = 432(0x1b0, float:6.05E-43)
            switch(r5) {
                case 16: goto L_0x0010;
                case 17: goto L_0x0015;
                case 18: goto L_0x001a;
                case 19: goto L_0x001f;
                case 20: goto L_0x0023;
                case 21: goto L_0x0027;
                case 22: goto L_0x002c;
                case 23: goto L_0x0031;
                default: goto L_0x0007;
            }
        L_0x0007:
            r1 = 6
            r0 = 7
            r2 = 624(0x270, float:8.74E-43)
        L_0x000b:
            boolean r3 = r4.setRecvParam(r1, r0, r2)
            return r3
        L_0x0010:
            r1 = 1
            r0 = 1
            r2 = 432(0x1b0, float:6.05E-43)
            goto L_0x000b
        L_0x0015:
            r1 = 1
            r0 = 3
            r2 = 432(0x1b0, float:6.05E-43)
            goto L_0x000b
        L_0x001a:
            r1 = 2
            r0 = 4
            r2 = 432(0x1b0, float:6.05E-43)
            goto L_0x000b
        L_0x001f:
            r1 = 2
            r0 = 5
            r2 = 496(0x1f0, float:6.95E-43)
        L_0x0023:
            r1 = 2
            r0 = 6
            r2 = 496(0x1f0, float:6.95E-43)
        L_0x0027:
            r1 = 2
            r0 = 6
            r2 = 560(0x230, float:7.85E-43)
            goto L_0x000b
        L_0x002c:
            r1 = 3
            r0 = 6
            r2 = 624(0x270, float:8.74E-43)
            goto L_0x000b
        L_0x0031:
            r1 = 4
            r0 = 6
            r2 = 624(0x270, float:8.74E-43)
            goto L_0x000b

        throw new UnsupportedOperationException("Method not decompiled: com.android.hdhe.uhf.reader.NewSendCommendManager.setOutputPower(int):boolean");
    }*/


    public void setWorkAntenna() {
        byte[] cmd = new byte[6];
        cmd[0] = Constants.HEAD;
        cmd[1] = 4;
        cmd[2] = 1;
        cmd[3] = Constants.CMD_SET_WORK_ANTENNA;
        cmd[4] = 0;
        cmd[5] = checkSum(cmd, cmd.length - 1);
        sendCMD(cmd);
    }

    public boolean setOutputPower(int value) {
        byte[] cmd = new byte[6];
        cmd[0] = Constants.HEAD;
        cmd[1] = 4;
        cmd[2] = 1;
        cmd[3] = Constants.CMD_SET_OUTPUT_POWER;
        cmd[4] = (byte) value;
        cmd[5] = checkSum(cmd, cmd.length - 1);
        sendCMD(cmd);
        read();
        return true;
    }

    public boolean setRecvParam(int mixer_g, int if_g, int trd) {
        byte[] cmd = new byte[11];
        cmd[0] = -69;
        cmd[2] = -16;
        cmd[4] = 4;
        cmd[5] = 3;
        cmd[6] = 6;
        cmd[7] = 1;
        cmd[8] = Constants.CMD_ISO18000_6B_INVENTORY;
        cmd[9] = -82;
        cmd[10] = 126;
        cmd[5] = (byte) mixer_g;
        cmd[6] = (byte) if_g;
        cmd[7] = (byte) (trd / 256);
        cmd[8] = (byte) (trd % 256);
        cmd[9] = checkSum(cmd);
        sendCMD(cmd);
        byte[] recv = read();
        if (recv == null || handlerResponse(recv) == null) {
            return false;
        }
        return true;
    }

    public List<byte[]> inventoryMulti() {
        unSelectEPC();
        List<byte[]> list = new ArrayList<>();
        byte[] cmd = new byte[10];
        cmd[0] = -69;
        cmd[2] = 39;
        cmd[4] = 3;
        cmd[5] = Constants.ANTENNA_MISSING_ERROR;
        cmd[6] = 39;
        cmd[7] = Constants.CMD_SUCCESS;
        cmd[8] = Constants.CMD_6C_LOCK;
        cmd[9] = 126;
        sendCMD(cmd);
        byte[] response = read();
        if (response != null) {
            int responseLength = response.length;
            int start = 0;
            if (responseLength > 15) {
                while (responseLength > 5) {
                    int paraLen = response[start + 4] & MotionEvent.ACTION_MASK;
                    int singleCardLen = paraLen + 7;
                    if (singleCardLen > responseLength) {
                        break;
                    }
                    byte[] sigleCard = new byte[singleCardLen];
                    System.arraycopy(response, start, sigleCard, 0, singleCardLen);
                    byte[] resolve = handlerResponse(sigleCard);
                    if (resolve != null && paraLen > 5) {
                        byte[] epcBytes = new byte[(paraLen - 5)];
                        System.arraycopy(resolve, 4, epcBytes, 0, paraLen - 5);
                        list.add(epcBytes);
                    }
                    start += singleCardLen;
                    responseLength -= singleCardLen;
                }
            } else {
                handlerResponse(response);
            }
        }
        return list;
    }

    public void stopInventoryMulti() {
        byte[] cmd = new byte[7];
        cmd[0] = -69;
        cmd[2] = 40;
        cmd[5] = 40;
        cmd[6] = 126;
        sendCMD(cmd);
        byte[] read = read();
    }

    public List<TagModel> inventoryRealTime() {
        unSelectEPC();
        byte[] cmd = new byte[7];
        cmd[0] = -69;
        cmd[2] = Constants.ANTENNA_MISSING_ERROR;
        cmd[5] = Constants.ANTENNA_MISSING_ERROR;
        cmd[6] = 126;
        sendCMD(cmd);
        Log.e("inventoryRealTime cmd", Tools.Bytes2HexString(cmd, cmd.length));
        List<TagModel> list = new ArrayList<>();
        byte[] response = read();
        if (response != null) {
            int responseLength = response.length;
            Log.e("inventoryRealTime", Tools.Bytes2HexString(response, response.length));
            int start = 0;
            if (responseLength > 6) {
                while (responseLength > 5) {
                    Log.e("多张卡", Tools.Bytes2HexString(response, response.length));
                    int paraLen = response[start + 4] & MotionEvent.ACTION_MASK;
                    int singleCardLen = paraLen + 7;
                    if (singleCardLen > responseLength) {
                        break;
                    }
                    byte[] sigleCard = new byte[singleCardLen];
                    System.arraycopy(response, start, sigleCard, 0, singleCardLen);
                    byte[] resolve = handlerResponse(sigleCard);
                    if (resolve != null) {
                        byte[] rssi = new byte[1];
                        if (paraLen > 5) {
                            byte[] epcBytes = new byte[(paraLen - 5)];
                            System.arraycopy(resolve, 4, epcBytes, 0, paraLen - 5);
                            System.arraycopy(resolve, 1, rssi, 0, 1);
                            list.add(new TagModel(epcBytes, rssi[0]));
                        }
                    }
                    start += singleCardLen;
                    responseLength -= singleCardLen;
                }
            } else {
                byte[] resolve2 = handlerResponse(response);
                if (resolve2 != null) {
                    byte[] epcBytes2 = new byte[(responseLength - 5)];
                    System.arraycopy(resolve2, 4, epcBytes2, 0, responseLength - 5);
                    System.arraycopy(resolve2, 1, (byte) -1, 0, 1);
                    list.add(new TagModel(epcBytes2, (byte) -1));
                }
            }
        }
        return list;
    }

    public void selectEPC(byte[] epc) {
        byte[] cmd = new byte[8];
        cmd[0] = -69;
        cmd[2] = 18;
        cmd[4] = 1;
        cmd[6] = 19;
        cmd[7] = 126;
        this.selectEPC = epc;
        sendCMD(cmd);
        byte[] read = read();
    }

    public int unSelectEPC() {
        byte[] cmd = new byte[8];
        cmd[0] = -69;
        cmd[2] = 18;
        cmd[4] = 1;
        cmd[5] = 1;
        cmd[6] = 20;
        cmd[7] = 126;
        sendCMD(cmd);
        byte[] read = read();
        return 0;
    }

    private void setSelectPara() {
        if (this.selectEPC != null) {
            byte[] para = new byte[7];
            para[0] = 1;
            para[4] = Constants.MCU_RESET_ERROR;
            para[5] = (byte) (this.selectEPC.length * 8);
            byte[] cmd = new byte[(this.selectEPC.length + 14)];
            cmd[0] = -69;
            cmd[1] = 0;
            cmd[2] = 12;
            cmd[3] = 0;
            cmd[4] = (byte) (this.selectEPC.length + 7);
            System.arraycopy(para, 0, cmd, 5, para.length);
            Log.e("", "select epc");
            System.arraycopy(this.selectEPC, 0, cmd, 12, this.selectEPC.length);
            cmd[cmd.length - 2] = checkSum(cmd);
            cmd[cmd.length - 1] = 126;
            Log.e("setSelectPara", Tools.Bytes2HexString(cmd, cmd.length));
            sendCMD(cmd);
            byte[] response = read();
            if (response != null) {
                Log.e("setSelectPara response", Tools.Bytes2HexString(response, response.length));
            }
        }
    }

    public byte[] readFrom6C(int memBank, int startAddr, int length, byte[] accessPassword) {
        setSelectPara();
        byte[] cmd = new byte[16];
        cmd[0] = -69;
        cmd[2] = 57;
        cmd[4] = 9;
        cmd[9] = 3;
        cmd[13] = 8;
        cmd[14] = 77;
        cmd[15] = 126;
        byte[] data = null;
        if (accessPassword == null || accessPassword.length != 4) {
            return null;
        }
        System.arraycopy(accessPassword, 0, cmd, 5, 4);
        cmd[9] = (byte) memBank;
        if (startAddr <= 255) {
            cmd[10] = 0;
            cmd[11] = (byte) startAddr;
        } else {
            cmd[10] = (byte) (startAddr / 256);
            cmd[11] = (byte) (startAddr % 256);
        }
        if (length <= 255) {
            cmd[12] = 0;
            cmd[13] = (byte) length;
        } else {
            cmd[12] = (byte) (length / 256);
            cmd[13] = (byte) (length % 256);
        }
        cmd[14] = checkSum(cmd);
        sendCMD(cmd);
        byte[] response = read();
        if (response != null) {
            Log.e("readFrom6c response", Tools.Bytes2HexString(response, response.length));
            byte[] resolve = handlerResponse(response);
            if (resolve != null) {
                Log.e("readFrom6c resolve", Tools.Bytes2HexString(resolve, resolve.length));
                if (resolve[0] == 57) {
                    int lengData = (resolve.length - resolve[1]) - 2;
                    data = new byte[lengData];
                    System.arraycopy(resolve, resolve[1] + 2, data, 0, lengData);
                } else {
                    data = new byte[]{resolve[1]};
                }
            }
        }
        return data;
    }

    public boolean writeTo6C(byte[] password, int memBank, int startAddr, int dataLen, byte[] data) {
        byte[] resolve;
        setSelectPara();
        if (password == null || password.length != 4) {
            return false;
        }
        int cmdLen = data.length + 16;
        int parameterLen = data.length + 9;
        byte[] cmd = new byte[cmdLen];
        cmd[0] = -69;
        cmd[1] = 0;
        cmd[2] = 73;
        if (parameterLen < 256) {
            cmd[3] = 0;
            cmd[4] = (byte) parameterLen;
        } else {
            cmd[3] = (byte) (parameterLen / 256);
            cmd[4] = (byte) (parameterLen % 256);
        }
        System.arraycopy(password, 0, cmd, 5, 4);
        cmd[9] = (byte) memBank;
        if (startAddr < 256) {
            cmd[10] = 0;
            cmd[11] = (byte) startAddr;
        } else {
            cmd[10] = (byte) (startAddr / 256);
            cmd[11] = (byte) (startAddr % 256);
        }
        if (dataLen < 256) {
            cmd[12] = 0;
            cmd[13] = (byte) dataLen;
        } else {
            cmd[12] = (byte) (dataLen / 256);
            cmd[13] = (byte) (dataLen % 256);
        }
        System.arraycopy(data, 0, cmd, 14, data.length);
        cmd[cmdLen - 2] = checkSum(cmd);
        cmd[cmdLen - 1] = 126;
        sendCMD(cmd);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        byte[] response = read();
        if (response == null || (resolve = handlerResponse(response)) == null || resolve[0] != 73 || resolve[resolve.length - 1] != 0) {
            return false;
        }
        return true;
    }

    public boolean lock6C(byte[] password, int memBank, int lockType) {
        setSelectPara();
        byte[] cmd = new byte[14];
        cmd[0] = -69;
        cmd[2] = Constants.CMD_6C_WRITE;
        cmd[4] = 7;
        cmd[13] = 126;
        int lockPay = 0;
        byte[] bArr = new byte[4];
        byte[] bArr2 = new byte[4];
        if (lockType == 0) {
            lockPay = 1 << (19 - (memBank * 2));
        }
        if (lockType == 1) {
            lockPay = (1 << (19 - (memBank * 2))) + (1 << ((19 - (memBank * 2)) - 1)) + (1 << (8 - (memBank * 2)));
        }
        if (lockType == 2) {
            if (memBank == 1) {
                lockPay = 131200;
            } else {
                lockPay = (1 << (19 - (memBank * 2))) + (1 << (9 - (memBank * 2)));
            }
        }
        if (lockType == 3) {
            lockPay = (1 << (19 - (memBank * 2))) + (1 << ((19 - (memBank * 2)) - 1)) + (1 << (9 - (memBank * 2))) + (1 << (8 - (memBank * 2)));
        }
        byte[] lockPara = Tools.intToByte(lockPay);
        System.arraycopy(password, 0, cmd, 5, password.length);
        System.arraycopy(lockPara, 0, cmd, 9, lockPara.length);
        cmd[cmd.length - 2] = checkSum(cmd);
        Log.e("Lock cmd", Tools.Bytes2HexString(cmd, cmd.length));
        sendCMD(cmd);
        byte[] recv = read();
        if (recv != null) {
            byte[] resolve = handlerResponse(recv);
            Log.e("Lock recv", Tools.Bytes2HexString(recv, recv.length));
            if (resolve != null && resolve[0] == -126 && resolve[resolve.length - 1] == 0) {
                return true;
            }
            return false;
        }
        return false;
    }

    public void close() {
        try {
            this.in.close();
            this.out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte checkSum(byte[] data) {
        byte crc = 0;
        for (int i = 1; i < data.length - 2; i++) {
            crc = (byte) (data[i] + crc);
        }
        return crc;
    }
    //warning amr
    public byte checkSum(byte[] uBuff, int uBuffLen) {
        byte crc = 0;
        for (int i = 0; i < uBuffLen; i++) {
            crc = (byte) (uBuff[i] + crc);
        }
        return (byte) (((crc ^ -1) + 1) & MotionEvent.ACTION_MASK);
    }
    private byte[] handlerResponse(byte[] response) {
        byte[] data = null;
        int responseLength = response.length;
        if (response[0] != -69) {
            Log.e("handlerResponse", "head error");
            return null;
        } else if (response[responseLength - 1] != 126) {
            Log.e("handlerResponse", "end error");
            return null;
        } else if (responseLength < 7) {
            return null;
        } else {
            int lengthHigh = response[3] & MotionEvent.ACTION_MASK;
            int dataLength = (lengthHigh * 256) + (response[4] & MotionEvent.ACTION_MASK);
            if (checkSum(response) != response[responseLength - 2]) {
                Log.e("handlerResponse", "crc error");
                return null;
            }
            if (dataLength != 0 && responseLength == dataLength + 7) {
                Log.e("handlerResponse", "response right");
                data = new byte[(dataLength + 1)];
                data[0] = response[2];
                System.arraycopy(response, 5, data, 1, dataLength);
            }
            return data;
        }
    }

    public int setFrequency(int startFrequency, int freqSpace, int freqQuality) {
        int frequency = 1;
        if (startFrequency > 840125 && startFrequency < 844875) {
            frequency = (startFrequency - 840125) / 250;
        } else if (startFrequency > 920125 && startFrequency < 924875) {
            frequency = (startFrequency - 920125) / 250;
        } else if (startFrequency > 865100 && startFrequency < 867900) {
            frequency = (startFrequency - 865100) / 200;
        } else if (startFrequency > 902250 && startFrequency < 927750) {
            frequency = (startFrequency - 902250) / 500;
        }
        byte[] cmd = new byte[8];
        cmd[0] = -69;
        cmd[2] = -85;
        cmd[4] = 1;
        cmd[5] = 4;
        cmd[6] = Constants.CMD_ISO18000_6B_INVENTORY;
        cmd[7] = 126;
        cmd[5] = (byte) frequency;
        cmd[6] = checkSum(cmd);
        sendCMD(cmd);
        byte[] recv = read();
        if (recv != null) {
            Log.e("frequency", Tools.Bytes2HexString(recv, recv.length));
        }
        return 0;
    }

    public int setWorkArea(int area) {
        byte[] cmd = new byte[8];
        cmd[0] = -69;
        cmd[2] = 7;
        cmd[4] = 1;
        cmd[5] = 1;
        cmd[6] = 9;
        cmd[7] = 126;
        cmd[5] = (byte) area;
        cmd[6] = checkSum(cmd);
        sendCMD(cmd);
        byte[] recv = read();
        if (recv != null) {
            Log.e("setWorkArea", Tools.Bytes2HexString(recv, recv.length));
            handlerResponse(recv);
        }
        return 0;
    }

    public int getFrequency() {
        byte[] cmd = new byte[7];
        cmd[0] = -69;
        cmd[2] = -86;
        cmd[5] = -86;
        cmd[6] = 126;
        sendCMD(cmd);
        byte[] recv = read();
        if (recv != null) {
            Log.e("getFrequency", Tools.Bytes2HexString(recv, recv.length));
            handlerResponse(recv);
        }
        return 0;
    }

    public boolean kill6C(byte[] killPassword) {
        boolean killFlag = false;
        byte[] cmd = new byte[11];
        cmd[0] = -69;
        cmd[1] = 0;
        cmd[2] = 101;
        cmd[3] = 0;
        cmd[4] = 4;
        System.arraycopy(killPassword, 0, cmd, 5, 4);
        cmd[9] = checkSum(cmd);
        cmd[10] = 126;
        sendCMD(cmd);
        byte[] recv = read();
        if (recv != null) {
            if (recv.length == 6) {
                return false;
            }
            byte[] resolve = handlerResponse(recv);
            if (resolve[resolve.length - 1] == 0) {
                killFlag = true;
            }
        }
        return killFlag;
    }
}
