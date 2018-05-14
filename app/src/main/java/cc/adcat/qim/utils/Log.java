package cc.adcat.qim.utils;


import cc.adcat.qim.BuildConfig;

;

/**
 * Created by changzuzhe on 2018/3/7.
 */

public class Log {

    public static final int DEBUG = android.util.Log.DEBUG;
    private static final boolean VERBOSE_B = BuildConfig.LOG_VERBOSE;
    private static final boolean INFO_B = BuildConfig.LOG_INFO;
    private static final boolean DEBUG_B = BuildConfig.LOG_DEBUG;
    private static final boolean WARN_B= BuildConfig.LOG_WARN;
    private static final boolean ERROR_B = BuildConfig.LOG_ERROR;

    public static int v(String tag, String msg) {
        if (VERBOSE_B) {
            return android.util.Log.v(tag,msg);
        } else {
            return -1;
        }
    }

    public static int v(String tag, String msg, Throwable tr) {
        if (VERBOSE_B) {
            return android.util.Log.v(tag,msg,tr);
        } else {
            return -1;
        }
    }

    public static int d(String tag, String msg) {
        if (DEBUG_B) {
            return android.util.Log.d(tag,msg);
        } else {
            return -1;
        }
    }

    public static int d(String tag, String msg, Throwable tr) {
        if (DEBUG_B) {
            return android.util.Log.d(tag,msg,tr);
        } else {
            return -1;
        }
    }

    public static int i(String tag, String msg) {
        if (INFO_B) {
            return android.util.Log.i(tag,msg);
        } else {
            return -1;
        }
    }

    public static int i(String tag, String msg, Throwable tr) {
        if (INFO_B) {
            return android.util.Log.i(tag,msg,tr);
        } else {
            return -1;
        }
    }

    public static int w(String tag, String msg) {
        if (WARN_B) {
            return android.util.Log.w(tag,msg);
        } else {
            return -1;
        }
    }

    public static int w(String tag, String msg, Throwable tr) {
        if (WARN_B) {
            return android.util.Log.w(tag,msg,tr);
        } else {
            return -1;
        }
    }

    public static int w(String tag, Throwable tr) {
        if (WARN_B) {
            return android.util.Log.w(tag,tr);
        } else {
            return -1;
        }
    }

    public static int e(String tag, String msg) {
        if (ERROR_B) {
            return android.util.Log.e(tag,msg);
        } else {
            return -1;
        }
    }

    public static int e(String tag, String msg, Throwable tr) {
        if (ERROR_B) {
            return android.util.Log.e(tag,msg,tr);
        } else {
            return -1;
        }
    }

    public static int wtf(String tag, String msg) {
        if (ERROR_B){
            return android.util.Log.wtf(tag,msg);
        } else {
            return -1;
        }
    }

    public static int wtf(String tag, Throwable tr) {
        if (ERROR_B){
            return android.util.Log.wtf(tag,tr);
        } else {
            return -1;
        }
    }

    public static  boolean isLoggable(String tag, int level){
        return android.util.Log.isLoggable(tag,level);
    }

    public static void printStackTrace(Exception e){
        if (ERROR_B) {
            e.printStackTrace();
        }
    }
}
