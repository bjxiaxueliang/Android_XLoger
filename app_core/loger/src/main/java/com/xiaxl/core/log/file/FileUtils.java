package com.xiaxl.core.log.file;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 压缩文件
 */
public class FileUtils {

    private static final String TAG = "FileUtils";


    /**
     * 多个文件的压缩
     *
     * @param filePaths 文件列表
     * @param destZip   压缩后文件 例:abc.zip
     * @return
     */
    public static boolean exportZipFromPaths(List<String> filePaths, File destZip) {
        // 空判断
        if (filePaths == null || filePaths.isEmpty() || destZip == null) {
            return false;
        }
        // 获取 lastFiles 路径下的全部文件
        List<File> lastFiles = new ArrayList<>();
        int size = filePaths.size();
        for (int i = 0; i < size; i++) {
            exportFiles(lastFiles, filePaths.get(i));
        }
        // 压缩文件
        return exportZipFromFiles(lastFiles, destZip);
    }

    /**
     * 读取 path 路径下的全部文件
     *
     * @param exportFiles 输出文件列表
     * @param path        对应的输入路径
     */
    private static void exportFiles(List<File> exportFiles, String path) {
        if (TextUtils.isEmpty(path) || exportFiles == null) {
            return;
        }
        exportFiles(exportFiles, new File(path));
    }

    /**
     * 读取 file 路径下的全部文件
     *
     * @param exportFiles 输出文件列表
     * @param file        对应的输入路径
     */
    private static void exportFiles(List<File> exportFiles, File file) {
        // 空判断
        if (file == null || !file.exists() || exportFiles == null) {
            return;
        }
        // 路径文件
        if (!file.isDirectory()) {
            exportFiles.add(file);
        }
        // 文件列表
        else {
            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                return;
            }
            int length = files.length;
            for (int i = 0; i < length; i++) {
                exportFiles(exportFiles, files[i]);
            }
        }
    }

    /**
     * 多个文件的压缩
     *
     * @param list    文件列表
     * @param destZip 压缩后文件 例:abc.zip
     * @return
     */
    private static boolean exportZipFromFiles(List<File> list, File destZip) {
        // 空判断
        if (list == null || list.isEmpty() || destZip == null) {
            return false;
        }
        InputStream input = null;
        ZipOutputStream zipOut = null;
        //
        try {
            // 压缩文件存在，则删除
            if (destZip.exists()) {
                destZip.delete();
            }
            // 创建压缩文件
            try {
                destZip.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            // 创建文件目录
            if (!destZip.getParentFile().exists()) {
                destZip.getParentFile().mkdir();
            }
            // 压缩文件流
            zipOut = new ZipOutputStream(new FileOutputStream(destZip));
            //
            File file = null;
            byte[] buf = new byte[4096];
            //
            int length = list.size();
            for (int i = 0; i < length; i++) {
                file = list.get(i);
                if (!file.isDirectory()) {
                    input = new FileInputStream(file);
                    zipOut.putNextEntry(new ZipEntry(file.getParentFile().getAbsolutePath() + File.separator + file.getName()));
                    int readCount = 0;
                    while ((readCount = input.read(buf)) > 0) {
                        zipOut.write(buf, 0, readCount);
                    }
                    zipOut.closeEntry();
                    input.close();
                }
            }
            zipOut.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                if (zipOut != null) {
                    zipOut.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    // ########################文件 目录#########################

    /**
     * 获取应用私有cache目录
     * <p>
     * /sdcard/Android/data/包名/cache
     */
    public static String getAppCachePath(Context context) {
        File file = context.getExternalCacheDir();
        //先判断外部存储是否可用
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && file != null) {
            return file.getAbsolutePath();
        } else {
            return context.getCacheDir().getAbsolutePath();
        }
    }
}
