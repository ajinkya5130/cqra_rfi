package com.ob.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.ob.database.db_tables.GroupListTableModel

@Dao
interface GroupListDao : CustomDao<GroupListTableModel> {
    @Query("SELECT * FROM Group1 WHERE FK_Checklist_ID = :checkId AND Node_Id = :nodeId")
    suspend fun getAllGroupList(checkId:String,nodeId:String): List<GroupListTableModel>

}