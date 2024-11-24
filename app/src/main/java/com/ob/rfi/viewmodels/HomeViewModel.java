package com.ob.rfi.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {
    private static final String TAG = "HomeViewModel";
    public LiveData<String> _lvda;
    private MutableLiveData<String> lvzdata = new MutableLiveData<>();

    HomeViewModel(){
        _lvda = lvzdata;
    }
}
