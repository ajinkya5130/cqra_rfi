package com.ob.rfi.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.ob.AppUtil.FIREBASE_AUTH_TOKEN
import com.ob.rfi.api.APIClient
import com.ob.rfi.models.APIErrorModel
import com.ob.rfi.models.SignInResponseModel
import com.ob.rfi.models.SpinnerType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.net.HttpURLConnection

class LoginViewModel:ViewModel() {

    companion object{
        private const val TAG = "LoginViewModel"
    }

    private val _flowModel =
        MutableStateFlow<LoginSealedClass>(LoginSealedClass.EmptyState)
    val flowModel: LiveData<LoginSealedClass> = _flowModel.asLiveData()


    fun getSignInApi(){
        val jsonObject = JsonObject()
        jsonObject.addProperty("username","sandeep")
        jsonObject.addProperty("password","2sandeep13")
        _flowModel.value = LoginSealedClass.Loading(true,"Signing in...")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = APIClient.getAPIInterface().getSignInAPI(jsonObject)
                if (response.isSuccessful && response.code() == HttpURLConnection.HTTP_OK) {
                    val model = response.body()
                    model?.let {
                        val signInResponseModel =
                            Gson().fromJson(it.string(), SignInResponseModel::class.java)
                        Log.d(TAG, "getSignInApi: response: $it, signInResponseModel: $signInResponseModel")
                        FIREBASE_AUTH_TOKEN = signInResponseModel.accessToken
                        _flowModel.value = LoginSealedClass.Success(signInResponseModel)
                    }
                } else {
                    Log.e(TAG, "getClientProjectWorkType: response: ${response.errorBody()}")
                    response.errorBody()?.let {
                        val value = it.string()
                        Log.e(TAG, "getClientAPi: Error response: $value")
                        var model = APIErrorModel()
                        if (value.isNotEmpty()){
                            model = Gson().fromJson(value, APIErrorModel::class.java)
                        }
                        model.message =
                            "Something is wrong while fetching SignIn Data, Please try again!, errorCode: ${response.code()}"
                        model.spinnerType = SpinnerType.CLIENT
                        _flowModel.value = LoginSealedClass.Failure(model)
                        Log.e(TAG, "getClientAPi: Error model : $model")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "getSignInApi: Exception: ",e)
                _flowModel.value = LoginSealedClass.Message(
                    "Something is wrong while fetching SignIn Data, Please try again!",
                    e
                )
            }
        }
    }

    fun showProgress() {
        _flowModel.value = LoginSealedClass.Loading(true,"Signing in...")
    }

    fun hideProgress() {
        _flowModel.value = LoginSealedClass.Loading(false,)
    }
}