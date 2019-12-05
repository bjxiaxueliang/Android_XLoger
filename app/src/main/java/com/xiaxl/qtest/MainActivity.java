package com.xiaxl.qtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.xiaxl.core.log.PalNetLog;
import com.xiaxl.core.log.PalUiLog;
import com.xiaxl.core.log.ZipLogFile;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PalUiLog.d(TAG, "---onCreate---");
        PalNetLog.d(TAG, "---onCreate---");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.zipLogFiles_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                zipLogFiles();
            }
        });

    }

    @Override
    protected void onStart() {
        PalUiLog.d(TAG, "---onStart---");
        PalNetLog.d(TAG, "---onStart---");
        super.onStart();
    }

    @Override
    protected void onResume() {
        PalUiLog.d(TAG, "---onResume---");
        PalNetLog.d(TAG, "---onResume---");
        super.onResume();
    }

    @Override
    protected void onPause() {
        PalUiLog.d(TAG, "---onPause---");
        PalNetLog.d(TAG, "---onPause---");
        super.onPause();
    }

    @Override
    protected void onStop() {
        PalUiLog.d(TAG, "---onPause---");
        PalNetLog.d(TAG, "---onPause---");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        PalUiLog.d(TAG, "---onDestroy---");
        PalNetLog.d(TAG, "---onDestroy---");
        super.onDestroy();
    }

    private void zipLogFiles() {
        PalUiLog.d(TAG, "---zipLogFiles---");
        PalNetLog.d(TAG, "---zipLogFiles---");

        File file = ZipLogFile.zipLogFiles(MainActivity.this);
        if (file != null) {
            Toast.makeText(MainActivity.this, "日志文件生成成功：" + file.getAbsolutePath(),
                    Toast.LENGTH_LONG).show();
        }
    }


}
