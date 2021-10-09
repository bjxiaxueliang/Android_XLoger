package com.xlog.core;

import android.content.Context;


/**
 * create by xiaxl on 2019.12.05
 * <p>
 * 日志
 */
public class XLog {
    //
    private static XLogCore mLog = XLogCore.getInstance();

    // application进入 onCreate 时调用
    public static void onAppCreate(Context context, boolean isDebug) {
        mLog.onAppCreate(context, isDebug ? XLogCore.XLogModel.MODEL_DEBUG : XLogCore.XLogModel.MODEL_RELEASE);
    }

    // application进入 onLowMemory 时调用
    public static void onAppTrimMemory() {
        mLog.onAppTrimMemory();
    }

    // 日志文件在 未进行压缩时 ，存储于App内部存储 /data/data/包名/files/xlog 路径下。
    // 日志文件 进行压缩后 ，存储到Sdcard存储 /sdcard/Android/data/包名/file/zip_log 路径下。
    public static String zipLogFiles() {
        return mLog.zipLogFiles();
    }

    // 打印日志
    public static void v(String tag, String msg) {
        mLog.v(tag, msg);
    }

    public static void v(String tag, String msg, Throwable throwable) {
        mLog.v(tag, msg, throwable);
    }

    public static void d(String tag, String msg) {
        mLog.d(tag, msg);
    }

    public static void d(String tag, String msg, Throwable throwable) {
        mLog.d(tag, msg, throwable);
    }

    public static void i(String tag, String msg) {
        mLog.i(tag, msg);
    }

    public static void i(String tag, String msg, Throwable throwable) {
        mLog.i(tag, msg, throwable);
    }

    public static void w(String tag, String msg) {
        mLog.w(tag, msg);
    }

    public static void w(String tag, String msg, Throwable throwable) {
        mLog.w(tag, msg, throwable);
    }

    public static void e(String tag, String msg) {
        mLog.e(tag, msg);
    }

    public static void e(String tag, String msg, Throwable throwable) {
        mLog.e(tag, msg, throwable);
    }
}
