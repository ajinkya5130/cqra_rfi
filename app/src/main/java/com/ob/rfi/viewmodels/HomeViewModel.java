package com.ob.rfi.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ob.rfi.api.APIClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {
    private static final String TAG = "HomeViewModel";
    public LiveData<String> _lvda;
    private MutableLiveData<String> lvzdata = new MutableLiveData<>();

    HomeViewModel(){
        _lvda = lvzdata;
    }
    public void getData(String rollName, String userId){

        APIClient.getAPIInterface().getClientProjectWorkType(userId,rollName).enqueue(
                new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String value = response.body();
                            Log.d(TAG, "onResponse: V: "+value);
                        } else {
                            Log.e(TAG, "Response not successful");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable throwable) {
                        Log.e(TAG, "API call failed: " + throwable.getMessage());
                    }
                }
        );

       lvzdata.setValue("jgjhgjhg");
    }
}
