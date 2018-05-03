package org.chengpx.test180501;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

/**
 * create at 2018/5/3 16:57 by chengpx
 */
public class MIApp extends Application implements Thread.UncaughtExceptionHandler {

    private String mTag = "org.chengpx.test180501.MIApp";

    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        t.getName();
        Log.d(mTag, "t.getName: " + t.getName());
        e.printStackTrace();
        Toast.makeText(this, "服务器已崩溃请立即重启", Toast.LENGTH_SHORT).show();
        System.exit(0);
    }

}
