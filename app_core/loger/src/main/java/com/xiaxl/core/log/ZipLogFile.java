package com.xiaxl.core.log;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


import com.xiaxl.core.log.base.PalLoger;
import com.xiaxl.core.log.file.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ZipLogFile {
    private static String TAG = "ZipLogFile";

    /**
     * 压缩日志文件 返回日志文件路径
     * <p>
     * 建议：异步任务中执行
     *
     * @param context
     * @return
     */
    public static File zipLogFiles(Context context) {
        Log.d(TAG, "---zipLogFiles---");
        if (context == null) {
            return null;
        }
        //
        String appFileDir = PalLoger.getAppLogDir();
        Log.d(TAG, "appFileDir: " + appFileDir);
        // 日志文件尚未初始化
        if (!TextUtils.isEmpty(appFileDir)) {
            // 当前时间作为文件名
            final String fileName = "xiaxl_log.zip";
            // 最终文件路径
            File zipFile = new File(FileUtils.getAppCachePath(context), fileName);
            Log.d(TAG, "zipFile: " + zipFile);
            //
            Log.d(TAG, "start zip");
            List<String> filePaths = new ArrayList<String>();
            filePaths.add(appFileDir);
            boolean flag = FileUtils.exportZipFromPaths(filePaths, zipFile);
            if (flag) {
                Log.d(TAG, "zip log file successfully");
                return zipFile;
            }
        }
        return null;
    }


}
