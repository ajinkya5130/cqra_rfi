package com.ob.rfi

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.ob.rfi.RfiApplication.rfiDB

class AppLifecycleObserver : DefaultLifecycleObserver {

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        Log.d("AppLifecycleObserver", "onStop: db closed")
        rfiDB.close();
    }
}