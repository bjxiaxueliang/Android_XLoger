package com.xlog.demo;

import android.app.Application;

import com.xiaxl.log.XLog;

public class MainApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化日志
        XLog.init(MainApplication.this, true);
    }
}
