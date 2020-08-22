package com.handheld.UHFLongerDemo;

import com.android.hdhe.uhf.reader.UhfReader;

import java.util.ArrayList;
import java.util.List;

public class MyApplication {
    public static MyApplication myapp;
    private List<String> listEpc = new ArrayList();
    private List<String> listInputEPC = new ArrayList();
    private UhfReader manager = null;
    private String password = null;
    private String selectEPC = null;

    public UhfReader getmanager() {
        return this.manager;
    }

    public List<String> getListInputEPC() {
        return this.listInputEPC;
    }

    public void setListInputEPC(List<String> listInputEPC2) {
        this.listInputEPC = listInputEPC2;
    }

    public void setmanager(UhfReader manager2) {
        this.manager = manager2;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password2) {
        this.password = password2;
    }

    public String getSelectEPC() {
        return this.selectEPC;
    }

    public void setSelectEPC(String selectEPC2) {
        this.selectEPC = selectEPC2;
    }

    public List<String> getListEpc() {
        return this.listEpc;
    }

    public void setListEpc(List<String> listEpc2) {
        this.listEpc = listEpc2;
    }

    public void addEPC(String epc) {
        if (this.listEpc != null) {
            this.listEpc.add(epc);
        }
    }
}
