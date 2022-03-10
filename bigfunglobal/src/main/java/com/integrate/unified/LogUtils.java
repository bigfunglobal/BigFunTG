package com.integrate.unified;

import android.util.Log;

public class LogUtils {
    private static final String TAG = "BigFunSDK>>>>";

    private LogUtils() {

    }

    public static void log(String message) {
        if (!LzWuSuptLoad.isDebug) {
            return;
        }
         Log.d(TAG, "" + message);
    }
}
