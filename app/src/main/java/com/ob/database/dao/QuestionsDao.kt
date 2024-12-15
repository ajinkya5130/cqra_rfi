package com.ob.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.ob.database.db_tables.QuestionsTableModel

@Dao
interface QuestionsDao : CustomDao<QuestionsTableModel> {
    @Query("SELECT * FROM question WHERE clientId = :clientId AND projectId =:projectId AND groupId=:groupId")
    suspend fun getAllQuestionsList(clientId:Int,projectId:Int,groupId:Int): List<QuestionsTableModel>

    @Query("SELECT * FROM question WHERE checklistId = :checkListId AND structureId =:structureId AND groupId=:groupId")
    suspend fun getQuestionOnChecklistId(checkListId:Int,structureId:Int,groupId:Int): List<QuestionsTableModel>

}