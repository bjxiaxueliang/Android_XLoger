# Android Log 打印到控制台与文件

Android项目开发中，需要将Log同时打印到`控制台` 与 `文件`：

+ 打印到控制台：软件研发阶段，方便Debug调试；
+ 打印到文件：软件上线后，方便线上问题的跟踪分析；

本文介绍的一个开源日志工具为`XLog`，具备的功能如下：

+ 开发模式
debug 为 true 时，打印在控制台，同时打印到文件；
+ 发版模式
debug 为 fase 时，只打印到文件；
+ 方便日志上传
支持日志压缩上传

## 一、使用举例

+ 日志输出到控制台

![请添加图片描述](https://img-blog.csdnimg.cn/2fad447cc47d4aa9a59d12460218486f.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBAYmp4aWF4dWVsaWFuZw==,size_20,color_FFFFFF,t_70,g_se,x_16)

+ 对应文件中的日志

![请添加图片描述](https://img-blog.csdnimg.cn/2027c404f2bc4ef08e97db1abad1b674.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBAYmp4aWF4dWVsaWFuZw==,size_20,color_FFFFFF,t_70,g_se,x_16)

+ 压缩后的日志文件路径

![请添加图片描述](https://img-blog.csdnimg.cn/91a93e38d508461390b53867ba07986d.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBAYmp4aWF4dWVsaWFuZw==,size_14,color_FFFFFF,t_70,g_se,x_16)

## 二、使用方式


+ 初始化
+ 打日志
+ 文件压缩上传


### 2.1、初始化

初始化建议放到Application中

```java
public class MainApplication extends Application {
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
```

### 2.2、打日志


```java
// 打印到控制台；同时打印到文件；
XLog.d(TAG, "---onCreate---");
```

### 2.3、文件压缩上传


```java
// 文件压缩，并返回压缩后的文件路径
String filePath = XLog.zipLogFiles();
// toast 文件路径
if (!TextUtils.isEmpty(filePath)) {
    Toast.makeText(MainActivity.this, "日志文件生成成功：" + filePath,
            Toast.LENGTH_LONG).show();
}
```

## 三、源码地址

Github源码地址：
[https://github.com/xiaxveliang/Android_XLoger](https://github.com/xiaxveliang/Android_XLoger)



