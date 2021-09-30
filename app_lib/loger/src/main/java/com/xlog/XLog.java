package com.xiaxl.log;

import android.content.Context;

import com.xiaxl.log.core.XLoger;


/**
 * create by xiaxl on 2019.12.05
 * <p>
 * 网络日志
 */
public class XLog {
    //
    private static XLoger mLog = XLoger.getInstance();

    // 初始化
    public static void init(Context context, boolean isDebug) {
        mLog.init(context, isDebug);
    }

    // 压缩日志文件到sdcard中
    public static String zipLogFiles() {
        return mLog.zipLogFiles();
    }

    public static void v(String tag, String msg) {
        mLog.v(tag, msg);
    }

    public static void d(String tag, String msg) {
        mLog.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        mLog.i(tag, msg);
    }

    public static void w(String tag, String msg) {
        mLog.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        mLog.e(tag, msg);
    }
}
