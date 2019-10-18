package com.orango.electronic.orangetxusb.tool;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class SimpleUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
private static final String LOGTAG = "SimpleUncaughtExceptionHandler";

@Override
public void uncaughtException(Thread thread, Throwable ex) {
//读取stacktrace信息
final Writer result = new StringWriter();
final PrintWriter printWriter = new PrintWriter(result);
        ex.printStackTrace(printWriter);
        String errorReport = result.toString();
        Log.i("LOGTAG", "uncaughtException errorReport=" + errorReport);
    android.os.Process.killProcess(android.os.Process.myPid());
        }
        }