package com.ob.allocate_task.presentationlayer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ob.database.db_tables.ClientTableModel
import com.ob.rfi.AllocateTask
import com.ob.rfi.CustomTitle
import com.ob.rfi.api.APIClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllocateTaskViewModel: ViewModel() {
    var lvClientData: LiveData<Int>? = null
    private val _lvClientData = MutableLiveData<Int>()
    var list = ArrayList<ClientTableModel>()
        init {
        lvClientData = _lvClientData
    }

   companion object{
       private const val TAG = "AllocateTaskViewModel"
   }

    fun getClientData() {
        viewModelScope.launch(Dispatchers.IO) {
            list = CustomTitle.rfiDB.myRfiDao().getAllClient().toList() as ArrayList<ClientTableModel>
            val count = list.size
            Log.d(TAG, "getClientData: count: $count ")
            _lvClientData.postValue(count)
            //Log.d(TAG, "getClientData: list: $list")
        }
    }

    fun getClientProjectWorkType(rollName: String?, userId: String?) {
        APIClient.getAPIInterface().getClientProjectWorkType(userId, rollName).enqueue(
            object : Callback<ResponseBody?> {
                override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                    if (response.isSuccessful && response.body() != null) {
                        val value = response.body()!!.string()
                        //saveData(value)
                        Log.d(TAG, "onResponse: V: $value")
                    } else {
                        Log.e(TAG, "Response not successful")
                    }
                }

                override fun onFailure(call: Call<ResponseBody?>, throwable: Throwable) {
                    Log.e(TAG, "API call failed: " + throwable.message)
                }
            }
        )
    }

    fun saveData(data: String) {
        if (data.equals("No Record Found", ignoreCase = true)) {
            println("No DATA")
        } else {
            try {
                val tabledata =
                    data.split("\\$".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                Log.d(TAG, "saveData: tabledata: $tabledata")

                val clientArray = JSONArray(tabledata[0])
                Log.d(TAG, "saveData: clientArray: $clientArray")
                /*for (i in 0 until clientArray.length()) {
                    val clientObject = clientArray.getJSONObject(i)
                    val client_id = clientObject.getString("CL_Id")
                    val client_name = clientObject.getString("CL_Name_var")
                    val client_display_name = clientObject
                        .getString("CL_DisplayName_var")
                    val client_address = clientObject
                        .getString("CL_Address_var")
                    val column = "Client_ID,Clnt_Name,CL_Dispaly_Name,Clnt_Adrs,user_id"
                    val values = ("'" + client_id + "','" + client_name
                            + "','" + client_display_name + "','"
                            + client_address + "','" + RfiDatabase.userId + "'")
                    db.insert("Client", column, values)
                    println("Row inserted in Client Table")
                }*/

                val projectArray = JSONArray(tabledata[1])
                Log.d(TAG, "saveData: projectArray: $projectArray")
                /*for (i in 0 until projectArray.length()) {
                    val projectObject = projectArray.getJSONObject(i)
                    val project_id = projectObject.getString("PRJ_Id")
                    val project_name = projectObject
                        .getString("PRJ_Name_var")
                    val project_client_id = projectObject
                        .getString("PRJ_CL_Id")
                    val project_display_name = projectObject
                        .getString("PRJ_DisplayName_var")
                    val project_address = projectObject
                        .getString("PRJ_Address_var")
                    val project_region = projectObject
                        .getString("PRJ_Region_var")
                    val project_scrolling_status = projectObject
                        .getString("PRJ_ScrollingUIStatus_bit")
                    val column =
                        "PK_Scheme_ID,Scheme_Name,Scheme_Cl_Id,Scheme_Diplay_Name,Scheme_Adrs,Scheme_Region,scrolling_status,user_id"
                    val values = ("'" + project_id + "','" + project_name
                            + "','" + project_client_id + "','"
                            + project_display_name + "','" + project_address
                            + "','" + project_region + "','"
                            + project_scrolling_status + "','" + RfiDatabase.userId
                            + "'")
                    db.insert("Scheme", column, values)
                    println("Row inserted in Project Table")
                }*/
                val workTypeArray = JSONArray(tabledata[2])
                Log.d(TAG, "saveData: $workTypeArray")
                /*for (i in 0 until workTypeArray.length()) {
                    val workTypeObject = workTypeArray.getJSONObject(i)
                    val worktype_id = workTypeObject.getString("WT_Id")
                    val worktype_name = workTypeObject
                        .getString("WT_Name_var")
                    val worktype_level_tint = workTypeObject
                        .getString("WT_Level_tint")
                    val worktype_project_id = workTypeObject
                        .getString("PRJ_Id")
                    val column = "WorkTyp_ID,WorkTyp_Name,WorkTyp_level,FK_PRJ_Id,user_id"
                    val values = ("'" + worktype_id + "','" + worktype_name
                            + "','" + worktype_level_tint + "','"
                            + worktype_project_id + "','" + RfiDatabase.userId + "'")
                    db.insert("WorkType", column, values)
                    println("Row inserted in WorkType Table")
                }
                setRFIData()*/
            } catch (e: Exception) {
                Log.d(AllocateTask.TAG, e.toString())
                e.printStackTrace()
                /*flushData()
                displayDialog(
                    "No Record Found",
                    "Sufficient Data is not available."
                )*/
            }
        }
    }

    /*fun isClientDataAvailable(): Int {
       viewModelScope.launch(Dispatchers.IO) {
            val list = CustomTitle.rfiDB.myRfiDao().isClientDataAvailable()

            Log.d(TAG, "getClientData: list: $list")
        }

    }*/
}