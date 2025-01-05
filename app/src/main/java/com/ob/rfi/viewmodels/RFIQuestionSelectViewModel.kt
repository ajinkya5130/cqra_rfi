package com.ob.rfi.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ob.database.RFIRoomDb
import com.ob.database.db_tables.AnswerTableModel
import com.ob.database.db_tables.QuestionsTableModel
import com.ob.rfi.RfiApplication.rfiDB
import com.ob.rfi.db.RfiDatabase
import com.ob.rfi.db.RfiDatabase.selectedChecklistId
import com.ob.rfi.db.RfiDatabase.selectedGroupId
import com.ob.rfi.db.RfiDatabase.selectedrfiId
import com.ob.rfi.models.APIErrorModel
import com.ob.rfi.models.SpinnerType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RFIQuestionSelectViewModel:ViewModel() {

    companion object{
        private const val TAG = "RFIQuestionSelectViewMo"
    }
    var listOfQuestions = arrayListOf<QuestionsTableModel>()

    var lvQuestionsData: LiveData<Int>? = null
    private val _lvQuestionsData = MutableLiveData<Int>()

    private val _lvErrorData = MutableLiveData<APIErrorModel>()
    var lvErrorData: LiveData<APIErrorModel>

    init {
        lvQuestionsData = _lvQuestionsData
        lvErrorData = _lvErrorData
    }

    fun getQuestionListFromDb(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                listOfQuestions =
                    rfiDB.questionsDao().getQuestionOnChecklistId(
                        RfiDatabase.selectedChecklistId.toInt(),
                        RfiDatabase.selectedBuildingId.toInt(),
                        RfiDatabase.selectedGroupId.toInt()
                    ) as ArrayList<QuestionsTableModel>
                /*if (listOfQuestions.size != 0) {
                    listOfQuestions.add(
                        0,
                        QuestionsTableModel(question = "Select Question", questionId = NO_DATA_AVAILABLE_VAL.toInt())
                    )
                }*/
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

    fun insertDefaultAnswer(answerTableModel: AnswerTableModel) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val value = rfiDB.answerDao().insert(answerTableModel)
                Log.d(TAG, "insertDefaultAnswer: value: $value")
            }catch (e:Exception){
                Log.e(TAG, "insertDefaultAnswer: Exception: ",e)
            }
        }

    }

    private suspend fun getQuestionAnswer(qId: String): AnswerTableModel {
        val value = rfiDB.answerDao().getAnswer(qId,selectedChecklistId,selectedGroupId,selectedrfiId)
        Log.d(TAG, "getQuestionAnswer: value: $value")

        return value
    }

    fun getQuestionAnswerAsync(
        qId: String,
        callback: (AnswerTableModel?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val value = getQuestionAnswer(qId)
                withContext(Dispatchers.Main) {
                    callback(value) // Pass the result to Java
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback(null) // Handle error case
                }
            }
        }
    }
}