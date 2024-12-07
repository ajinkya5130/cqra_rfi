package com.ob.select_questions.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ob.AppUtil.NO_DATA_AVAILABLE_VAL
import com.ob.database.RFIRoomDb
import com.ob.database.db_tables.ClientAllocateTaskModel
import com.ob.database.db_tables.ProjectAllocateTaskModel
import com.ob.rfi.CustomTitle
import com.ob.rfi.db.RfiDatabase
import com.ob.rfi.models.APIErrorModel
import com.ob.rfi.models.SpinnerType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SelectQuestionViewModel: ViewModel() {

    var lvClientAllocateData: LiveData<Int>? = null
    private val _lvClientAllocateData = MutableLiveData<Int>()

    var lvSchemaAllocateData: LiveData<Int>? = null
    private val _lvSchemaAllocateData = MutableLiveData<Int>()

    private val _lvErrorData = MutableLiveData<APIErrorModel>()
    var lvErrorData: LiveData<APIErrorModel>

    var listOfClientAllocateTaskModel = arrayListOf<ClientAllocateTaskModel>()
    var listOfSchemaAllocateTaskModel = arrayListOf<ProjectAllocateTaskModel>()
    init {
        lvClientAllocateData = _lvClientAllocateData
        lvSchemaAllocateData = _lvSchemaAllocateData
        lvErrorData = _lvErrorData
    }
    companion object{
        private const val TAG = "SelectQuestionViewModel"
    }

    fun getClientAllocateDataFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                listOfClientAllocateTaskModel =
                    CustomTitle.rfiDB.clientAllocateTaskDao().getClientAllocateData(
                    ) as ArrayList<ClientAllocateTaskModel>
                if (listOfClientAllocateTaskModel.size != 0) {
                    listOfClientAllocateTaskModel.add(
                        0,
                        ClientAllocateTaskModel(clientName = "Select Data", clientId = NO_DATA_AVAILABLE_VAL.toInt())
                    )
                }
                val count = listOfClientAllocateTaskModel.size
                Log.d(TAG, "${RFIRoomDb.TAG} - getClientAllocateDataFromDB: count: $count ")
                _lvClientAllocateData.postValue(count)
            } catch (e: Exception) {
                _lvErrorData.postValue(
                    APIErrorModel(
                        message = "Something is wrong while fetching Data, Please try again!, ${e.message}",
                        spinnerType = SpinnerType.CLIENT
                    )
                )
                Log.e(TAG, "${RFIRoomDb.TAG} - getClientAllocateDataFromDB: Exception: ", e)
            }
        }
    }

    fun getSchemaOrProjectData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                listOfSchemaAllocateTaskModel =
                    CustomTitle.rfiDB.clientAllocateTaskDao().getProjectAllocateData(
                        RfiDatabase.selectedClientId) as ArrayList<ProjectAllocateTaskModel>
                if (listOfSchemaAllocateTaskModel.size != 0) {
                    listOfSchemaAllocateTaskModel.add(
                        0,
                        ProjectAllocateTaskModel(schemaOrProjectName = "Select Data", schemaOrProjectId = NO_DATA_AVAILABLE_VAL.toInt())
                    )
                }
                val count = listOfSchemaAllocateTaskModel.size
                Log.d(TAG, "${RFIRoomDb.TAG} - getSchemaOrProjectData: count: $count ")
                _lvSchemaAllocateData.postValue(count)
            } catch (e: Exception) {
                _lvErrorData.postValue(
                    APIErrorModel(
                        message = "Something is wrong while fetching Data, Please try again!, ${e.message}",
                        spinnerType = SpinnerType.PROJECT
                    )
                )
                Log.e(TAG, "${RFIRoomDb.TAG} - getSchemaOrProjectData: Exception: ", e)
            }
        }
    }
}