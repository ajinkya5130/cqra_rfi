package com.ob.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.ob.database.db_tables.QuestionsTableModel

@Dao
interface QuestionsDao : CustomDao<QuestionsTableModel> {
    @Query("SELECT * FROM question WHERE clientId = :clientId AND projectId =:projectId AND groupId=:groupId")
    suspend fun getAllQuestionsList(clientId:String,projectId:String,groupId:String): List<QuestionsTableModel>

}