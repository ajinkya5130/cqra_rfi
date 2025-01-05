package com.ob.rfi.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ob.database.RFIRoomDb
import com.ob.database.db_tables.LoginUserTableModel
import com.ob.rfi.CustomTitle
import com.ob.rfi.db.RfiDatabase.userId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel:ViewModel() {

    companion object{
        private const val TAG = "LoginViewModel"
    }

    private val _flowModel =
        MutableStateFlow<LoginSealedClass>(LoginSealedClass.EmptyState)
    val flowModel: LiveData<LoginSealedClass> = _flowModel.asLiveData()


    fun showProgress() {
        _flowModel.value = LoginSealedClass.Loading(true,"Signing in...")
    }

    fun hideProgress() {
        _flowModel.value = LoginSealedClass.Loading(false,)
    }


    fun getLoginUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val modelIsPresent = CustomTitle.rfiDB.loginUserDao().getLoginUserDataUsingId(userId = userId.toInt())?:LoginUserTableModel()
                _flowModel.value = LoginSealedClass.Success(modelIsPresent)
            } catch (e: Exception) {
                Log.e(TAG, "${RFIRoomDb.TAG} - getLoginUserData: Exception: ", e)
            }
        }
    }
}