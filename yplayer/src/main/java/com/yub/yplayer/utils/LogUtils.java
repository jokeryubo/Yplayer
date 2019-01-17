package com.yub.yplayer.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xuqiqiang on 2017/4/28.
 */
public class LogUtils {
    public static int level = Log.VERBOSE;
    private static boolean DEBUG = true;
    private static File mDir;
    private static final long MAX_FILE_SIZE = 1024 * 1024;
    private static final ExecutorService mExecutorService = Executors.newSingleThreadExecutor();
    private static String mDefaultDate;
    private static final int MAX_FILE_SUM = 50;
    private static final int MAX_FILE_SUM_CACHE = 30;


    public static void register(Context context, String dirPath, boolean debug) {
        if (TextUtils.isEmpty(dirPath)) {
            dirPath = getDefaultPath(context);
        }
        LogUtils.DEBUG = debug;
        mDir = new File(dirPath);
        if (!mDir.exists()) {
            if (!mDir.mkdirs())
                Log.e("LibLog", "Error occurred during creating mDir");
        }
    }

    public static void initDefaultDate() {
        mDefaultDate = FormatDate.getFormatDate();
    }

    public static void unregister() {
        mDir = null;
    }

    private static String getDefaultPath(Context context) {
        String path;
        if (context != null) {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                path = Environment
                        .getExternalStorageDirectory().getAbsolutePath()
                        + File.separator + context.getPackageName();
            } else {
                path = context.getCacheDir().getAbsolutePath()
                        + File.separator + context.getPackageName();
            }
        } else {
            path = Environment
                    .getExternalStorageDirectory().getAbsolutePath();
        }
        return path;
    }

    public static void vLog(Object obj, String msg) {
        if (level <= Log.VERBOSE) {
            String tag = getClassInfoByObject(obj);
            StackTraceElement ste = new Throwable().getStackTrace()[1];
            String codeInfo = "(" + ste.getFileName() + ":"
                    + ste.getLineNumber() + ")";
            if (DEBUG)
                Log.v(tag, msg + " " + codeInfo);
            String systemOut = FormatDate.getFormatTime() + " V/" + tag + codeInfo + " : " + msg + "\r\n";
            startWriteThread(systemOut);
        }
    }

    public static void dLog(Object obj, String msg) {
        if (level <= Log.DEBUG) {
            String tag = getClassInfoByObject(obj);
            StackTraceElement ste = new Throwable().getStackTrace()[1];
            String codeInfo = "(" + ste.getFileName() + ":"
                    + ste.getLineNumber() + ")";
            if (DEBUG)
                Log.d(tag, msg + " " + codeInfo);
            String systemOut = FormatDate.getFormatTime() + " D/" + tag + codeInfo + " : " + msg + "\r\n";
            startWriteThread(systemOut);
        }
    }


    public static void iLog(Object obj, String msg) {
        if (level <= Log.INFO) {
            String tag = getClassInfoByObject(obj);
            StackTraceElement ste = new Throwable().getStackTrace()[1];
            String codeInfo = "(" + ste.getFileName() + ":"
                    + ste.getLineNumber() + ")";
            if (DEBUG)
                Log.i(tag, msg + " " + codeInfo);
            String systemOut = FormatDate.getFormatTime() + " I/" + tag + codeInfo + " : " + msg + "\r\n";
            startWriteThread(systemOut);
        }
    }

    public static void wLog(Object obj, String msg) {
        if (level <= Log.WARN) {
            String tag = getClassInfoByObject(obj);
            StackTraceElement ste = new Throwable().getStackTrace()[1];
            String codeInfo = "(" + ste.getFileName() + ":"
                    + ste.getLineNumber() + ")";
            if (DEBUG)
                Log.w(tag, msg + " " + codeInfo);
            String systemOut = FormatDate.getFormatTime() + " W/" + tag + codeInfo + " : " + msg + "\r\n";
            startWriteThread(systemOut);
        }
    }

    public static void eLog(Object obj, String msg) {
        if (level <= Log.ERROR) {
            String tag = getClassInfoByObject(obj);
            StackTraceElement ste = new Throwable().getStackTrace()[1];
            String codeInfo = "(" + ste.getFileName() + ":"
                    + ste.getLineNumber() + ")";
            if (DEBUG)
                Log.e(tag, msg + " " + codeInfo);
            String systemOut = FormatDate.getFormatTime() + " E/" + tag + codeInfo + " : " + msg + "\r\n";
            startWriteThread(systemOut);
        }
    }

    private static String getClassInfoByObject(Object obj) {
        if (obj == null) {
            return "null";
        }
        String simpleName = obj.getClass().getSimpleName();
        if ("String".equals(simpleName)) {
            return obj.toString();
        }
        if (TextUtils.isEmpty(simpleName)) {
            return "TAG";
        }
        return simpleName;
    }

    @SuppressLint("SimpleDateFormat")
    private static class FormatDate {

        static String getFormatDate() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            return sdf.format(System.currentTimeMillis());
        }

        static String getYesterdayFormatDate() {
            Calendar ca = Calendar.getInstance();
            ca.setTime(new Date());
            ca.add(Calendar.DATE, -1);
            Date lastDay = ca.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            return sdf.format(lastDay);
        }

        static String getBeforeYesterdayFormatDate() {
            Calendar ca = Calendar.getInstance();
            ca.setTime(new Date());
            ca.add(Calendar.DATE, -2);
            Date lastDay = ca.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            return sdf.format(lastDay);
        }

        static String getFormatTime() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            return sdf.format(System.currentTimeMillis());
        }
    }

    private static synchronized void startWriteThread(final String systemOut) {
        if (mDir == null)
            return;
        mExecutorService.execute(new Thread() {
            @Override
            public void run() {
                if (!write(systemOut))
                    Log.e("LibLog", "Error occurred during writing log : " + systemOut);
            }
        });
    }

    private static File getWriteFile() {
        String date = FormatDate.getFormatDate();
        int fileIndex = 1;
        File file;
        do {
            file = new File(mDir,
                    date + "-" + (fileIndex++) + ".log");
        } while (file.exists() && file.length() > MAX_FILE_SIZE);

        if (!file.exists()) {

            File f = handleOutdatedFiles();

            if (f != null)
                file = f;
            try {
                if (!file.createNewFile())
                    return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return file;
    }

    private static boolean write(String info) {
        if (mDir == null)
            return false;
        File file = getWriteFile();
        if (file == null)
            return false;
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        byte[] b = info.getBytes();
        try {
            outputStream.write(b, 0, b.length);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private static List<File> getFileList(String date) {
        List<File> list = new ArrayList<>();
        int fileIndex = 1;
        File file;
        do {
            file = new File(mDir,
                    date + "-" + (fileIndex++) + ".log");
        } while (file.exists() && list.add(file));
        return list;
    }

    public static List<File> getYesterdayFileList() {
        return getFileList(FormatDate.getYesterdayFormatDate());
    }

    public static List<File> getYesterdayAndDefaultFileList() {
        List<File> list = getYesterdayFileList();
        list.addAll(getFileList(mDefaultDate));
        return list;
    }

    /**
     * 从指定日期的日志中删除sum个文件，并重置编号.
     *
     * @param date 指定日期.
     * @param sum  需要删除的文件个数，0表示删除所有文件.
     * @return {@link int} 删除的文件个数.
     */
    private static int deleteFileList(String date, int sum) {
        if (TextUtils.isEmpty(date))
            return 0;
        int fileIndex = 1;
        File file = null;
        do {
            if (file != null) {
                if (sum > 0) {
                    if (fileIndex - 1 <= sum)
                        file.delete();
                    else {
                        file.renameTo(new File(mDir,
                                date + "-" + (fileIndex - 1 - sum) + ".log"));
                    }
                } else {
                    file.delete();
                }
            }
            file = new File(mDir,
                    date + "-" + (fileIndex++) + ".log");
        } while (file.exists());
        int fileSum = fileIndex - 2;
        return sum <= 0 ? fileSum : fileSum < sum ? fileSum : sum;
    }


    private static int fixFileList(String date, int deleteIndex) {
        if (TextUtils.isEmpty(date) || deleteIndex <= 0)
            return 0;
        int fileIndex = deleteIndex + 1;
        File file = null;
        do {
            if (file != null) {
                file.renameTo(new File(mDir,
                        date + "-" + (fileIndex - 1 - deleteIndex) + ".log"));
            }
            file = new File(mDir,
                    date + "-" + (fileIndex++) + ".log");
        } while (file.exists());
        return fileIndex - 1 - deleteIndex;
    }

    /**
     * 清理过时的日志，如果文件数还是大于80，把日志删除到只剩50个，如果删除了今天的日志，会调整
     * 今天的日志的编号，并返回一个正确编号的文件.
     *
     * @return {@link File} 返回一个正确编号的文件.
     */
    private static File handleOutdatedFiles() {
        File[] files = mDir.listFiles();
        if (files != null && files.length > MAX_FILE_SUM + MAX_FILE_SUM_CACHE) {
            List<File> fileList = Arrays.asList(files);
            Collections.sort(fileList, new Comparator<File>() {

                @Override
                public int compare(File o1, File o2) {
                    long t1 = o1.lastModified();
                    long t2 = o2.lastModified();
                    if (t1 < t2)
                        return -1;
                    else if (t1 == t2)
                        return 0;
                    else
                        return 1;
                }
            });
            int deleteSum = files.length - MAX_FILE_SUM;
            String today = FormatDate.getFormatDate();
            int todayIndex = 0;
            int i = 0;
            do {
                File file = fileList.get(i);
                if (file.getName().startsWith(today)) {
                    todayIndex++;
                }
                file.delete();
            } while (++i < deleteSum);
            if (todayIndex > 0)
                return new File(mDir,
                        today + "-" + fixFileList(today, todayIndex) + ".log");

        }
        return null;
    }
}
