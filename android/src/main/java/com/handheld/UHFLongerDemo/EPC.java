package com.handheld.UHFLongerDemo;

public class EPC {
    private int count;
    private String epc;
    private int id;
    private int rssi;

    private boolean isFind;

    public boolean isFind() {
        return this.isFind;
    }

    public void setFind(boolean isFind2) {
        this.isFind = isFind2;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id2) {
        this.id = id2;
    }

    public String getEpc() {
        return this.epc;
    }

    public void setEpc(String epc2) {
        this.epc = epc2;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count2) {
        this.count = count2;
    }

    public int getRssi() {
        return this.rssi;
    }

    public void setRssi(int rssi2) {
        this.rssi = rssi2;
    }

    public String toString() {
        return "EPC [id=" + this.id + ", epc=" + this.epc + ", count=" + this.count + "]";
    }
}
