package com.ob.allocate_task.presentationlayer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ob.database.db_tables.ClientTableModel
import com.ob.database.db_tables.ProjectTableModel
import com.ob.database.db_tables.WorkTypeTableModel
import com.ob.rfi.CustomTitle
import com.ob.rfi.api.APIClient
import com.ob.rfi.api.ConverterModel
import com.ob.rfi.db.RfiDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection

class AllocateTaskViewModel : ViewModel() {
    var lvClientData: LiveData<Int>? = null
    private val _lvClientData = MutableLiveData<Int>()

    var lvProjectData: LiveData<Int>? = null
    private val _lvProjectData = MutableLiveData<Int>()

    var lvWorkTypeData: LiveData<Int>
    private val _lvWorkTypeData = MutableLiveData<Int>()

    var list = ArrayList<ClientTableModel>()
    var listOfProject = ArrayList<ProjectTableModel>()
    var listOfWorkType = ArrayList<WorkTypeTableModel>()

    init {
        lvClientData = _lvClientData
        lvProjectData = _lvProjectData
        lvWorkTypeData = _lvWorkTypeData
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
            Log.d(TAG, "listOfProject: count: $count ")
            _lvProjectData.postValue(count)
        }
    }

    fun getClientProjectWorkType(rollName: Int, userRole: String?) {
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

    fun getProjectApi(rollName: Int, userRole: String?) {
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
                Log.e(TAG, "getClientProjectWorkType: response: ${response.errorBody()}")
            }

        }
    }

    fun getWorkTypeSequenceApi(rollName: Int, userRole: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = APIClient.getAPIInterface().workTypeSequenceApi(
                rollName,
                userRole
            )
            if (response.isSuccessful && response.code() == HttpURLConnection.HTTP_OK) {
                val model = response.body()
                model?.let {
                    Log.d(TAG, "getProjectApi: response: $it")
                    val dbModel = ConverterModel.convertWorkTypeModel(it)
                    try {
                        CustomTitle.rfiDB.workTypeDao().insertAll(dbModel)
                        getWorkTypeFromDB()
                    } catch (e: Exception) {
                        Log.e(TAG, "getWorkTypeSequenceApi: Exception: ",e )
                    }
                }

            } else {
                Log.e(TAG, "getClientProjectWorkType: response: ${response.errorBody()}")
            }

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