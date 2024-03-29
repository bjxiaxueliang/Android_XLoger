# Android日志输出到控制台、文件（XLog开源日志工具）

Android项目开发中，需要将Log同时输出到`控制台` 与 `文件`，并要求`将日志文件压缩`并`上传到远程服务端`（便于分析App使用过程中用户反馈的Bug，进行问题定位与修复）：

+ 打印到控制台：
软件研发阶段，方便Debug调试；
+ 打印到文件：
软件上线后，方便线上问题的跟踪分析；
+ 日志文件压缩上传：
用户反馈遇到的Bug后，以此文件分析用户用户的操作行为，从而修复Bug。


**本文介绍的一个开源日志工具为`XLog`。**
**XLog具备如下两种模式：**

+ Debug模式：
日志输出到控制台，同时`输出`到`App内置存储路径下`的文件中；
+ Release模式：
日志输出到`App内置存储路径下`的文件中，同时`禁止`控制台日志打印；

**Debug模式 / Release模式均支持如下功能：**
+ 日志压缩上传：
支持`日志压缩上传`，便于对用户反馈的Bug进行定位分析。
+ 日志关键词筛选：
支持通过`日志关键词进行筛选`，默认为`XLogCore`。
+ Exception错误信息打印：
支持`Exception错误信息打印`。

## 一、效果举例

### 1.1 日志 输出到 控制台
日志输出到控制台，同时支持`日志筛选关键词`、`打印Excetption日志`。
![在这里插入图片描述](https://img-blog.csdnimg.cn/188f9ad7f754432ab033323f1062a91b.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBAYmp4aWF4dWVsaWFuZw==,size_20,color_FFFFFF,t_70,g_se,x_16#pic_center)
### 1.2 日志 输出到 文件
日志默认输出到，同时支持`日志筛选关键词`、`打印Excetption日志`。

+ 日志文件在 `未进行压缩时 `，存储于App内部存储 `/data/data/包名/files/xlog` 路径下。
![在这里插入图片描述](https://img-blog.csdnimg.cn/87daa758364e4d45b2ad9c35f5245176.png#pic_center)

+ 日志文件 `进行压缩后 `，存储到Sdcard存储 `/sdcard/Android/data/包名/file/zip_log` 路径下。
![在这里插入图片描述](https://img-blog.csdnimg.cn/6d3518598f474b4a8595a5ebc71dc6c9.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBAYmp4aWF4dWVsaWFuZw==,size_14,color_FFFFFF,t_70,g_se,x_16#pic_center)

+ 写入日志文件的数据如下：
![请添加图片描述](https://img-blog.csdnimg.cn/bae8ec26dc9c48e69901ccfbddd3fb08.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBAYmp4aWF4dWVsaWFuZw==,size_20,color_FFFFFF,t_70,g_se,x_16)

## 二、使用方式


+ XLog 初始化
+ XLog 打印日志
+ XLog 文件压缩上传


### 2.1、XLog 初始化

Application的onCreate方法中进行XLog初始化；
Application的onTrimMemory方法中进行文件IO流的关闭。

```java
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Application的onCreate方法中进行XLog初始化
        // 初始化日志（debug模式 日志输出到）
        XLog.onAppCreate(MainApplication.this, true);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        // Application的onTrimMemory方法中进行文件IO流的关闭
        // 程序在内存清理的时候执行
        XLog.onAppTrimMemory();
    }
}
```

### 2.2、XLog 打印日志


```java
// 打印到控制台；同时打印到文件；
XLog.d(TAG, "---onCreate---");

// 打印 Exception 信息
try {
    int tt = 5 / 0;
} catch (Exception e) {
    XLog.d(TAG, "XLog Exception", e);
}
```

### 2.3、XLog 文件压缩上传


```java
// 文件压缩，并返回压缩后的文件路径
String filePath = XLog.zipLogFiles();
// toast 文件路径
if (!TextUtils.isEmpty(filePath)) {
    Toast.makeText(MainActivity.this, "日志文件生成成功：" + filePath,
            Toast.LENGTH_LONG).show();
}
```

## 三、源码

Github源码地址：
[https://github.com/xiaxveliang/Android_XLoger](https://github.com/xiaxveliang/Android_XLoger)


XLog.java

```java
/**
 * create by xiaxl on 2019.12.05
 * <p>
 * 日志
 */
public class XLog {
    //
    private static XLogCore mLog = XLogCore.getInstance();

    // application进入 onCreate 时调用
    public static void onAppCreate(Context context, boolean isDebug) {
        mLog.onAppCreate(context, isDebug ? XLogCore.XLogModel.MODEL_DEBUG : XLogCore.XLogModel.MODEL_RELEASE);
    }
    // application进入 onLowMemory 时调用
    public static void onAppTrimMemory() {
        mLog.onAppTrimMemory();
    }

    // 日志文件在 未进行压缩时 ，存储于App内部存储 /data/data/包名/files/xlog 路径下。
    // 日志文件 进行压缩后 ，存储到Sdcard存储 /sdcard/Android/data/包名/file/zip_log 路径下。
    public static String zipLogFiles() {
        return mLog.zipLogFiles();
    }

    // 打印日志
    public static void v(String tag, String msg) { mLog.v(tag, msg); }
    public static void v(String tag, String msg, Throwable throwable) { mLog.v(tag, msg, throwable); }

    public static void d(String tag, String msg) { mLog.d(tag, msg); }
    public static void d(String tag, String msg, Throwable throwable) { mLog.d(tag, msg, throwable); }

    public static void i(String tag, String msg) { mLog.i(tag, msg); }
    public static void i(String tag, String msg, Throwable throwable) { mLog.i(tag, msg, throwable); }

    public static void w(String tag, String msg) { mLog.w(tag, msg); }
    public static void w(String tag, String msg, Throwable throwable) { mLog.w(tag, msg, throwable); }

    public static void e(String tag, String msg) { mLog.e(tag, msg); }
    public static void e(String tag, String msg, Throwable throwable) { mLog.e(tag, msg, throwable); }
}
```


XLoger.java
```java

/**
 * 日志工具
 * 1、debug 为 true 时，展示在控制台；同时缓存到内部缓存的xlog文件中；
 * 2、debug 为 fase 时，只缓存到内部缓存的xlog文件中；
 * <p>
 * Log文件缓存目录：
 * /data/data/%packetname%/files/
 */
public class XLogCore {


    /**
     * 配置信息
     */
    private static class XLogConfig {

        /**
         * App可通过该tag 查看筛选日志信息
         */
        private static final String XLOG_TAG = "XLogCore";

        /**
         * 日志文件的构成：xlog_20201111_111111.txt
         */
        // 日志缓存文件夹
        private static final String XLOG_FILE_DIR = "xlog";
        // 日志归档文件夹（日志超过一定大小后，归档到该路径）
        private static final String XLOG_FILING_DIR = "filing";
        // 日志文件的最大长度(字节Byte) 15M
        private static final int XLOG_FILE_MAXSIZE = 30 * 1024 * 1024;
        // 默认 pre name
        private static final String XLOG_FILE_NAME_PRE = "xlog_";
        // 日志文件扩展名
        private static final String XLOG_FILE_NAME_FORMAT = ".txt";

        /**
         * 输出控制
         */
        // 输出到控制台 or 输出到文件
        private static final int SHOW_LOG_CONSOLE = 0x01; // 输出到控制台
        private static final int SHOW_LOG_FILE = 0x10; // 输出到文件

        /**
         * 单线程池（用于异步写文件）
         */
        private static ExecutorService mExecutorService = Executors.newFixedThreadPool(1);

        /**
         * 日志压缩后的文件夹名称
         */
        // 压缩后的缓存文件 文件夹名称
        private static final String XLOG_ZIP_DIR = "xlog_zip";
    }

    // 注解仅存在于源码中，在class字节码文件中不包含
    @Retention(RetentionPolicy.SOURCE)
    // 限定取值范围为{MODEL_DEBUG, MODEL_RELEASE}
    @IntDef({XLogModel.MODEL_DEBUG, XLogModel.MODEL_RELEASE})
    public @interface XLogModel {
        // debug模式：打印到控制台 和 文件
        int MODEL_DEBUG = XLogConfig.SHOW_LOG_CONSOLE | XLogConfig.SHOW_LOG_FILE;
        // release模式：打印到文件
        int MODEL_RELEASE = XLogConfig.SHOW_LOG_FILE;
    }


    /**
     * 当前文件信息记录
     */
    public static class XLogInfoBean {
        // 文件名 xlog_yyyyMMdd_HHmmss.txt
        public String xLogName = "";
        // 文件大小（字节大小）
        public long xLogSize = 0;
    }


    // #############################################################################


    /**
     * 内部变量
     */

    private Context mContext;
    // 文件读写锁
    private Object mLockObj = new Object();
    // 日志打印级别
    private final int mLogPrintLevel = Log.VERBOSE;
    //
    // 日志缓存路径（内部缓存）：/data/data/%packetname%/files/xlog
    private String mLogFileDir;
    // 日志文件归档路径
    private String mLogFilingDir;

    /**
     * 用户变量
     */
    // debug 开关 (默认为debug模式)
    @XLogCore.XLogModel
    private int mDebugModel = XLogModel.MODEL_DEBUG;
    // 当前文件信息记录
    private XLogInfoBean mCurrLogFileInfo = null;
    // 文件IO
    private OutputStream mCurrLogFileStream = null;


    /**
     * 单例
     */
    private static volatile XLogCore instance;

    private XLogCore() {

    }

    public static XLogCore getInstance() {
        if (instance == null) {
            synchronized (XLogCore.class) {
                if (instance == null) {
                    instance = new XLogCore();
                }
            }
        }
        return instance;
    }

    // #############################################################################

    /**
     * application进入 onCreate 时调用
     *
     * @param context    application的context
     * @param debugModel debug or release
     */
    public void onAppCreate(Context context, @XLogCore.XLogModel int debugModel) {
        if (context == null) {
            Log.e("XLoger", "context need init");
            return;
        }
        this.mContext = context;
        this.mDebugModel = debugModel;

        /**
         * 读取data/data下缓存日志文件
         */
        loadLogFileInfo();
    }

    /**
     * application进入 onTrimMemory 时调用
     */
    public void onAppTrimMemory() {
        closeFileStream();
    }


    /**
     * 压缩日志文件 返回压缩后的文件存储路径
     * <p>
     * 建议：异步任务中执行
     * <p>
     * 日志文件在 未进行压缩时 ，存储于App内部存储 /data/data/包名/files/xlog 路径下。
     * 日志文件 进行压缩后 ，存储到Sdcard存储 /sdcard/Android/data/包名/file/zip_log 路径下。
     *
     * @return
     */
    public String zipLogFiles() {
        if (mContext == null) {
            Log.e("XLoger", "context need init");
            return "";
        }
        if (TextUtils.isEmpty(mLogFileDir)) {
            Log.e("XLoger", "mLogFileDir isEmpty");
            return "";
        }

        String logFileDir = mLogFileDir;
        //
        if (!TextUtils.isEmpty(logFileDir)) {
            // 当前时间作为 zip文件 的文件名称
            final String desZipFilePath = XLogStoreUtil.genDesZipFilePath(mContext);
            File desZipFile = new File(desZipFilePath);
            // 内部缓存路径下 log 文件的缓存路径
            List<String> srcFilePaths = new ArrayList<String>();
            srcFilePaths.add(logFileDir);
            // 压缩文件
            boolean flag = ZipFileUtil.exportZipFromPaths(srcFilePaths, desZipFile);
            // 压缩成功，则返回对应的文件存储路径
            if (flag) {
                return desZipFilePath;
            }
        }
        return "";
    }


    // #############################################################################


    public void v(String tagFromUser, String msgFromUser) {
        XLogCore.this.v(tagFromUser, msgFromUser, null);
    }

    public void v(String tagFromUser, String msgFromUser, Throwable throwable) {
        showLog(tagFromUser, msgFromUser, throwable, mDebugModel, Log.VERBOSE);
    }

    public void d(String tagFromUser, String msgFromUser) {
        XLogCore.this.d(tagFromUser, msgFromUser, null);
    }

    public void d(String tagFromUser, String msgFromUser, Throwable throwable) {
        showLog(tagFromUser, msgFromUser, throwable, mDebugModel, Log.DEBUG);
    }

    public void i(String tagFromUser, String msgFromUser) {
        XLogCore.this.i(tagFromUser, msgFromUser, null);
    }

    public void i(String tagFromUser, String msgFromUser, Throwable throwable) {
        showLog(tagFromUser, msgFromUser, throwable, mDebugModel, Log.INFO);
    }

    public void w(String tagFromUser, String msgFromUser) {
        XLogCore.this.w(tagFromUser, msgFromUser, null);
    }

    public void w(String tagFromUser, String msgFromUser, Throwable throwable) {
        showLog(tagFromUser, msgFromUser, throwable, mDebugModel, Log.WARN);
    }

    public void e(String tagFromUser, String msgFromUser) {
        XLogCore.this.e(tagFromUser, msgFromUser, null);
    }

    public void e(String tagFromUser, String msgFromUser, Throwable throwable) {
        showLog(tagFromUser, msgFromUser, throwable, mDebugModel, Log.ERROR);
    }


    // #############################################################################


    /**
     * 打印到文件 或 打印到控制台
     *
     * @param tagFromUser
     * @param msgFromUser
     * @param xLogModel     @XLoger.XLogModel
     * @param logPrintLevel log打印级别
     */
    private void showLog(String tagFromUser, String msgFromUser, Throwable throwable,
                         @XLogCore.XLogModel int xLogModel, int logPrintLevel) {
        if (mContext == null) {
            Log.e("XLoger", "context need init");
            return;
        }
        if (TextUtils.isEmpty(mLogFileDir)) {
            Log.e("XLoger", "mLogFileDir isEmpty");
            return;
        }
        if (TextUtils.isEmpty(tagFromUser)) {
            tagFromUser = "USER_TAG_NULL";
        }
        if (TextUtils.isEmpty(msgFromUser)) {
            msgFromUser = "USER_MSG_NULL";
        }
        if (logPrintLevel < mLogPrintLevel) {
            return;
        }
        // 输出到控制台
        if ((xLogModel & XLogConfig.SHOW_LOG_CONSOLE) != 0) {
            logToConsole(tagFromUser, msgFromUser, throwable, logPrintLevel);
        }
        // 输出到文件
        if ((xLogModel & XLogConfig.SHOW_LOG_FILE) != 0) {
            //
            final String tagFromUser2File = tagFromUser;
            final String msgFromUser2File = msgFromUser;
            final Throwable throwable2File = throwable;
            try {
                if (XLogConfig.mExecutorService != null) {
                    XLogConfig.mExecutorService.submit(new Runnable() {
                        public void run() {
                            XLogCore.this.logToFile(tagFromUser2File, msgFromUser2File, throwable2File);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // #############################################################################

    /**
     * 将log打到控制台
     *
     * @param tagFromUser
     * @param msgFromUser
     * @param level
     */
    private void logToConsole(String tagFromUser, String msgFromUser, Throwable throwable, int level) {
        // 日志打印
        String logTag = XLogConfig.XLOG_TAG;
        String logMsg = getConsoleLogMsg(tagFromUser, msgFromUser, throwable);
        switch (level) {
            case Log.DEBUG:
                Log.d(logTag, logMsg);
                break;
            case Log.ERROR:
                Log.e(logTag, logMsg);
                break;
            case Log.INFO:
                Log.i(logTag, logMsg);
                break;
            case Log.VERBOSE:
                Log.v(logTag, logMsg);
                break;
            case Log.WARN:
                Log.w(logTag, logMsg);
                break;
        }
    }

    /**
     * 组合用户 tagFromUser + msgFromUser + Exception
     *
     * @param msgFromUser
     * @return
     */
    private String getConsoleLogMsg(String tagFromUser, String msgFromUser, Throwable throwable) {
        StringBuffer sb = new StringBuffer();
        sb.append(tagFromUser);
        sb.append(": ");
        sb.append(msgFromUser);
        if (throwable != null) {
            sb.append("\n");
            sb.append(Log.getStackTraceString(throwable));
        }
        return sb.toString();
    }

    // #############################################################################

    /**
     * 读取data/data下缓存日志文件信息
     */
    private void loadLogFileInfo() {
        // 路径空
        if (mContext == null) {
            Log.e("XLoger", "context need init");
            return;
        }
        // 异步读取文件信息
        try {
            if (XLogConfig.mExecutorService != null) {
                XLogConfig.mExecutorService.submit(new Runnable() {
                    public void run() {
                        synchronized (mLockObj) {
                            // 文件路径（内部存储路径）
                            mLogFileDir = XLogStoreUtil.getLogFileDir(mContext);
                            mLogFilingDir = XLogStoreUtil.getLogFilingDir(mContext);
                            // 从对应文件夹中 读取 缓存的xlog文件信息
                            mCurrLogFileInfo = XLogStoreUtil.readXLogInfoFromPath(mLogFileDir);
                            // 路径下 文件未读取到
                            if (mCurrLogFileInfo == null) {
                                initXLogFileInfo();
                            }
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 将log打到文件日志
     *
     * @param tagFromUser
     * @param msgFromUser
     */
    private void logToFile(String tagFromUser, String msgFromUser, Throwable throwable) {
        // 路径空
        if (mContext == null) {
            Log.e("XLoger", "context need init");
            return;
        }
        synchronized (mLockObj) {
            // 要写入文件的日志信息
            String printLogInfo = getFileLogMsg(tagFromUser, msgFromUser, throwable);
            try {
                // 待输入的数据
                byte[] d = printLogInfo.getBytes("utf-8");
                //
                // Log文件的缓存路径 /data/data/%packetname%/files/xlog_yyyyMMdd_HHmmss.txt
                String logFilePath = XLogStoreUtil.getLogFilePath(mContext, mCurrLogFileInfo.xLogName);
                File logFile = new File(logFilePath);

                /**
                 * 文件归档
                 */
                if (mCurrLogFileInfo.xLogSize > XLogConfig.XLOG_FILE_MAXSIZE) {
                    // 文件归档
                    String logFilingPath = XLogStoreUtil.getLogFilingPath(mContext, mCurrLogFileInfo.xLogName);
                    File logFilingFile = new File(logFilingPath);
                    logFile.renameTo(logFilingFile);
                    /**
                     * 生成新的文件路径
                     */
                    resetLogFileInfo();
                    /**
                     * 向新文件路径中写入数据
                     */
                    logToFile(tagFromUser, msgFromUser, throwable);
                    return;
                }
                /**
                 * 创建文件IO流
                 */
                if (mCurrLogFileStream == null) {
                    // 文件存在
                    if (logFile.exists()) {
                        mCurrLogFileStream = new FileOutputStream(logFile, true);
                    } else {
                        mCurrLogFileStream = new FileOutputStream(logFile);
                    }
                }
                /**
                 * 写入日志
                 */
                mCurrLogFileStream.write(d);
                mCurrLogFileStream.write("\r\n".getBytes());
                mCurrLogFileStream.flush();
                mCurrLogFileInfo.xLogSize += d.length;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 组成Log字符串.添加时间信息.
     *
     * @param tagFromUser
     * @param msgFromUser
     * @return
     */
    private String getFileLogMsg(String tagFromUser, String msgFromUser, Throwable throwable) {
        // 日期 + 筛选tag + msg
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        // 添加当前日期
        sb.append(TimeUtil.getFormatCurrTime());
        sb.append(" ");
        // 添加筛选tag
        sb.append(XLogConfig.XLOG_TAG);
        sb.append(" ");
        sb.append("] ");
        // 添加用户tag + 日志msg + Exception
        sb.append(getConsoleLogMsg(tagFromUser, msgFromUser, throwable));
        return sb.toString();
    }


    // #############################################################################


    private void initXLogFileInfo() {

        /**
         * 创建日志文件路径
         */
        File fileDir = new File(mLogFileDir);
        File filingDir = new File(mLogFilingDir);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        if (!filingDir.exists()) {
            filingDir.mkdir();
        }
        /**
         * 生成日志文件信息
         */
        resetLogFileInfo();
    }

    private void resetLogFileInfo() {
        if (mCurrLogFileInfo == null) {
            mCurrLogFileInfo = new XLogInfoBean();
        }
        // 创建新的缓存文件
        mCurrLogFileInfo.xLogName = XLogStoreUtil.genLogFileNameByCurrTime();
        mCurrLogFileInfo.xLogSize = 0;
        // 关闭文件流
        closeFileStream();
    }

    private void closeFileStream() {
        // 关闭文件流
        try {
            if (mCurrLogFileStream != null) {
                mCurrLogFileStream.close();
                mCurrLogFileStream = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // #############################################################################


    public static class XLogStoreUtil {

        // ####################################


        /**
         * 从对应文件夹中 读取 缓存的xlog文件信息
         *
         * @param logFileDir
         * @return
         */
        public static XLogInfoBean readXLogInfoFromPath(String logFileDir) {
            // 数据bean
            XLogInfoBean xLogInfoBean = null;
            // 读取缓存路径中已存在的log文件
            File fileDir = new File(logFileDir);
            if (fileDir.exists()) {
                final File[] files = fileDir.listFiles();
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    if (file.isFile()) {
                        String fileName = file.getName();
                        long fileSize = file.length();
                        if (!TextUtils.isEmpty(fileName)
                                && fileName.startsWith(XLogConfig.XLOG_FILE_NAME_PRE)
                                && fileName.endsWith(XLogConfig.XLOG_FILE_NAME_FORMAT)) {
                            // 赋值
                            xLogInfoBean = new XLogInfoBean();
                            xLogInfoBean.xLogName = fileName;
                            xLogInfoBean.xLogSize = fileSize;
                            break;
                        }
                    }
                }
            }
            return xLogInfoBean;

        }


        /**
         * 组装：日志缓存文件夹 路径
         *
         * @param context
         * @return
         */
        public static String getLogFileDir(Context context) {
            String logFileDir = "";
            if (context == null) {
                return logFileDir;
            }
            // 创建log缓存文件路径
            // /data/data/%packetname%/files/xlog/
            StringBuffer sb = new StringBuffer();
            sb.append(context.getFilesDir().getPath());
            sb.append(File.separator);
            sb.append(XLogConfig.XLOG_FILE_DIR);
            logFileDir = sb.toString();
            return logFileDir;
        }

        /**
         * 组装：日志缓存文件路径
         * /data/data/%packetname%/files/xlog/xlog_20201111_111111.txt
         *
         * @param fileName xlog_20201111_111111.txt
         * @return
         */
        public static String getLogFilePath(Context context, String fileName) {

            // 创建log缓存文件路径
            // /data/data/%packetname%/files/log/
            StringBuffer sb = new StringBuffer();
            sb.append(XLogStoreUtil.getLogFileDir(context));
            sb.append(File.separator);
            sb.append(fileName);
            return sb.toString();
        }

        /**
         * 组装：日志归档文件夹 路径（文件超过一定大小后归档到该路径下）
         *
         * @param context
         * @return
         */
        public static String getLogFilingDir(Context context) {
            if (context == null) {
                return "";
            }
            StringBuffer sb = new StringBuffer();
            sb.append(XLogStoreUtil.getLogFileDir(context));
            sb.append(File.separator);
            sb.append(XLogConfig.XLOG_FILING_DIR);
            return sb.toString();
        }


        /**
         * 组装：日志归档文件路径
         * /data/data/%packetname%/files/xlog/xlog_20201111_111111.txt
         *
         * @param fileName xlog_20201111_111111.txt
         * @return
         */
        public static String getLogFilingPath(Context context, String fileName) {

            // 创建log缓存文件路径
            // /data/data/%packetname%/files/log/
            StringBuffer sb = new StringBuffer();
            sb.append(XLogStoreUtil.getLogFilingDir(context));
            sb.append(File.separator);
            sb.append(fileName);
            return sb.toString();
        }


        /**
         * 举例 xlog_20201111_111111.txt
         *
         * @return
         */
        public static String genLogFileNameByCurrTime() {
            String currTime = TimeUtil.getFormatCurrTime();
            // 生成文件路径
            StringBuffer sb = new StringBuffer();
            // file name
            sb.append(XLogConfig.XLOG_FILE_NAME_PRE);
            sb.append(currTime);
            sb.append(XLogConfig.XLOG_FILE_NAME_FORMAT);
            // 打开对应文件
            return sb.toString();
        }

        /**
         * 压缩后的日志文件缓存路径：
         * /sdcard/Android/data/包名/file/zip_log
         *
         * @param context
         * @return
         */
        public static String genDesZipFilePath(Context context) {
            StringBuffer sb = new StringBuffer();
            // 图片文件夹路径
            sb.append(SdCardUtil.getPrivateFilePath(context, XLogConfig.XLOG_ZIP_DIR));
            sb.append(File.separator);
            sb.append(XLogConfig.XLOG_FILE_NAME_PRE);
            // 以时间命令log文件
            sb.append(TimeUtil.getFormatCurrTime());
            // 扩展名
            sb.append(".zip");
            return sb.toString();
        }

    }


    /**
     * 时间工具类
     */
    public static class TimeUtil {

        /**
         * 格式化的 当前时间
         *
         * @return 字符串 yyyyMMdd_HHmmss
         */
        public static String getFormatCurrTime() {
            Date currentTime = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String dateString = formatter.format(currentTime);
            return dateString;
        }
    }

    /**
     * 内部存储
     * /data/data/包名/files
     * context.getFilesDir().getPath()
     * /data/data/包名/cache
     * context.getCacheDir().getPath()
     * <p>
     * 外部存储
     * /sdcard/Android/data/包名/cache/dir
     * context.getExternalFilesDir("dir").getPath()
     * /sdcard/Android/data/包名/cache
     * context.getExternalCacheDir().getPath()
     */
    public static class SdCardUtil {


        /**
         * 获取应用私有file目录
         * <p>
         * /sdcard/Android/data/包名/file/dir
         *
         * @param dir The type of files directory to return. May be {@code null}
         *            for the root of the files directory or one of the following
         *            constants for a subdirectory:
         *            {@link Environment#DIRECTORY_MUSIC},
         *            {@link Environment#DIRECTORY_PODCASTS},
         *            {@link Environment#DIRECTORY_RINGTONES},
         *            {@link Environment#DIRECTORY_ALARMS},
         *            {@link Environment#DIRECTORY_NOTIFICATIONS},
         *            {@link Environment#DIRECTORY_PICTURES}, or
         *            {@link Environment#DIRECTORY_MOVIES}.
         */
        public static String getPrivateFilePath(Context context, String dir) {
            File file = context.getExternalFilesDir(dir);
            //先判断外部存储是否可用
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && file != null) {
                return file.getAbsolutePath();
            } else {
                return context.getFilesDir() + File.separator + dir;
            }
        }
    }


    /**
     * 文件压缩工具类
     */
    public static class ZipFileUtil {
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
            List<File> exportFiles = new ArrayList<>();
            int size = filePaths.size();
            for (int i = 0; i < size; i++) {
                exportFiles(exportFiles, filePaths.get(i));
            }
            // 压缩文件
            return exportZipFromFiles(exportFiles, destZip);
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
    }
}

```


