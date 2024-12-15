package com.ob.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.ob.database.db_tables.AnswerTableModel

@Dao
interface AnswerDao : CustomDao<AnswerTableModel> {

    @Query("SELECT DISTINCT FK_question_id,* FROM answer WHERE fk_question_id = :qId AND Fk_CHKL_Id = :selectedChecklistId AND Fk_Grp_ID = :selectedGroupId AND rfi_id = :selectedRfiId")
     suspend fun getAnswer(
        qId: String,
        selectedChecklistId: String,
        selectedGroupId: String,
        selectedRfiId: String
    ): AnswerTableModel

    @Query("SELECT DISTINCT FK_question_id,* FROM answer WHERE fk_question_id = :qId AND rfi_id = :selectedRfiId")
     suspend fun getAnswerUsingRfId(
        qId: String,
        selectedRfiId: String
    ): AnswerTableModel

}