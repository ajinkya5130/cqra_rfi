package com.ob.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.ob.database.db_tables.AllocateTaskTableModel

@Dao
interface AllocateTaskDao : CustomDao<AllocateTaskTableModel> {
    @Query("SELECT * FROM allocateTask")
    suspend fun getAllQuestionsList(): List<AllocateTaskTableModel>

}