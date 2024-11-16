package com.ob.rfi;

import android.app.Application;

public class RfiApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Set the custom exception handler
        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
    }
}
