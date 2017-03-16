package com.ytrain.wxns.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;

public class HandlerException implements UncaughtExceptionHandler {
	/**
	 * 发生其他异常时候的处理
	 */
    private Context context;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
 
    private static HandlerException mHandlerException = new HandlerException();
 
    private HandlerException() {
    }
 
    public static HandlerException getInstance() {
        return mHandlerException;
        
    }
 
    public void init(Context context) {
        this.context = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }
 
    @Override
    public void uncaughtException(Thread thread, Throwable e) {
        //如果没有处理异常，则设置默认的异常处理机制
        if (!handlerException(e) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, e);
        } else {
            // 如果自己处理了异常，退出app
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
 
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(10);
        }
 
    }
 
    public boolean handlerException(Throwable e) {
        if (e == null)//无需处理
            return false;
        final String message = e.getMessage();
        //将异常log保存在手机本地
        try {
            //没有测试成功
            FileOutputStream fos = context.openFileOutput("/Android/log.txt",
                    Context.MODE_PRIVATE);
            fos.write(message.getBytes());
            fos.flush();
            fos.close();     
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } 
        //异常处理过了，返回true
        //异常没有处理，返回false
        return false;
    }
 
}
