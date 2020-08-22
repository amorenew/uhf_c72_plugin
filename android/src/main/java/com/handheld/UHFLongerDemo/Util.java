package com.handheld.UHFLongerDemo;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import java.util.HashMap;
import java.util.Map;

public class Util {
    public static Context context;
    public static SoundPool sp;
    public static Map<Integer, Integer> suondMap;

    public static void initSoundPool(Context context2) {
        /*context = context2;
        sp = new SoundPool(1, 3, 1);
        suondMap = new HashMap();
        suondMap.put(1, Integer.valueOf(sp.load(context2, R.raw.msg, 1)));*/
    }

    public static void play(int sound, int number) {
       /* Context context2 = context;
        Context context3 = context;
        AudioManager am = (AudioManager) context2.getSystemService("audio");
        float streamVolume = ((float) am.getStreamVolume(3)) / ((float) am.getStreamMaxVolume(3));
        sp.play(1, 1.0f, 1.0f, 0, 0, 1.0f);*/
    }
}
