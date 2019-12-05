package com.xiaxl.core.log;

import android.content.Context;

import com.xiaxl.core.log.base.PalLoger;
import com.xiaxl.core.log.base.PalLoggerFactory;


/**
 * create by xiaxl on 2019.12.05
 * <p>
 * UI 相关日志 用这个文件
 */
public class PalUiLog {

    private static PalLoger mLog = PalLoggerFactory.getLogger();

    private static final String LOG_FILE_PRE_NAME = "ui";

    static {
        mLog.setLogFilePreName(LOG_FILE_PRE_NAME);
    }

    public static void init(Context context, boolean isDebug) {
        if (mLog != null) {
            // 初始化
            mLog.init(context);
            // debug模式
            mLog.setDebug(isDebug);
        }
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
