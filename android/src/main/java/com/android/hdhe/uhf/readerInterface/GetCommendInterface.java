package com.android.hdhe.uhf.readerInterface;

public interface GetCommendInterface {
    void getFirmware();

    void inventoryRealTime();

    void readFrom6C(int i, int i2, int i3);

    void selectEPC(byte[] bArr);

    void setBaudrate();

    void setWorkAntenna();

    void writeTo6C(byte[] bArr, int i, int i2, byte[] bArr2);
}
