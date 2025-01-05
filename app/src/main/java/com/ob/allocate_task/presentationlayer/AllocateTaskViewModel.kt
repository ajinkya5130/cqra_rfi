package com.ob.allocate_task.presentationlayer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ob.AppUtil.Int_NO_DATA_AVAILABLE_VAL
import com.ob.AppUtil.NO_DATA_AVAILABLE
import com.ob.AppUtil.NO_DATA_AVAILABLE_VAL
import com.ob.database.RFIRoomDb
import com.ob.database.db_tables.AllocateTaskTableModel
import com.ob.database.db_tables.ChecklistTableModel
import com.ob.database.db_tables.ClientTableModel
import com.ob.database.db_tables.GroupListTableModel
import com.ob.database.db_tables.ProjectTableModel
import com.ob.database.db_tables.QuestionsTableModel
import com.ob.database.db_tables.StageTableModel
import com.ob.database.db_tables.StructureTableModel
import com.ob.database.db_tables.SubUnitTableModel
import com.ob.database.db_tables.UnitTableModel
import com.ob.database.db_tables.WorkTypeTableModel
import com.ob.rfi.RfiApplication.rfiDB
import com.ob.rfi.api.APIClient
import com.ob.rfi.api.ConverterModel
import com.ob.rfi.db.RfiDatabase
import com.ob.rfi.models.APIErrorModel
import com.ob.rfi.models.ChecklistApiResponseModel
import com.ob.rfi.models.GroupListApiResponseModel
import com.ob.rfi.models.QuestionsApiResponseModel
import com.ob.rfi.models.SpinnerType
import com.ob.rfi.models.StageApiResponseModel
import com.ob.rfi.models.SubUnitListApiResponseModel
import com.ob.rfi.models.UnitListApiResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.net.HttpURLConnection

class AllocateTaskViewModel : ViewModel() {
    @JvmField
    var isError: Boolean = false
    var clientId:Int = 0
    var lvClientData: LiveData<Int>? = null
    private val _lvClientData = MutableLiveData<Int>()

    var lvProjectData: LiveData<Int>? = null
    private val _lvProjectData = MutableLiveData<Int>()

    var lvWorkTypeData: LiveData<Int>
    private val _lvWorkTypeData = MutableLiveData<Int>()

    var lvStructureData: LiveData<Int>
    private val _lvStructureData = MutableLiveData<Int>()

    var lvCheckListData: LiveData<Int>
    private val _lvCheckListData = MutableLiveData<Int>()

    var lvGroupListData: LiveData<Int>
    private val _lvGroupListData = MutableLiveData<Int>()

    var lvStageData: LiveData<Int>
    private val _lvStageData = MutableLiveData<Int>()

    var lvUnitData: LiveData<Int>
    private val _lvUnitData = MutableLiveData<Int>()

    var lvSubUnitData: LiveData<Int>
    private val _lvSubUnitData = MutableLiveData<Int>()

    var lvQuestionsData: LiveData<Int>
    private val _lvQuestionsData = MutableLiveData<Int>()

    var lvErrorData: LiveData<APIErrorModel>
    private val _lvErrorData = MutableLiveData<APIErrorModel>()

    var list = ArrayList<ClientTableModel>()
    var listOfProject = ArrayList<ProjectTableModel>()
    var listOfWorkType = ArrayList<WorkTypeTableModel>()
    var listOfStructure = ArrayList<StructureTableModel>()
    var listOfStage = arrayListOf<StageTableModel>()
    var listOfCheckList = arrayListOf<ChecklistTableModel>()
    var listOfGroupList = arrayListOf<GroupListTableModel>()
    var listOfUnitList = arrayListOf<UnitTableModel>()
    var listOfSubUnitList = arrayListOf<SubUnitTableModel>()
    var listOfQuestions = arrayListOf<QuestionsTableModel>()

    init {
        lvClientData = _lvClientData
        lvProjectData = _lvProjectData
        lvWorkTypeData = _lvWorkTypeData
        lvStructureData = _lvStructureData
        lvCheckListData = _lvCheckListData
        lvGroupListData = _lvGroupListData
        lvStageData = _lvStageData
        lvUnitData = _lvUnitData
        lvSubUnitData = _lvSubUnitData
        lvQuestionsData = _lvQuestionsData
        lvErrorData = _lvErrorData
    }

    companion object {
        private const val TAG = "AllocateTaskViewModel"
    }

    fun getClientProjectWorkType(rollName: Int, userRole: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = APIClient.getAPIInterface().getClientsAPI(
                rollName,
                userRole
            )
            if (response.isSuccessful && response.code() == HttpURLConnection.HTTP_OK) {
                val model = response.body()
                model?.let {
                    Log.d(TAG, "getClientProjectWorkType: response: $it")
                    if (it.isEmpty()) {
                        list.clear()
                        list.add(
                            0,
                            ClientTableModel(
                                clientName = NO_DATA_AVAILABLE,
                                clientId = Int_NO_DATA_AVAILABLE_VAL
                            )
                        )
                        _lvClientData.postValue(1)
                    } else {
                        val dbModel = ConverterModel.convertClientData(it[0])
                        rfiDB.clientDao().insert(dbModel)
                        getClientData()
                    }
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
                        "Something is wrong while fetching Client Data, Please try again!, errorCode: ${response.code()}"
                    model.spinnerType = SpinnerType.CLIENT
                    list = arrayListOf()
                    _lvErrorData.postValue(model)
                    Log.e(TAG, "getClientAPi: Error model : $model")
                }
            }

        }
    }


    fun getProjectApi(rollName: Int, userRole: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = APIClient.getAPIInterface().getProjectApi(
                rollName,
                userRole
            )
            if (response.isSuccessful && response.code() == HttpURLConnection.HTTP_OK) {
                val model = response.body()
                model?.let {
                    Log.d(TAG, "getProjectApi: response: $it")
                    if (it.isEmpty()) {
                        listOfProject.clear()
                        listOfProject.add(
                            0,
                            ProjectTableModel(
                                Scheme_Name = NO_DATA_AVAILABLE,
                                PK_Scheme_ID = NO_DATA_AVAILABLE_VAL
                            )
                        )
                        _lvProjectData.postValue(1)
                    } else {
                        val dbModel = ConverterModel.convertProjectData(it[0].project)
                        val nodeDbModel = ConverterModel.convertNodeData(it[0].nodeUserDetail)
                        rfiDB.projectDao().insert(dbModel)
                        rfiDB.nodeUserDetailsDao().insert(nodeDbModel)
                        getProjectDataFromDB()
                    }
                }

            } else {
                Log.e(TAG, "getProjectApi: response error: ${response.errorBody()}")
                response.errorBody()?.let {
                    val value = it.string()
                    Log.e(TAG, "getProjectApi: Error response: $value")
                    var model = APIErrorModel()
                    if (value.isNotEmpty()){
                        model = Gson().fromJson(value, APIErrorModel::class.java)
                    }
                    model.message =
                        "Something is wrong while fetching Project Data, Please try again!, errorcode: ${response.code()}"
                    model.spinnerType = SpinnerType.PROJECT
                    listOfProject = arrayListOf()
                    _lvErrorData.postValue(model)
                    Log.e(TAG, "getProjectApi: Error model : $model")
                }
            }

        }
    }

    fun getWorkTypeSequenceApi(rollName: Int, userRole: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = APIClient.getAPIInterface().workTypeSequenceApi(
                rollName,
                userRole
            )
            if (response.isSuccessful && response.code() == HttpURLConnection.HTTP_OK) {
                val model = response.body()
                model?.let {
                    Log.d(TAG, "getWorkTypeSequenceApi: response: $it")
                    try {
                        if (it.isEmpty()) {
                            listOfWorkType.clear()
                            listOfWorkType.add(
                                0,
                                WorkTypeTableModel(
                                    activitySequenceName = NO_DATA_AVAILABLE,
                                    activitySequenceGroupId = NO_DATA_AVAILABLE_VAL.toInt()
                                )
                            )
                            _lvWorkTypeData.postValue(1)
                        } else {
                            val dbModel = ConverterModel.convertWorkTypeModel(it)
                            rfiDB.workTypeDao().insertAll(dbModel)
                            getWorkTypeFromDB()
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "getWorkTypeSequenceApi: Exception: ", e)
                    }
                }

            } else {
                val model = APIErrorModel()
                model.message =
                    "Something is wrong while fetching CheckList Data, Please try again!"
                model.spinnerType = SpinnerType.WORK_TYPE
                listOfWorkType = arrayListOf()
                _lvErrorData.postValue(model)
                Log.e(TAG, "getStructureApi: response:Error $model")
            }

        }
    }

    fun getStructureApi(rollName: Int, userRole: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = APIClient.getAPIInterface().getStructureApi(
                rollName,
                userRole,
                clientId.toString(),
                RfiDatabase.selectedSchemeId,
                RfiDatabase.selectedWorkTypeId,
                RfiDatabase.selectedChecklistId,
            )
            if (response.isSuccessful && response.code() == HttpURLConnection.HTTP_OK) {
                val model = response.body()
                model?.let {
                    Log.d(TAG, "getStructureApi: response: $it")
                    try {
                        if (it.isEmpty()) {
                            listOfStructure.clear()
                            listOfStructure.add(
                                0,
                                StructureTableModel(
                                    Bldg_Name = NO_DATA_AVAILABLE,
                                    Bldg_ID = NO_DATA_AVAILABLE_VAL
                                )
                            )
                            _lvStructureData.postValue(1)
                        } else {
                            val dbModel = ConverterModel.convertStructureModel(it)
                            rfiDB.structureDao().insertAll(dbModel)
                            getStructureDataFromDB()
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "getStructureApi: Exception: ", e)
                    }
                }

            } else {
                val model = APIErrorModel()
                model.message =
                    "Something is wrong while fetching CheckList Data, Please try again!"
                model.spinnerType = SpinnerType.STRUCTURE
                listOfStructure = arrayListOf()
                _lvErrorData.postValue(model)
                Log.e(TAG, "getStructureApi: response: error: $model")
            }

        }
    }

    fun getCheckListApi(rollName: Int, userRole: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = APIClient.getAPIInterface().getCheckListApi(
                rollName,
                userRole,
                clientId.toString(),
                RfiDatabase.selectedSchemeId,
                RfiDatabase.selectedWorkTypeId
            )
            if (response.isSuccessful && response.code() == HttpURLConnection.HTTP_OK) {
                val model = response.body()
                model?.let {
                    val checkListModel =
                        Gson().fromJson(it.string(), ChecklistApiResponseModel::class.java)
                    Log.d(TAG, "getStructureApi: response: $checkListModel")
                    try {
                        if (checkListModel.isEmpty()) {
                            listOfCheckList.clear()
                            listOfCheckList.add(
                                0,
                                ChecklistTableModel(
                                    Checklist_Name = NO_DATA_AVAILABLE,
                                    Checklist_ID = NO_DATA_AVAILABLE_VAL
                                )
                            )
                            _lvCheckListData.postValue(1)
                        } else {
                            val dbModel = ConverterModel.convertCheckListModel(
                                RfiDatabase.selectedNodeId,
                                checkListModel
                            )
                            rfiDB.checkListDao().insertAll(dbModel)
                            getCheckListDataFromDB()
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "getStructureApi: Exception: ", e)
                    }
                }

            } else {
                response.errorBody()?.let {
                    val value = it.string()
                    Log.e(TAG, "getStructureApi: Error response: $value")
                    var model = APIErrorModel()
                    if (value.isNotEmpty()){
                        model = Gson().fromJson(value, APIErrorModel::class.java)
                    }
                    model.message =
                        "Something is wrong while fetching CheckList Data, Please try again!"
                    model.spinnerType = SpinnerType.CHECK_LIST
                    listOfCheckList = arrayListOf()
                    _lvErrorData.postValue(model)
                    Log.e(TAG, "getStageApi: Error model : $model")
                }
            }

        }
    }

    fun getGroupListApi(rollName: Int, userRole: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = APIClient.getAPIInterface().getGroupListApi(
                rollName,
                userRole,
                clientId.toString(),
                RfiDatabase.selectedSchemeId,
                RfiDatabase.selectedChecklistId
            )
            if (response.isSuccessful && response.code() == HttpURLConnection.HTTP_OK) {
                val model = response.body()
                model?.let {
                    val groupListModel =
                        Gson().fromJson(it.string(), GroupListApiResponseModel::class.java)
                    Log.d(TAG, "getGroupListApi: response: $groupListModel")
                    try {
                        if (groupListModel.isEmpty()) {
                            listOfGroupList.clear()
                            listOfGroupList.add(
                                0,
                                GroupListTableModel(
                                    Grp_Name = NO_DATA_AVAILABLE,
                                    Grp_ID = NO_DATA_AVAILABLE_VAL
                                )
                            )
                            _lvGroupListData.postValue(1)
                        } else {
                            val dbModel = ConverterModel.convertGroupListModel(
                                RfiDatabase.selectedNodeId,
                                groupListModel
                            )
                            rfiDB.groupListDao().insertAll(dbModel)
                            getGroupListDataFromDB()
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "getGroupListApi: Exception: ", e)
                    }
                }

            } else {
                response.errorBody()?.let {
                    val value = it.string()
                    Log.e(TAG, "getGroupListApi: response: $value")
                    var model = APIErrorModel()
                    if (value.isNotEmpty()){
                        model = Gson().fromJson(value, APIErrorModel::class.java)
                    }
                    model.message =
                        "Something is wrong while fetching CheckList Data, Please try again!"
                    model.spinnerType = SpinnerType.GROUP_LIST
                    listOfGroupList = arrayListOf()
                    _lvErrorData.postValue(model)

                    Log.e(TAG, "getStageApi: Error model : $model")
                }
            }

        }
    }


    fun getStageApi(rollName: Int, userRole: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = APIClient.getAPIInterface().getStageApi(
                rollName,
                userRole,
                clientId.toString(),
                RfiDatabase.selectedSchemeId,
                RfiDatabase.selectedWorkTypeId,
                RfiDatabase.selectedChecklistId,
                RfiDatabase.selectedBuildingId
            )
            if (response.isSuccessful && response.code() == HttpURLConnection.HTTP_OK) {
                val responseBody: ResponseBody = response.body()!!
                Log.d(TAG, "getStageApi: response: $responseBody")
                val model =
                    Gson().fromJson(responseBody.string(), StageApiResponseModel::class.java)
                model?.let {
                    Log.d(TAG, "getStageApi: response: $it")
                    try {
                        if (it.isEmpty()) {
                            listOfStage.add(
                                0,
                                StageTableModel(
                                    floor_Id = NO_DATA_AVAILABLE_VAL,
                                    floor_Name = NO_DATA_AVAILABLE
                                )
                            )
                            _lvStageData.postValue(1)
                        } else {
                            val dbModel = ConverterModel.convertStageModel(it)
                            rfiDB.stageDao().insertAll(dbModel)
                            getStageDataFromDB()
                        }

                    } catch (e: Exception) {
                        Log.e(TAG, "getStageApi: Exception: ", e)
                    }
                }

            } else {
                response.errorBody()?.let {
                    val value = it.string()
                    Log.e(TAG, "getStageApi: response: $value")
                    var model = APIErrorModel()
                    if (value.isNotEmpty()){
                        model = Gson().fromJson(value, APIErrorModel::class.java)
                    }
                    model.message =
                        "Something is wrong while fetching Stage Data, Please try again!"
                    model.spinnerType = SpinnerType.STAGE
                    listOfStage = arrayListOf()
                    _lvErrorData.postValue(model)
                    Log.e(TAG, "getStageApi: Error model : $model")
                }

            }

        }
    }


    fun getUnitApi(rollName: Int, userRole: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = APIClient.getAPIInterface().getUnitsApi(
                rollName,
                userRole,
                clientId.toString(),
                RfiDatabase.selectedSchemeId,
                RfiDatabase.selectedWorkTypeId,
                RfiDatabase.selectedChecklistId,
                RfiDatabase.selectedFloorId
            )
            if (response.isSuccessful && response.code() == HttpURLConnection.HTTP_OK) {
                val responseBody: ResponseBody = response.body()!!
                Log.d(TAG, "getUnitApi: response: $responseBody")
                val model =
                    Gson().fromJson(responseBody.string(), UnitListApiResponseModel::class.java)
                model?.let {
                    Log.d(TAG, "getUnitApi: response: $it")

                    try {
                        if (it.isEmpty()) {
                            listOfUnitList.clear()
                            listOfUnitList.add(
                                0,
                                UnitTableModel(
                                    Unit_Name = NO_DATA_AVAILABLE,
                                    Unit_ID = NO_DATA_AVAILABLE_VAL
                                )
                            )
                            _lvUnitData.postValue(1)
                        } else {
                            val dbModel = ConverterModel.convertUnitModel(it)
                            rfiDB.unitDao().insertAll(dbModel)
                            getUnitListDataFromDB()
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "getUnitApi: Exception: ", e)
                    }
                }

            } else {
                Log.e(TAG, "getUnitApi: response: ${response.errorBody()}")
                //response.errorBody()?.let {
                    val model = APIErrorModel()
                    model.message =
                        "Something is wrong while fetching Stage Data, Please try again!"
                    model.spinnerType = SpinnerType.UNIT_LIST
                    listOfStage = arrayListOf()
                    _lvErrorData.postValue(model)
                //}

            }

        }
    }

    fun getSubUnitApi(rollName: Int, userRole: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = APIClient.getAPIInterface().getSubUnitsApi(
                rollName,
                userRole,
                clientId.toString(),
                RfiDatabase.selectedSchemeId,
                RfiDatabase.selectedWorkTypeId,
                RfiDatabase.selectedChecklistId,
                RfiDatabase.selectedUnitId
            )
            if (response.isSuccessful && response.code() == HttpURLConnection.HTTP_OK) {
                val responseBody: ResponseBody = response.body()!!
                Log.d(TAG, "getSubUnitApi: response: $responseBody")
                val model =
                    Gson().fromJson(responseBody.string(), SubUnitListApiResponseModel::class.java)
                model?.let {
                    try {
                        Log.d(TAG, "getSubUnitApi: response: $it")
                        if (it.isEmpty()) {
                            listOfSubUnitList.clear()
                            listOfSubUnitList.add(
                                SubUnitTableModel(
                                    subUnitName = NO_DATA_AVAILABLE,
                                    subUnitId = NO_DATA_AVAILABLE_VAL
                                )
                            )
                            _lvSubUnitData.postValue(1)
                        } else {
                            val dbModel = ConverterModel.convertSubUnitModel(it)
                            rfiDB.subUnitDao().insertAll(dbModel)
                            getSubUnitListDataFromDB()
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "getSubUnitApi: Exception: ", e)
                    }
                }

            } else {
                //Log.e(TAG, "getSubUnitApi: response: ${response.errorBody()}")
                //response.errorBody()?.let {
                    val model = APIErrorModel()
                    model.message =
                        "Something is wrong while fetching Sub Unit Data, Please try again!"
                    model.spinnerType = SpinnerType.SUB_UNIT_LIST
                    listOfSubUnitList = arrayListOf()
                    _lvErrorData.postValue(model)
                    Log.e(TAG, "getSubUnitApi: Error model : $model")
                //}

            }

        }
    }

    fun getQuestionsApi(rollName: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = APIClient.getAPIInterface().getQuestionsApi(
                rollName,
                clientId.toString(),
                RfiDatabase.selectedSchemeId,
                RfiDatabase.selectedGroupId,
            )
            if (response.isSuccessful && response.code() == HttpURLConnection.HTTP_OK) {
                val responseBody: ResponseBody = response.body()!!
                responseBody.let {
                    try {
                        val model =
                            Gson().fromJson(it.string(), QuestionsApiResponseModel::class.java)
                        Log.d(TAG, "getQuestionsApi: response: $model")
                        if (model.isEmpty()) {
                            listOfQuestions.clear()
                            listOfQuestions.add(
                                0,
                                QuestionsTableModel(
                                    question = NO_DATA_AVAILABLE,
                                    questionId = NO_DATA_AVAILABLE_VAL.toInt()
                                )
                            )
                            _lvQuestionsData.postValue(1)
                        } else {
                            val dbModel = ConverterModel.convertQuestionsModel(model,
                                RfiDatabase.selectedBuildingId)
                            rfiDB.questionsDao().insertAll(dbModel)
                            getQuestionsDataFromDB()
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "getQuestionsApi: Exception: ", e)
                    }
                }

            } else {
                //Log.e(TAG, "getSubUnitApi: response: ${response.errorBody()}")
                //response.errorBody()?.let {
                    val model = APIErrorModel()
                    model.message =
                        "Something is wrong while fetching Questions Data, Please try again!"
                    model.spinnerType = SpinnerType.QUESTIONS_LIST
                    listOfQuestions = arrayListOf()
                    _lvErrorData.postValue(model)
                    Log.e(TAG, "getSubUnitApi: Error model : $model")
                //}

            }

        }
    }

    fun getGroupListDataFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                listOfGroupList =
                    rfiDB.groupListDao().getAllGroupList(
                        RfiDatabase.selectedChecklistId,
                        RfiDatabase.selectedNodeId
                    ) as ArrayList<GroupListTableModel>
                if (listOfGroupList.size != 0) {
                    listOfGroupList.add(
                        0,
                        GroupListTableModel(Grp_Name = "Select Group", Grp_ID = NO_DATA_AVAILABLE_VAL)
                    )
                }
                val count = listOfGroupList.size
                Log.d(TAG, "${RFIRoomDb.TAG} - getGroupListDataFromDB: count: $count ")
                _lvGroupListData.postValue(count)
            } catch (e: Exception) {
                _lvErrorData.postValue(
                    APIErrorModel(
                        message = "Something is wrong while fetching Group List Data, Please try again!, ${e.message}",
                        spinnerType = SpinnerType.GROUP_LIST
                    )
                )
                Log.e(TAG, "${RFIRoomDb.TAG} - getGroupListDataFromDB: Exception: ", e)
            }
        }
    }


    fun getUnitListDataFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                listOfUnitList =
                    rfiDB.unitDao()
                        .getAllUnitList(RfiDatabase.selectedFloorId) as ArrayList<UnitTableModel>
                if (listOfUnitList.size != 0) {
                    listOfUnitList.add(0, UnitTableModel(Unit_Name = "Select Unit", Unit_ID = NO_DATA_AVAILABLE_VAL))
                }
                val count = listOfUnitList.size
                Log.d(TAG, "${RFIRoomDb.TAG} - getUnitListDataFromDB: count: $count ")
                _lvUnitData.postValue(count)
            } catch (e: Exception) {
                _lvErrorData.postValue(
                    APIErrorModel(
                        message = "Something is wrong while fetching Unit List Data, Please try again!, ${e.message}",
                        spinnerType = SpinnerType.UNIT_LIST
                    )
                )
                Log.e(TAG, "${RFIRoomDb.TAG} - getUnitListDataFromDB: Exception: ", e)
            }
        }
    }

    fun getSubUnitListDataFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                listOfSubUnitList =
                    rfiDB.subUnitDao().getAllSubUnitList(
                        RfiDatabase.selectedUnitId,
                        RfiDatabase.selectedSchemeId,
                        RfiDatabase.selectedWorkTypeId
                    ) as ArrayList<SubUnitTableModel>
               /* if (listOfSubUnitList.size != 0) {
                    listOfSubUnitList.add(
                        0,
                        SubUnitTableModel(subUnitName = "Select Sub Unit", subUnitId = NO_DATA_AVAILABLE_VAL)
                    )
                }*/
                val count = listOfSubUnitList.size
                Log.d(TAG, "${RFIRoomDb.TAG} - getSubUnitListDataFromDB: count: $count ")
                _lvSubUnitData.postValue(count)
            } catch (e: Exception) {
                _lvErrorData.postValue(
                    APIErrorModel(
                        message = "Something is wrong while fetching SubUnit List Data, Please try again!, ${e.message}",
                        spinnerType = SpinnerType.SUB_UNIT_LIST
                    )
                )
                Log.e(TAG, "${RFIRoomDb.TAG} - getSubUnitListDataFromDB: Exception: ", e)
            }
        }
    }

    fun getQuestionsDataFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                listOfQuestions =
                    rfiDB.questionsDao().getAllQuestionsList(
                        clientId,
                        RfiDatabase.selectedSchemeId.toInt(),
                        RfiDatabase.selectedGroupId.toInt()
                    ) as ArrayList<QuestionsTableModel>
                if (listOfQuestions.size != 0) {
                    listOfQuestions.add(
                        0,
                        QuestionsTableModel(question = "Select Question", questionId = NO_DATA_AVAILABLE_VAL.toInt())
                    )
                }
                val count = listOfQuestions.size
                Log.d(TAG, "${RFIRoomDb.TAG} - getQuestionsDataFromDB: count: $count ")
                _lvQuestionsData.postValue(count)
            } catch (e: Exception) {
                _lvErrorData.postValue(
                    APIErrorModel(
                        message = "Something is wrong while fetching Questions List Data, Please try again!, ${e.message}",
                        spinnerType = SpinnerType.QUESTIONS_LIST
                    )
                )
                Log.e(TAG, "${RFIRoomDb.TAG} - getQuestionsDataFromDB: Exception: ", e)
            }
        }
    }

    fun getStageDataFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                listOfStage =
                    rfiDB.stageDao().getAllStagesOrFloor(
                        RfiDatabase.selectedSchemeId,
                        RfiDatabase.selectedBuildingId,
                        RfiDatabase.selectedWorkTypeId
                    ) as ArrayList<StageTableModel>
                if (listOfStage.size != 0) {
                    listOfStage.add(
                        0,
                        StageTableModel(floor_Id = NO_DATA_AVAILABLE_VAL, floor_Name = "Select Floor")
                    )
                }
                val count = listOfStage.size
                Log.d(TAG, "${RFIRoomDb.TAG} - getStageDataFromDB: count: $count ")
                _lvStageData.postValue(count)
            } catch (e: Exception) {
                _lvErrorData.postValue(
                    APIErrorModel(
                        message = "Something is wrong while fetching Stage/floor List Data, Please try again!, ${e.message}",
                        spinnerType = SpinnerType.STAGE
                    )
                )
                Log.e(TAG, "${RFIRoomDb.TAG} - getAllStagesOrFloor: Exception: ", e)
            }
        }
    }

    fun getWorkTypeFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                listOfWorkType =
                    rfiDB.workTypeDao()
                        .getAllWork(RfiDatabase.selectedSchemeId) as ArrayList<WorkTypeTableModel>
                if (listOfWorkType.size != 0) {
                    listOfWorkType.add(
                        0,
                        WorkTypeTableModel(activitySequenceGroupId = NO_DATA_AVAILABLE_VAL.toInt(), activitySequenceName = "Select Work")
                    )
                }
                val count = listOfWorkType.size
                Log.d(TAG, "${RFIRoomDb.TAG} - listOfWorkType: count: $count ")
                _lvWorkTypeData.postValue(count)
            } catch (e: Exception) {
                _lvErrorData.postValue(
                    APIErrorModel(
                        message = "Something is wrong while fetching Work type List Data, Please try again!, ${e.message}",
                        spinnerType = SpinnerType.WORK_TYPE
                    )
                )
                Log.e(TAG, "${RFIRoomDb.TAG} - getWorkTypeFromDB: Exception: ", e)
            }
        }
    }

    fun getCheckListDataFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                listOfCheckList =
                    rfiDB.checkListDao().getAllCheckList(
                        RfiDatabase.selectedWorkTypeId,
                        RfiDatabase.selectedNodeId
                    ) as ArrayList<ChecklistTableModel>
                if (listOfCheckList.size != 0) {
                    listOfCheckList.add(
                        0,
                        ChecklistTableModel(Checklist_Name = "Select Check", Checklist_ID = NO_DATA_AVAILABLE_VAL)
                    )
                }
                val count = listOfCheckList.size
                Log.d(TAG, "${RFIRoomDb.TAG} - getCheckListDataFromDB: count: $count ")
                _lvCheckListData.postValue(count)
            } catch (e: Exception) {
                _lvErrorData.postValue(
                    APIErrorModel(
                        message = "Something is wrong while fetching Check List Data, Please try again!, ${e.message}",
                        spinnerType = SpinnerType.CHECK_LIST
                    )
                )
                Log.e(TAG, "${RFIRoomDb.TAG} - getCheckListDataFromDB: Exception: ", e)
            }
        }
    }

    fun getClientData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                list = rfiDB.clientDao().getAllClient() as ArrayList<ClientTableModel>
                if (list.size != 0) {
                    list.add(0, ClientTableModel(clientId = NO_DATA_AVAILABLE_VAL.toInt(), clientName = "Select Client"))
                }
                val count = list.size
                Log.d(TAG, "${RFIRoomDb.TAG} - getClientData: count: $count ")
                _lvClientData.postValue(count)
            } catch (e: Exception) {
                Log.e(TAG, "${RFIRoomDb.TAG} - getClientData: Exception: ", e)
            }
            //Log.d(TAG, "getClientData: list: $list")
        }
    }

    fun getProjectDataFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                listOfProject =
                    rfiDB.projectDao()
                        .getAllProjects(clientId.toString()) as ArrayList<ProjectTableModel>
                if (listOfProject.size != 0) {
                    listOfProject.add(0, ProjectTableModel(PK_Scheme_ID = NO_DATA_AVAILABLE_VAL, Scheme_Name = "Select Project"))
                }
                val count = listOfProject.size
                Log.d(TAG, "${RFIRoomDb.TAG} - getProjectDataFromDB: count: $count ")
                _lvProjectData.postValue(count)
            } catch (e: Exception) {
                Log.d(TAG, "${RFIRoomDb.TAG} - getProjectDataFromDB: Exception, ", e)
            }
        }
    }


    fun getStructureDataFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                listOfStructure =
                    rfiDB.structureDao().getAllStructure(
                        RfiDatabase.selectedSchemeId,
                        RfiDatabase.selectedWorkTypeId
                    ) as ArrayList<StructureTableModel>
                if (listOfStructure.size != 0) {
                    listOfStructure.add(0, StructureTableModel(Bldg_ID = NO_DATA_AVAILABLE_VAL, Bldg_Name = "Select Structure"))
                }
                val count = listOfStructure.size
                Log.d(TAG, "${RFIRoomDb.TAG} - getStructureDataFromDB: count: $count ")
                _lvStructureData.postValue(count)
            } catch (e: Exception) {
                Log.e(TAG, "${RFIRoomDb.TAG} - getStructureDataFromDB: Exception: ", e)
            }
        }
    }

    fun insertAllocateTask(model: AllocateTaskTableModel) {
        viewModelScope.launch(Dispatchers.IO) {
            rfiDB.allocateTaskDao().insert(model)
        }

    }
}