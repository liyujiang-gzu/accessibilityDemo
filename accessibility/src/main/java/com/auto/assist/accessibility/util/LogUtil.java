package com.auto.assist.accessibility.util;

import android.util.Log;

import com.auto.assist.BuildConfig;

public class LogUtil
{
    private static final String TAG = "accessibility";

    private static boolean isSHow = BuildConfig.DEBUG;

    public static  void error(String msg) {
        if (isSHow)
            Log.e(TAG, msg);
    }
    public static void debug(String msg) {
        if (isSHow)
            Log.w(TAG, msg);
    }

}
