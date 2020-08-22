package cn.pda.serialport;

import android.view.MotionEvent;
import android.view.View;

import com.android.hdhe.uhf.consts.Constants;


public class Tools {
    public static String Bytes2HexString(byte[] b, int size) {
        String ret = "";
        for (int i = 0; i < size; i++) {
            String hex = Integer.toHexString(b[i] & 255);
            if (hex.length() == 1) {
                hex = "0" + hex;
            }
            ret = String.valueOf(ret) + hex.toUpperCase();
        }
        return ret;
    }

    public static byte uniteBytes(byte src0, byte src1) {
        return (byte) (((byte) (Byte.decode("0x" + new String(new byte[]{src0})).byteValue() << 4)) ^ Byte.decode("0x" + new String(new byte[]{src1})).byteValue());
    }

    public static byte[] HexString2Bytes(String src) {
        int len = src.length() / 2;
        byte[] ret = new byte[len];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < len; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[(i * 2) + 1]);
        }
        return ret;
    }

    public static int bytesToInt(byte[] bytes) {
        return (bytes[0] & MotionEvent.ACTION_MASK) | ((bytes[1] << 8) & MotionEvent.ACTION_POINTER_INDEX_MASK) | ((bytes[2] << Constants.CMD_SUCCESS) & 16711680) | ((bytes[3] << 25) & View.MEASURED_STATE_MASK);
    }

    public static byte[] intToByte(int i) {
        byte[] abyte0 = new byte[3];
        abyte0[2] = (byte) (i & MotionEvent.ACTION_MASK);
        abyte0[1] = (byte) ((65280 & i) >> 8);
        abyte0[0] = (byte) ((16711680 & i) >> 16);
        return abyte0;
    }
}
