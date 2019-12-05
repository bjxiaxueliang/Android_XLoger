package com.xiaxl.core.log.base;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 日志工具
 * 1、debug 为 true 时，展示在控制台；同时打印到文件中；
 * 2、debug 为 fase 时，只打印到文件中；
 * <p>
 * 文件目录：/data/data/%packetname%/files/
 */
public class PalLoger {
    // App可通过该tag 查看日志信息
    public static final String APP_TAG = "xiaxl";

    /**
     * config
     */
    // 日志输出级别
    private static final int LOG_LEVEL = Log.VERBOSE;
    // 日志文件的最大长度
    private static final int LOG_MAXSIZE = 6 * 1024 * 1024;
    // 文件名称
    private static final String LOG_FILE_TEMP = "log.temp";
    private static final String LOG_FILE_LAST = "log_last.txt";
    // 输出到控制台 or 输出到文件
    private static final int SHOW_LOG_CONSOLE = 0x01; // 输出到控制台
    private static final int SHOW_LOG_FILE = 0x10; // 输出到文件
    // debug模式：打印到控制台 和 文件
    // release模式：打印到文件
    private static final int MODEL_DEBUG = SHOW_LOG_CONSOLE | SHOW_LOG_FILE;
    private static final int MODEL_RELEASE = SHOW_LOG_FILE;
    // 单线程池
    private static ExecutorService mExecutorService = Executors.newFixedThreadPool(1);

    /**
     * 变量数据
     */
    // debug 开关
    private boolean DEBUG = true;

    // 变量锁
    private Object lockObj = new Object();
    //
    // /data/data/%packetname%/files/
    private static String mAppLogDir;
    // mLogFilePreName + netease_log_last.txt
    private String mLogFilePreName = "ui";
    //
    // 当前时间
    private Calendar mCalendar = Calendar.getInstance();
    //
    // 文件输入流
    private OutputStream mTempLogFileStream;
    // 当前文件大小
    private long mTempLogFileSize;


    // ###################################公共方法 begin##########################################

    /**
     * 构造方法
     */
    public PalLoger() {
    }

    /**
     * debug 模式开关
     *
     * @param isDebug
     */
    public void setDebug(boolean isDebug) {
        this.DEBUG = isDebug;
    }

    /**
     * 设置日志文件的 pre name
     *
     * @param prefix
     */
    public void setLogFilePreName(String prefix) {
        Log.d("PalLoger", "setLogPreName： " + prefix);
        if (prefix != null && !prefix.equals("")) {
            mLogFilePreName = prefix;
        }
    }


    /**
     * 初始化文件路径
     *
     * @param context
     */
    public void init(Context context) {
        Log.d("PalLoger", "---iniAppPath---");
        //
        synchronized (lockObj) {
            // /data/data/%packetname%/files/log/
            mAppLogDir = context.getFilesDir().getPath() + File.separator + "log" + File.separator;
            // 创建对应的路径
            File dir = new File(mAppLogDir);
            if (!dir.exists()) {
                dir.mkdir();
            }
            Log.d("PalLoger", "mAppLogDir: " + mAppLogDir);
        }
    }


    /**
     * 日志文件夹
     *
     * @return
     */
    public static String getAppLogDir() {
        Log.d("PalLoger", "getAppLogDir： " + mAppLogDir);
        return mAppLogDir;
    }


    // ###################################公共方法 begin##########################################


    public void d(String tagFromUser, String msgFromUser) {
        if (DEBUG) {
            showLog(tagFromUser, msgFromUser, MODEL_DEBUG, Log.DEBUG);
        } else {
            showLog(tagFromUser, msgFromUser, MODEL_RELEASE, Log.DEBUG);
        }
    }

    public void v(String tagFromUser, String msgFromUser) {
        if (DEBUG) {
            showLog(tagFromUser, msgFromUser, MODEL_DEBUG, Log.VERBOSE);
        } else {
            showLog(tagFromUser, msgFromUser, MODEL_RELEASE, Log.VERBOSE);
        }
    }

    public void e(String tagFromUser, String msgFromUser) {
        if (DEBUG) {
            showLog(tagFromUser, msgFromUser, MODEL_DEBUG, Log.ERROR);
        } else {
            showLog(tagFromUser, msgFromUser, MODEL_RELEASE, Log.ERROR);
        }
    }

    public void i(String tagFromUser, String msgFromUser) {
        if (DEBUG) {
            showLog(tagFromUser, msgFromUser, MODEL_DEBUG, Log.INFO);
        } else {
            showLog(tagFromUser, msgFromUser, MODEL_RELEASE, Log.INFO);
        }
    }

    public void w(String tagFromUser, String msgFromUser) {
        if (DEBUG) {
            showLog(tagFromUser, msgFromUser, MODEL_DEBUG, Log.WARN);
        } else {
            showLog(tagFromUser, msgFromUser, MODEL_RELEASE, Log.WARN);
        }
    }


    // ####################################日志打印#########################################


    /**
     * 打印到文件 或 打印到控制台
     *
     * @param tagFromUser
     * @param msgFromUser
     * @param outDest
     * @param level
     */
    private void showLog(String tagFromUser, String msgFromUser, int outDest, int level) {
        if (mAppLogDir == null) {
            Log.e("PalLoger", "PalLoger need init");
            return;
        }
        if (tagFromUser == null) {
            tagFromUser = "TAG_NULL";
        }
        if (msgFromUser == null) {
            msgFromUser = "MSG_NULL";
        }
        // 日志级别
        if (level >= LOG_LEVEL) {
            // 输出到控制台
            if ((outDest & SHOW_LOG_CONSOLE) != 0) {
                logToConsole(tagFromUser, msgFromUser, level);
            }
            // 输出到文件
            if ((outDest & SHOW_LOG_FILE) != 0) {
                //
                final String tagFromUser2File = tagFromUser;
                final String msgFromUser2File = msgFromUser;
                //
                try {
                    if (mExecutorService != null) {
                        mExecutorService.submit(new Runnable() {
                            public void run() {
                                PalLoger.this.logToFile(tagFromUser2File, msgFromUser2File);
                            }
                        });
                    }
                } catch (Exception var9) {
                    Log.e("PalLoger", "log -> " + var9.toString());
                }
            }
        }
    }

    /**
     * 将log打到控制台
     *
     * @param tagFromUser
     * @param msgFromUser
     * @param level
     */
    private void logToConsole(String tagFromUser, String msgFromUser, int level) {
        switch (level) {
            case Log.DEBUG:
                Log.d(APP_TAG, getConsoleLogMsg(tagFromUser, msgFromUser));
                break;
            case Log.ERROR:
                Log.e(APP_TAG, getConsoleLogMsg(tagFromUser, msgFromUser));
                break;
            case Log.INFO:
                Log.i(APP_TAG, getConsoleLogMsg(tagFromUser, msgFromUser));
                break;
            case Log.VERBOSE:
                Log.v(APP_TAG, getConsoleLogMsg(tagFromUser, msgFromUser));
                break;
            case Log.WARN:
                Log.w(APP_TAG, getConsoleLogMsg(tagFromUser, msgFromUser));
                break;
            default:
                break;
        }
    }

    /**
     * 组合用户 tagFromUser 与 msgFromUser
     *
     * @param msgFromUser
     * @return
     */
    private String getConsoleLogMsg(String tagFromUser, String msgFromUser) {
        StringBuffer sb = new StringBuffer();
        sb.append(tagFromUser);
        sb.append(": ");
        sb.append(msgFromUser);
        return sb.toString();
    }

    /**
     * 将log打到文件日志
     *
     * @param tagFromUser
     * @param msgFromUser
     */
    private void logToFile(String tagFromUser, String msgFromUser) {
        synchronized (lockObj) {
            // 输入流
            OutputStream tempLogFileStream = openTempFileOutStream();
            //
            if (tempLogFileStream != null) {
                try {
                    // 待输入的数据
                    byte[] d = getFileLogMsg(tagFromUser, msgFromUser).getBytes("utf-8");
                    // 写入
                    if (mTempLogFileSize < LOG_MAXSIZE) {
                        tempLogFileStream.write(d);
                        tempLogFileStream.write("\r\n".getBytes());
                        tempLogFileStream.flush();
                        mTempLogFileSize += d.length;
                    }
                    // 大小超出
                    else {
                        // 关闭 temp 读写流
                        closeTempFileOutStream();
                        // temp ——> last 重命名
                        renameTemp2Last();
                        /**
                         * 重新创建 temp文件，并向 temp文件 写入数据
                         */
                        logToFile(tagFromUser, msgFromUser);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("PalLoger", "Log File open fail: [AppPath]=" + mAppLogDir
                        + ",[LogName]:" + mLogFilePreName);
            }
        }
    }

    /**
     * /data/data/%packetname%/files/mLogFilePrefix_name
     *
     * @param name
     * @return
     */
    private File getNameFile(String name) {
        // 路径空
        if (mAppLogDir == null || mAppLogDir.length() == 0) {
            Log.e("PalLoger", "PalLoger should init");
            return null;
        }
        // 打开对应文件
        else {
            File file = new File(mAppLogDir + mLogFilePreName + "_" + name);
            return file;
        }
    }


    /**
     * 组成Log字符串.添加时间信息.
     *
     * @param tagFromUser
     * @param msgFromUser
     * @return
     */
    private String getFileLogMsg(String tagFromUser, String msgFromUser) {
        //
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        //
        StringBuffer sb = new StringBuffer();
        // 日期 + AppTag
        sb.append("[");
        sb.append(mCalendar.get(Calendar.YEAR));
        sb.append("-");
        sb.append(mCalendar.get(Calendar.MONTH) + 1);
        sb.append("-");
        sb.append(mCalendar.get(Calendar.DATE));
        sb.append(" ");
        sb.append(mCalendar.get(Calendar.HOUR_OF_DAY));
        sb.append(":");
        sb.append(mCalendar.get(Calendar.MINUTE));
        sb.append(":");
        sb.append(mCalendar.get(Calendar.SECOND));
        sb.append(":");
        sb.append(mCalendar.get(Calendar.MILLISECOND));
        sb.append(" ");
        sb.append(APP_TAG);
        sb.append(" ");
        sb.append("] ");
        // msg
        sb.append(tagFromUser);
        sb.append(": ");
        sb.append(msgFromUser);
        //
        return sb.toString();
    }


    /**
     * 获取日志临时文件输入流
     *
     * @return
     */
    private OutputStream openTempFileOutStream() {
        if (mTempLogFileStream == null) {
            try {
                // 没有初始化
                if (mAppLogDir == null || mAppLogDir.length() == 0) {
                    Log.e("PalLoger", "PalLoger should init");
                    return null;
                }
                // /data/data/%packetname%/files/mLogFilePreName+netease_log.temp
                File file = getNameFile(LOG_FILE_TEMP);
                // 文件为null
                if (file == null) {
                    Log.e("PalLoger", "LOG_FILE_TEMP is null");
                    return null;
                }
                // 文件存在
                if (file.exists()) {
                    mTempLogFileStream = new FileOutputStream(file, true);
                    // 当前文件大小
                    mTempLogFileSize = file.length();
                } else {
                    // file.createNewFile();
                    mTempLogFileStream = new FileOutputStream(file);
                    // 当前文件大小
                    mTempLogFileSize = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("PalLoger", "openTempFileOutStream exception: " + e.getMessage());
            }
        }
        return mTempLogFileStream;
    }

    /**
     * 关闭日志输出流
     */
    private void closeTempFileOutStream() {
        try {
            if (mTempLogFileStream != null) {
                mTempLogFileStream.close();
                mTempLogFileStream = null;
                mTempLogFileSize = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * netease_log.temp 重命名为 netease_log_last.txt
     */
    private void renameTemp2Last() {
        synchronized (lockObj) {
            File tempFile = getNameFile(LOG_FILE_TEMP);
            File lastFile = getNameFile(LOG_FILE_LAST);
            // 删除上次的
            if (lastFile.exists()) {
                lastFile.delete();
            }
            // 重命名
            tempFile.renameTo(lastFile);
        }
    }
}
