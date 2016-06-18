package com.baiwanlu.android.hotfix.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by benren.fj on 6/12/16.
 */
public class FHotfixLog {
    private static boolean LOG_DEBUG = false;
    private static String TAG = "baiwanlu-hotfix";

    public static void setDebug(boolean debug) {
        LOG_DEBUG = debug;
    }

    public static void d(String msg) {
        if (LOG_DEBUG) {
            if (TextUtils.isEmpty(msg)) {
                return;
            }
            FHotfixLog.d(TAG, msg);
        }
    }

    public static void d(Object... messages) {
        if (LOG_DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Object msg : messages) {
                if (null == msg) {
                    continue;
                }
                stringBuilder.append(msg.toString());
            }
            if (stringBuilder.length() > 0) {
                FHotfixLog.d(TAG, stringBuilder.toString());
            }
        }
    }

    public static void d(String tag, String msg) {
        if (LOG_DEBUG) {
            if (TextUtils.isEmpty(msg)) {
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(Thread.currentThread().toString());
            stringBuilder.append(":");
            stringBuilder.append(msg);
            Log.d(tag, stringBuilder.toString());
        }
    }

    public static void e(String tag, String msg) {
        if (LOG_DEBUG) {
            if (TextUtils.isEmpty(msg)) {
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(Thread.currentThread().toString());
            stringBuilder.append(":");
            stringBuilder.append(msg);
            Log.e(tag, stringBuilder.toString());
        }
    }

    public static void e(String... messages) {
        if (LOG_DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String msg : messages) {
                stringBuilder.append(msg);
            }
            FHotfixLog.e(TAG, stringBuilder.toString());
        }
    }

    public static void e(String tag, Throwable throwable) {
        if (LOG_DEBUG) {
            Log.e(tag, Thread.currentThread().toString(), throwable);
        }
    }

    public static void e(Throwable throwable) {
        if (LOG_DEBUG) {
            FHotfixLog.e(TAG, throwable);
        }
    }
}
