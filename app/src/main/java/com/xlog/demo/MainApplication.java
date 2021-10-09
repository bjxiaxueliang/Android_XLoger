package com.xlog.demo;

import android.app.Application;

import com.xlog.core.XLog;
import com.xlog.core.XLogCore;


public class MainApplication extends Application {

    private static final String TAG = "MainApplication";


    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化日志
        XLog.onAppCreate(MainApplication.this, true);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        // 程序在内存清理的时候执行
        XLog.onAppTrimMemory();
    }
}
