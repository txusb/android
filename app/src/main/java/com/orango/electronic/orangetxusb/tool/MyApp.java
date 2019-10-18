package com.orango.electronic.orangetxusb.tool;

import android.app.Application;
import android.content.Context;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.orango.electronic.orangetxusb.BuildConfig;
import com.orango.electronic.orangetxusb.mainActivity.LogoActivity;

public class MyApp extends Application {
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        context=getApplicationContext();
//        Thread.setDefaultUncaughtExceptionHandler(new SimpleUncaughtExceptionHandler());
    }
}