package com.android.hdhe.uhf.readerInterface;


import cn.pda.serialport.Tools;

public class TagModel {
    private byte[] mEpcBytes;
    private byte mRssi;

    public TagModel(byte[] epcBytes, byte rssi) {
        this.mEpcBytes = epcBytes;
        this.mRssi = rssi;
    }

    public byte[] getmEpcBytes() {
        return this.mEpcBytes;
    }

    public byte getmRssi() {
        return this.mRssi;
    }

    public String getValue() {
        if (mEpcBytes == null && mEpcBytes.length == 0) {
            return "";//"", (byte) -1);
        } else {
            return Tools.Bytes2HexString(mEpcBytes, mEpcBytes.length);
            // Tools.Bytes2HexString(tag.getmEpcBytes(), tag.getmEpcBytes().length), tag.getmRssi());
        }

    }
}
