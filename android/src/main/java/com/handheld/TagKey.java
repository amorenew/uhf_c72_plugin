package com.handheld;

import java.util.Map;

public class TagKey {
    public final static String ID = "KEY_ID";
    public final static String EPC = "KEY_EPC";
    public final static String COUNT = "KEY_COUNT";

    public  static String getTag(Map<String, Object> map){
        return ((String) map.get(EPC));
    }
}
