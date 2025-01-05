package com.ob.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.ob.database.db_tables.NodeUserTableModel

@Dao
interface NodeUserDetailsDao: CustomDao<NodeUserTableModel> {
    @Query("SELECT * FROM NodeUserDetails")
    suspend fun getNodeUserData(): NodeUserTableModel

}