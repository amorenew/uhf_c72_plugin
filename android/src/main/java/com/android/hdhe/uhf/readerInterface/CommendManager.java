package com.android.hdhe.uhf.readerInterface;

import java.util.List;

public interface CommendManager {
    byte checkSum(byte[] bArr);

    void close();

    byte[] getFirmware();

    List<TagModel> inventoryRealTime();

    boolean lock6C(byte[] bArr, int i, int i2);

    byte[] readFrom6C(int i, int i2, int i3, byte[] bArr);

    void selectEPC(byte[] bArr);

    boolean setBaudrate();

    int setFrequency(int i, int i2, int i3);

    boolean setOutputPower(int i);

    void setSensitivity(int i);

    boolean writeTo6C(byte[] bArr, int i, int i2, int i3, byte[] bArr2);
}
