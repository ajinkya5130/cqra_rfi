package com.ob.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.ob.database.db_tables.WorkTypeTableModel

@Dao
interface WorkTypeDao : CustomDao<WorkTypeTableModel> {
    @Query("SELECT * FROM WorkTypeSeq Where projectId=:projectId")
    suspend fun getAllWork(projectId: String): List<WorkTypeTableModel>

    @Query("SELECT count(*) FROM Scheme")
    suspend fun isClientDataAvailable(): Int


}