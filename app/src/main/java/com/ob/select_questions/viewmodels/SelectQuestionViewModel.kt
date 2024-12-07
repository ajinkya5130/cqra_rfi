package com.ob.select_questions.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ob.AppUtil.NO_DATA_AVAILABLE_VAL
import com.ob.database.RFIRoomDb
import com.ob.database.db_tables.BuildingAllocateTaskModel
import com.ob.database.db_tables.ClientAllocateTaskModel
import com.ob.database.db_tables.FloorAllocateTaskModel
import com.ob.database.db_tables.ProjectAllocateTaskModel
import com.ob.database.db_tables.WorkTypeAllocateTaskModel
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

    var lvWorkTypeAllocateData: LiveData<Int>? = null
    private val _lvWorkTypeAllocateData = MutableLiveData<Int>()

    var lvBuildingAllocateData: LiveData<Int>? = null
    private val _lvBuildingAllocateData = MutableLiveData<Int>()

    var lvFloorAllocateData: LiveData<Int>? = null
    private val _lvFloorAllocateData = MutableLiveData<Int>()

    private val _lvErrorData = MutableLiveData<APIErrorModel>()
    var lvErrorData: LiveData<APIErrorModel>

    var listOfClientAllocateTaskModel = arrayListOf<ClientAllocateTaskModel>()
    var listOfSchemaAllocateTaskModel = arrayListOf<ProjectAllocateTaskModel>()
    var listOfWorkTypeAllocateTaskModel = arrayListOf<WorkTypeAllocateTaskModel>()
    var listOfBuildingAllocateTaskModel = arrayListOf<BuildingAllocateTaskModel>()
    var listOfFloorAllocateTaskModel = arrayListOf<FloorAllocateTaskModel>()
    init {
        lvClientAllocateData = _lvClientAllocateData
        lvSchemaAllocateData = _lvSchemaAllocateData
        lvWorkTypeAllocateData = _lvWorkTypeAllocateData
        lvBuildingAllocateData = _lvBuildingAllocateData
        lvFloorAllocateData = _lvFloorAllocateData
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

    fun getWorkTypeAllocatedData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                listOfWorkTypeAllocateTaskModel =
                    CustomTitle.rfiDB.clientAllocateTaskDao().getWorkTypeAllocateData(
                        RfiDatabase.selectedSchemeId) as ArrayList<WorkTypeAllocateTaskModel>
                if (listOfWorkTypeAllocateTaskModel.size != 0) {
                    listOfWorkTypeAllocateTaskModel.add(
                        0,
                        WorkTypeAllocateTaskModel(workTypeName = "Select Data", workTypeId = NO_DATA_AVAILABLE_VAL.toInt())
                    )
                }
                val count = listOfWorkTypeAllocateTaskModel.size
                Log.d(TAG, "${RFIRoomDb.TAG} - getWorkTypeAllocatedData: count: $count ")
                _lvWorkTypeAllocateData.postValue(count)
            } catch (e: Exception) {
                _lvErrorData.postValue(
                    APIErrorModel(
                        message = "Something is wrong while fetching Data, Please try again!, ${e.message}",
                        spinnerType = SpinnerType.WORK_TYPE
                    )
                )
                Log.e(TAG, "${RFIRoomDb.TAG} - getWorkTypeAllocatedData: Exception: ", e)
            }
        }
    }

    fun getBuildingDataFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                listOfBuildingAllocateTaskModel =
                    CustomTitle.rfiDB.clientAllocateTaskDao().getBuildingAllocateData(
                        RfiDatabase.selectedWorkTypeId) as ArrayList<BuildingAllocateTaskModel>
                if (listOfBuildingAllocateTaskModel.size != 0) {
                    listOfBuildingAllocateTaskModel.add(
                        0,
                        BuildingAllocateTaskModel(buildingName = "Select Data", buildingId = NO_DATA_AVAILABLE_VAL.toInt())
                    )
                }
                val count = listOfBuildingAllocateTaskModel.size
                Log.d(TAG, "${RFIRoomDb.TAG} - getBuildingDataFromDB: count: $count ")
                _lvBuildingAllocateData.postValue(count)
            } catch (e: Exception) {
                _lvErrorData.postValue(
                    APIErrorModel(
                        message = "Something is wrong while fetching Data, Please try again!, ${e.message}",
                        spinnerType = SpinnerType.WORK_TYPE
                    )
                )
                Log.e(TAG, "${RFIRoomDb.TAG} - getBuildingDataFromDB: Exception: ", e)
            }
        }
    }

    fun getFloorDataFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                listOfFloorAllocateTaskModel =
                    CustomTitle.rfiDB.clientAllocateTaskDao().getFloorAllocateData(
                        RfiDatabase.selectedBuildingId) as ArrayList<FloorAllocateTaskModel>
                if (listOfFloorAllocateTaskModel.size != 0) {
                    listOfFloorAllocateTaskModel.add(
                        0,
                        FloorAllocateTaskModel(floorName = "Select Data", floorId = NO_DATA_AVAILABLE_VAL.toInt())
                    )
                }
                val count = listOfFloorAllocateTaskModel.size
                Log.d(TAG, "${RFIRoomDb.TAG} - getBuildingDataFromDB: count: $count ")
                _lvFloorAllocateData.postValue(count)
            } catch (e: Exception) {
                _lvErrorData.postValue(
                    APIErrorModel(
                        message = "Something is wrong while fetching Data, Please try again!, ${e.message}",
                        spinnerType = SpinnerType.WORK_TYPE
                    )
                )
                Log.e(TAG, "${RFIRoomDb.TAG} - getBuildingDataFromDB: Exception: ", e)
            }
        }
    }
}