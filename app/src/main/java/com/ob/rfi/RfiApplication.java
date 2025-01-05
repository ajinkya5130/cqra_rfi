package com.ob.rfi;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.ProcessLifecycleOwner;

import com.ob.database.RFIRoomDb;

public class RfiApplication extends Application {
    public static RFIRoomDb rfiDB = null;
    @Override
    public void onCreate() {
        super.onCreate();
        // Set the custom exception handler
        rfiDB = RFIRoomDb.Companion.getDatabase(this);
        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new AppLifecycleObserver());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        try {
            //rfiDB.close();
        } catch (Exception e) {
            Log.e("RfiApplication", "onTerminate:Exception: ",e );
        }
    }
}
