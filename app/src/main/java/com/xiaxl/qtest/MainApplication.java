package com.xiaxl.qtest;

import android.app.Application;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.xiaxl.core.log.PalNetLog;
import com.xiaxl.core.log.PalUiLog;
import com.xiaxl.core.log.ZipLogFile;

import java.io.File;

public class MainApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        //
        initLog();
    }

    /**
     * 初始化日志
     */
    private void initLog() {
        PalUiLog.init(MainApplication.this, true);
        PalNetLog.init(MainApplication.this, true);
    }
}
