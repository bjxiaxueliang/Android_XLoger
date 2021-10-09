package com.xlog.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.xlog.core.XLog;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        XLog.d(TAG, "---onCreate---");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.zipLogFiles_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                zipLogFiles();
            }
        });

        // exception 日志打印
        try {
            int tt = 5 / 0;
        } catch (Exception e) {
            XLog.d(TAG, "XLog Exception", e);
        }

    }

    @Override
    protected void onStart() {
        XLog.d(TAG, "---onStart---");
        super.onStart();
    }

    @Override
    protected void onResume() {
        XLog.d(TAG, "---onResume---");
        super.onResume();
    }

    @Override
    protected void onPause() {
        XLog.d(TAG, "---onPause---");
        super.onPause();
    }

    @Override
    protected void onStop() {
        XLog.d(TAG, "---onStop---");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        XLog.d(TAG, "---onDestroy---");
        super.onDestroy();
    }

    private void zipLogFiles() {
        XLog.d(TAG, "---zipLogFiles---");

        String filePath = XLog.zipLogFiles();
        if (!TextUtils.isEmpty(filePath)) {
            Toast.makeText(MainActivity.this, "日志文件生成成功：" + filePath,
                    Toast.LENGTH_LONG).show();
        }
    }


}
