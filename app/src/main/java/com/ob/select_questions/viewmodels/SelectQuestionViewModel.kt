package com.ob.select_questions.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ob.AppUtil.NO_DATA_AVAILABLE_VAL
import com.ob.database.RFIRoomDb
import com.ob.database.db_tables.BuildingAllocateTaskModel
import com.ob.database.db_tables.CheckListAllocateTaskModel
import com.ob.database.db_tables.ClientAllocateTaskModel
import com.ob.database.db_tables.CreateRFITableModel
import com.ob.database.db_tables.FloorAllocateTaskModel
import com.ob.database.db_tables.GroupListAllocateTaskModel
import com.ob.database.db_tables.ProjectAllocateTaskModel
import com.ob.database.db_tables.QuestionsTableModel
import com.ob.database.db_tables.SubUnitAllocateTaskModel
import com.ob.database.db_tables.UnitAllocateTaskModel
import com.ob.database.db_tables.WorkTypeAllocateTaskModel
import com.ob.rfi.CustomTitle
import com.ob.rfi.db.RfiDatabase
import com.ob.rfi.db.RfiDatabase.selectedSchemeId
import com.ob.rfi.db.RfiDatabase.selectedUnitId
import com.ob.rfi.db.RfiDatabase.selectedWorkTypeId
import com.ob.rfi.db.RfiDatabase.userId
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

    var lvUnitAllocateData: LiveData<Int>? = null
    private val _lvUnitAllocateData = MutableLiveData<Int>()

    var lvSubUnitAllocateData: LiveData<Int>? = null
    private val _lvSubUnitAllocateData = MutableLiveData<Int>()

    var lvCheckListAllocateData: LiveData<Int>? = null
    private val _lvCheckListAllocateData = MutableLiveData<Int>()

    var lvGroupListAllocateData: LiveData<Int>? = null
    private val _lvGroupListAllocateData = MutableLiveData<Int>()

    var lvQuestionsData: LiveData<Int>? = null
    private val _lvQuestionsData = MutableLiveData<Int>()

    private val _lvErrorData = MutableLiveData<APIErrorModel>()
    var lvErrorData: LiveData<APIErrorModel>

    var listOfClientAllocateTaskModel = arrayListOf<ClientAllocateTaskModel>()
    var listOfCreateRFITableModel = arrayListOf<CreateRFITableModel>()
    var listOfQuestions = arrayListOf<QuestionsTableModel>()
    var listOfSchemaAllocateTaskModel = arrayListOf<ProjectAllocateTaskModel>()
    var listOfWorkTypeAllocateTaskModel = arrayListOf<WorkTypeAllocateTaskModel>()
    var listOfBuildingAllocateTaskModel = arrayListOf<BuildingAllocateTaskModel>()
    var listOfFloorAllocateTaskModel = arrayListOf<FloorAllocateTaskModel>()
    var listOfUnitAllocateTaskModel = arrayListOf<UnitAllocateTaskModel>()
    var listOfSubUnitAllocateTaskModel = arrayListOf<SubUnitAllocateTaskModel>()
    var listOfCheckListAllocateTaskModel = arrayListOf<CheckListAllocateTaskModel>()
    var listOfGroupListAllocateTaskModel = arrayListOf<GroupListAllocateTaskModel>()
    init {
        lvClientAllocateData = _lvClientAllocateData
        lvSchemaAllocateData = _lvSchemaAllocateData
        lvWorkTypeAllocateData = _lvWorkTypeAllocateData
        lvBuildingAllocateData = _lvBuildingAllocateData
        lvFloorAllocateData = _lvFloorAllocateData
        lvUnitAllocateData = _lvUnitAllocateData
        lvSubUnitAllocateData = _lvSubUnitAllocateData
        lvCheckListAllocateData = _lvCheckListAllocateData
        lvGroupListAllocateData = _lvGroupListAllocateData
        lvQuestionsData = _lvQuestionsData
        lvErrorData = _lvErrorData
    }
    companion object{
        private const val TAG = "SelectQuestionViewModel"
    }

    fun getCreatedRFI(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                listOfCreateRFITableModel =
                    CustomTitle.rfiDB.createRFITableDao().getAllCreatedRFI() as ArrayList<CreateRFITableModel>
                val count = listOfCreateRFITableModel.size
                Log.d(TAG, "${RFIRoomDb.TAG} - listOfCreateRFITableModel: count: $count ")
                _lvClientAllocateData.postValue(count)
            } catch (e: Exception) {
                listOfCreateRFITableModel = arrayListOf()
                Log.e(TAG, "${RFIRoomDb.TAG} - getClientAllocateDataFromDB: Exception: ", e)
            }
        }
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
                        selectedSchemeId
                    ) as ArrayList<WorkTypeAllocateTaskModel>
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
                        spinnerType = SpinnerType.STAGE
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
                        spinnerType = SpinnerType.STRUCTURE
                    )
                )
                Log.e(TAG, "${RFIRoomDb.TAG} - getBuildingDataFromDB: Exception: ", e)
            }
        }
    }

    fun getUnitDataFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                listOfUnitAllocateTaskModel =
                    CustomTitle.rfiDB.clientAllocateTaskDao().getUnitAllocateData(
                        RfiDatabase.selectedFloorId,selectedSchemeId) as ArrayList<UnitAllocateTaskModel>
                if (listOfUnitAllocateTaskModel.size != 0) {
                    listOfUnitAllocateTaskModel.add(
                        0,
                        UnitAllocateTaskModel(unitName = "Select Data", unitId = NO_DATA_AVAILABLE_VAL.toInt())
                    )
                }
                val count = listOfUnitAllocateTaskModel.size
                Log.d(TAG, "${RFIRoomDb.TAG} - getBuildingDataFromDB: count: $count ")
                _lvUnitAllocateData.postValue(count)
            } catch (e: Exception) {
                _lvErrorData.postValue(
                    APIErrorModel(
                        message = "Something is wrong while fetching Data, Please try again!, ${e.message}",
                        spinnerType = SpinnerType.UNIT_LIST
                    )
                )
                Log.e(TAG, "${RFIRoomDb.TAG} - getBuildingDataFromDB: Exception: ", e)
            }
        }
    }

    fun getSubUnitAllocateDataFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                listOfSubUnitAllocateTaskModel =
                    CustomTitle.rfiDB.clientAllocateTaskDao().getSubUnitAllocateData(
                        selectedSchemeId,selectedUnitId) as ArrayList<SubUnitAllocateTaskModel>
                if (listOfSubUnitAllocateTaskModel.size != 0) {
                    listOfSubUnitAllocateTaskModel.add(
                        0,
                        SubUnitAllocateTaskModel(subUnitName = "Select Data", subUnitId = NO_DATA_AVAILABLE_VAL.toInt())
                    )
                }
                val count = listOfSubUnitAllocateTaskModel.size
                Log.d(TAG, "${RFIRoomDb.TAG} - getSubUnitAllocateDataFromDB: count: $count ")
                _lvSubUnitAllocateData.postValue(count)
            } catch (e: Exception) {
                _lvErrorData.postValue(
                    APIErrorModel(
                        message = "Something is wrong while fetching Data, Please try again!, ${e.message}",
                        spinnerType = SpinnerType.SUB_UNIT_LIST
                    )
                )
                Log.e(TAG, "${RFIRoomDb.TAG} - getSubUnitAllocateDataFromDB: Exception: ", e)
            }
        }
    }

    fun getCheckListAllocateDataFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                listOfCheckListAllocateTaskModel =
                    CustomTitle.rfiDB.clientAllocateTaskDao().getCheckListAllocateData(
                        selectedWorkTypeId) as ArrayList<CheckListAllocateTaskModel>
                if (listOfCheckListAllocateTaskModel.size != 0) {
                    listOfCheckListAllocateTaskModel.add(
                        0,
                        CheckListAllocateTaskModel(checkListName = "Select Data", checkListId = NO_DATA_AVAILABLE_VAL.toInt())
                    )
                }
                val count = listOfCheckListAllocateTaskModel.size
                Log.d(TAG, "${RFIRoomDb.TAG} - getBuildingDataFromDB: count: $count ")
                _lvCheckListAllocateData.postValue(count)
            } catch (e: Exception) {
                _lvErrorData.postValue(
                    APIErrorModel(
                        message = "Something is wrong while fetching Data, Please try again!, ${e.message}",
                        spinnerType = SpinnerType.CHECK_LIST
                    )
                )
                Log.e(TAG, "${RFIRoomDb.TAG} - getBuildingDataFromDB: Exception: ", e)
            }
        }
    }

    fun getGroupAllocateDataFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                listOfGroupListAllocateTaskModel =
                    CustomTitle.rfiDB.clientAllocateTaskDao().getGroupListAllocateData(
                        RfiDatabase.selectedChecklistId) as ArrayList<GroupListAllocateTaskModel>
                if (listOfGroupListAllocateTaskModel.size != 0) {
                    listOfGroupListAllocateTaskModel.add(
                        0,
                        GroupListAllocateTaskModel(groupName = "Select Data", groupId = NO_DATA_AVAILABLE_VAL.toInt())
                    )
                }
                val count = listOfGroupListAllocateTaskModel.size
                Log.d(TAG, "${RFIRoomDb.TAG} - getBuildingDataFromDB: count: $count ")
                _lvGroupListAllocateData.postValue(count)
            } catch (e: Exception) {
                _lvErrorData.postValue(
                    APIErrorModel(
                        message = "Something is wrong while fetching Data, Please try again!, ${e.message}",
                        spinnerType = SpinnerType.GROUP_LIST
                    )
                )
                Log.e(TAG, "${RFIRoomDb.TAG} - getBuildingDataFromDB: Exception: ", e)
            }
        }
    }

    fun insertCreatedRFI(cov: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val model = CreateRFITableModel()
            model.user_id = userId
            model.coverageText = cov

            val id = CustomTitle.rfiDB.createRFITableDao().insert(model)
            Log.d(TAG, "insertCreatedRFI: id: $id")
            RfiDatabase.selectedrfiId = id.toString()
            model._id = id.toInt()
            model.FK_rfi_Id = id.toString()
            val updatedId = CustomTitle.rfiDB.createRFITableDao().update(model)
            Log.d(TAG, "insertCreatedRFI: updatedId: $updatedId")

        }
    }

    fun isCoveragePresent(cov: String): Boolean {
        return listOfCreateRFITableModel.any { it.coverageText == cov }
    }

    fun checkIsQuestionAvailable() {
        viewModelScope.launch(Dispatchers.IO) {
            listOfQuestions =
                CustomTitle.rfiDB.questionsDao().getQuestionOnChecklistId(
                    RfiDatabase.selectedChecklistId.toInt(),
                    RfiDatabase.selectedBuildingId.toInt(),
                    RfiDatabase.selectedGroupId.toInt()
                ) as ArrayList<QuestionsTableModel>

            val count = listOfQuestions.size
            Log.d(TAG, "${RFIRoomDb.TAG} - listOfQuestions: count: $count ")
            _lvQuestionsData.postValue(count)
        }

    }
}