package com.ob.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.ob.database.db_tables.ChecklistTableModel

@Dao
interface CheckListDao : CustomDao<ChecklistTableModel> {
    @Query("SELECT * FROM CheckList WHERE FK_WorkTyp_ID = :workTypeId AND Node_Id = :nodeId")
    suspend fun getAllCheckList(workTypeId:String,nodeId:String): List<ChecklistTableModel>

}