package com.amorenew.c72.uhf_plugin.helper;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.rscja.deviceapi.RFIDWithUHFUART;
import com.rscja.deviceapi.entity.UHFTAGInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class UHFHelper {
    private static UHFHelper instance;
    public RFIDWithUHFUART mReader;
    Handler handler;
    private UHFListener uhfListener;
    private boolean isStart = false;
    private boolean isConnect = false;
    //private boolean isSingleRead = false;
    private ArrayList<HashMap<String, String>> tagList;
    private HashMap<String, String> map;

    private UHFHelper() {
    }

    public static UHFHelper getInstance() {
        if (instance == null)
            instance = new UHFHelper();
        return instance;
    }

    //public RFIDWithUHFUART getReader() {
     //   return mReader;
    //}

    public void setUhfListener(UHFListener uhfListener) {
        this.uhfListener = uhfListener;
    }

    public void init() {
        // this.context = context;
        //this.uhfListener = uhfListener;
        tagList = new ArrayList<>();
        clearData();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String result = msg.obj + "";
                String[] strs = result.split("@");
                addEPCToList(strs[0], strs[1]);
            }
        };

    }

    public boolean connect() {
        try {
            mReader = RFIDWithUHFUART.getInstance();
        } catch (Exception ex) {
            uhfListener.onConnect(false,0);
            return false;
        }

        if (mReader != null) {
            isConnect= mReader.init();
            uhfListener.onConnect(isConnect,0);
            return isConnect;
        }
        uhfListener.onConnect(false,0);
        return false;
    }

    public boolean start(boolean isSingleRead) {
        if (!isStart) {
            if (isSingleRead) {// Single Read
                UHFTAGInfo strUII = mReader.inventorySingleTag();
                if (strUII != null) {
                    String strEPC = strUII.getEPC();
                    addEPCToList(strEPC, strUII.getRssi());
                    return true;
                } else {
                    return false;
                }
            } else {// Auto read multi  .startInventoryTag((byte) 0, (byte) 0))
                //  mContext.mReader.setEPCTIDMode(true);
                if (mReader.startInventoryTag()) {
                    isStart = true;
                    new TagThread().start();
                    return true;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public void clearData() {
        tagList.clear();
    }

    public boolean stop() {
        if (isStart && mReader != null) {
            isStart = false;
            return mReader.stopInventory();
        }
        isStart = false;
        return false;
    }

    public void close() {
        isStart = false;
        if (mReader != null) {
            mReader.free();
            isConnect=false;
        }
    }

    public boolean setPowerLevel(String level) {
        //5 dBm : 30 dBm
        if (mReader != null)
            return mReader.setPower(Integer.parseInt(level));
        return false;
    }

    public boolean setWorkArea(String area) {
        //China Area 920~925MHz
        //Chin2a Area 840~845MHz
        //ETSI Area 865~868MHz
        //Fixed Area 915MHz
        //United States Area 902~928MHz
        if (mReader != null)
            return mReader.setFrequencyMode((byte) Integer.parseInt(area));
        return false;
    }


    /**
     * 添加EPC到列表中
     *
     * @param epc
     */
    private void addEPCToList(String epc, String rssi) {
        if (!TextUtils.isEmpty(epc)) {
            int index = checkIsExist(epc);

            map = new HashMap<String, String>();

            map.put(TagKey.ID, "");
            map.put(TagKey.EPC, epc);
            map.put(TagKey.COUNT, String.valueOf(1));
            map.put(TagKey.RSSI, rssi);

            // mContext.getAppContext().uhfQueue.offer(epc + "\t 1");

            if (index == -1) {
                tagList.add(map);
            } else {
                int tagCount = Integer.parseInt(
                        Objects.requireNonNull(tagList.get(index).get(TagKey.COUNT)), 10) + 1;

                map.put(TagKey.COUNT, String.valueOf(tagCount));

                tagList.set(index, map);

            }
            final JSONArray jsonArray = new JSONArray();

            for (int i = 0; i < tagList.size(); i++) {
                HashMap<String, String> map = tagList.get(i);
                JSONObject json = new JSONObject();
                try {
                    json.put(TagKey.ID, map.get(TagKey.ID));
                    json.put(TagKey.EPC, map.get(TagKey.EPC));
                    json.put(TagKey.RSSI, map.get(TagKey.RSSI));
                    json.put(TagKey.COUNT, map.get(TagKey.COUNT));
                    jsonArray.put(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            uhfListener.onRead(jsonArray.toString());

        }
    }

    /**
     * 判断EPC是否在列表中
     *
     * @param strEPC 索引
     * @return
     */
    public int checkIsExist(String strEPC) {
        int existFlag = -1;
        if (strEPC == null || strEPC.length() == 0) {
            return existFlag;
        }
        String tempStr = "";
        for (int i = 0; i < tagList.size(); i++) {
            HashMap<String, String> temp = new HashMap<String, String>();
            temp = tagList.get(i);
            tempStr = temp.get("tagUii");
            if (strEPC.equals(tempStr)) {
                existFlag = i;
                break;
            }
        }
        return existFlag;
    }

    public boolean isEmptyTags() {
        return tagList != null && !tagList.isEmpty();
    }

    public boolean isStarted() {
        return isStart;
    }
   public boolean isConnected() {
        return isConnect;
    }

    class TagThread extends Thread {
        public void run() {
            String strTid;
            String strResult;
            UHFTAGInfo res = null;
            while (isStart) {
                res = mReader.readTagFromBuffer();
                if (res != null) {
                    strTid = res.getTid();
                    if (strTid.length() != 0 && !strTid.equals("0000000" +
                            "000000000") && !strTid.equals("000000000000000000000000")) {
                        strResult = "TID:" + strTid + "\n";
                    } else {
                        strResult = "";
                    }
                    Log.i("data", "EPC:" + res.getEPC() + "|" + strResult);
                    Message msg = handler.obtainMessage();
                    msg.obj = strResult + "EPC:" + res.getEPC() + "@" + res.getRssi();

                    handler.sendMessage(msg);
                }
            }
        }
    }

}
