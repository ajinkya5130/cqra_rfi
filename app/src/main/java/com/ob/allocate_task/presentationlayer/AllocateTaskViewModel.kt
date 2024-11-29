package com.ob.allocate_task.presentationlayer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ob.database.db_tables.ClientTableModel
import com.ob.database.db_tables.ProjectTableModel
import com.ob.database.db_tables.StageTableModel
import com.ob.database.db_tables.StructureTableModel
import com.ob.database.db_tables.WorkTypeTableModel
import com.ob.rfi.CustomTitle
import com.ob.rfi.api.APIClient
import com.ob.rfi.api.ConverterModel
import com.ob.rfi.db.RfiDatabase
import com.ob.rfi.models.APIErrorModel
import com.ob.rfi.models.SpinnerType
import com.ob.rfi.models.StageApiResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.net.HttpURLConnection

class AllocateTaskViewModel : ViewModel() {
    var lvClientData: LiveData<Int>? = null
    private val _lvClientData = MutableLiveData<Int>()

    var lvProjectData: LiveData<Int>? = null
    private val _lvProjectData = MutableLiveData<Int>()

    var lvWorkTypeData: LiveData<Int>
    private val _lvWorkTypeData = MutableLiveData<Int>()

    var lvStructureData: LiveData<Int>
    private val _lvStructureData = MutableLiveData<Int>()

    var lvStageData: LiveData<Int>
    private val _lvStageData = MutableLiveData<Int>()

    var lvErrorData: LiveData<APIErrorModel>
    private val _lvErrorData = MutableLiveData<APIErrorModel>()

    var list = ArrayList<ClientTableModel>()
    var listOfProject = ArrayList<ProjectTableModel>()
    var listOfWorkType = ArrayList<WorkTypeTableModel>()
    var listOfStructure = ArrayList<StructureTableModel>()
    var listOfStage = arrayListOf<StageTableModel>()

    init {
        lvClientData = _lvClientData
        lvProjectData = _lvProjectData
        lvWorkTypeData = _lvWorkTypeData
        lvStructureData = _lvStructureData
        lvStageData = _lvStageData
        lvErrorData = _lvErrorData
    }

    companion object {
        private const val TAG = "AllocateTaskViewModel"
    }

    fun getClientData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                list = CustomTitle.rfiDB.clientDao().getAllClient() as ArrayList<ClientTableModel>
                if (list.size != 0) {
                    list.add(0, ClientTableModel(-1, Clnt_Name = "Select Client"))
                }
                val count = list.size
                Log.d(TAG, "getClientData: count: $count ")
                _lvClientData.postValue(count)
            } catch (e: Exception) {
                Log.e(TAG, "getClientData: Exception: ",e )
            }
            //Log.d(TAG, "getClientData: list: $list")
        }
    }

    fun getProjectDataFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            listOfProject =
                CustomTitle.rfiDB.projectDao().getAllProjects(RfiDatabase.selectedClientId) as ArrayList<ProjectTableModel>
            if (listOfProject.size != 0) {
                listOfProject.add(0, ProjectTableModel(-1, Scheme_Name = "Select Project"))
            }
            val count = listOfProject.size
            Log.d(TAG, "getProjectDataFromDB: count: $count ")
            _lvProjectData.postValue(count)
        }
    }

    fun getClientProjectWorkType(rollName: Int, userRole: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = APIClient.getAPIInterface().getClientsAPI(
                rollName,
                userRole
            )
            if (response.isSuccessful && response.code() == HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "getClientProjectWorkType: response: ${response.body()}")
                val model = response.body()
                model?.let {
                    val dbModel = ConverterModel.convertClientData(it[0])
                    CustomTitle.rfiDB.clientDao().insert(dbModel)
                    getClientData()
                }

            } else {
                Log.e(TAG, "getClientProjectWorkType: response: ${response.errorBody()}")
            }

        }
    }


    fun getStructureDataFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            listOfStructure =
                CustomTitle.rfiDB.structureDao().getAllStructure(RfiDatabase.selectedSchemeId,RfiDatabase.selectedWorkTypeId) as ArrayList<StructureTableModel>
            if (listOfStructure.size != 0) {
                listOfStructure.add(0, StructureTableModel(-1, Bldg_Name = "Select Structure"))
            }
            val count = listOfStructure.size
            Log.d(TAG, "getStructureDataFromDB: count: $count ")
            _lvStructureData.postValue(count)
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
                    val dbModel = ConverterModel.convertProjectData(it[0].project)
                    CustomTitle.rfiDB.projectDao().insert(dbModel)
                    getProjectDataFromDB()
                }

            } else {
                Log.e(TAG, "getProjectApi: response: ${response.errorBody()}")
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
                    val dbModel = ConverterModel.convertWorkTypeModel(it)
                    try {
                        CustomTitle.rfiDB.workTypeDao().insertAll(dbModel)
                        getWorkTypeFromDB()
                    } catch (e: Exception) {
                        Log.e(TAG, "getWorkTypeSequenceApi: Exception: ",e )
                    }
                }

            } else {
                Log.e(TAG, "getWorkTypeSequenceApi: response: ${response.errorBody()}")
            }

        }
    }

    fun getStructureApi(rollName: Int, userRole: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = APIClient.getAPIInterface().getStructureApi(
                rollName,
                userRole,
                RfiDatabase.selectedClientId,
                RfiDatabase.selectedSchemeId,
                RfiDatabase.selectedWorkTypeId
            )
            if (response.isSuccessful && response.code() == HttpURLConnection.HTTP_OK) {
                val model = response.body()
                model?.let {
                    Log.d(TAG, "getStructureApi: response: $it")
                    val dbModel = ConverterModel.convertStructureModel(it)
                    try {
                        CustomTitle.rfiDB.structureDao().insertAll(dbModel)
                        getStructureDataFromDB()
                    } catch (e: Exception) {
                        Log.e(TAG, "getStructureApi: Exception: ",e )
                    }
                }

            } else {
                Log.e(TAG, "getStructureApi: response: ${response.errorBody()}")
            }

        }
    }

    fun getStageApi(rollName: Int, userRole: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = APIClient.getAPIInterface().getStageApi(
                rollName,
                userRole,
                RfiDatabase.selectedClientId,
                RfiDatabase.selectedSchemeId,
                RfiDatabase.selectedWorkTypeId,
                RfiDatabase.selectedBuildingId
            )
            if (response.isSuccessful && response.code() == HttpURLConnection.HTTP_OK) {
                val responseBody: ResponseBody = response.body()!!
                Log.d(TAG, "getStageApi: response: $responseBody")
                val model = Gson().fromJson(responseBody.string(), StageApiResponseModel::class.java)
                model?.let {
                    Log.d(TAG, "getStageApi: response: $it")
                    val dbModel = ConverterModel.convertStageModel(it)
                    try {
                        CustomTitle.rfiDB.stageDao().insertAll(dbModel)
                        getStageDataFromDB()
                    } catch (e: Exception) {
                        Log.e(TAG, "getStageApi: Exception: ",e )
                    }
                }

            } else {
                Log.e(TAG, "getStageApi: response: ${response.errorBody()}")
                response.errorBody()?.let {
                    val model = Gson().fromJson(it.string(), APIErrorModel::class.java)
                    model.message = "Something is wrong while fetching Stage Data, Please try again!"
                    model.spinnerType = SpinnerType.STAGE
                    listOfStage = arrayListOf()
                    _lvErrorData.postValue(model)
                    // TODO: 30/11/24 clear stage SPinner

                    Log.e(TAG, "getStageApi: Error model : $model")
                }

            }

        }
    }


    fun getStageDataFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            listOfStage =
                CustomTitle.rfiDB.stageDao().getAllStagesOrFloor(RfiDatabase.selectedSchemeId,RfiDatabase.selectedBuildingId,RfiDatabase.selectedWorkTypeId) as ArrayList<StageTableModel>
            if (listOfStage.size != 0) {
                listOfStage.add(0, StageTableModel(floor_Id = "-1", floor_Name = "Select Floor"))
            }
            val count = listOfStage.size
            Log.d(TAG, "getStageDataFromDB: count: $count ")
            _lvStageData.postValue(count)
        }
    }

    fun getWorkTypeFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                listOfWorkType =
                    CustomTitle.rfiDB.workTypeDao().getAllWork(RfiDatabase.selectedSchemeId) as ArrayList<WorkTypeTableModel>
                if (listOfWorkType.size != 0) {
                    listOfWorkType.add(0, WorkTypeTableModel(-1, activitySequenceName = "Select Work"))
                }
                val count = listOfWorkType.size
                Log.d(TAG, "listOfWorkType: count: $count ")
                _lvWorkTypeData.postValue(count)
            } catch (e: Exception) {
                Log.e(TAG, "getWorkTypeFromDB: Exception: ",e )
            }
        }
    }
}