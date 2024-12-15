package com.ob.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.ob.database.db_tables.CreateRFITableModel

@Dao
interface CreateRFITableDao: CustomDao<CreateRFITableModel> {
    @Query("SELECT * FROM Created_RFI ORDER BY _id DESC")
    suspend fun getAllCreatedRFI(): List<CreateRFITableModel>

    @Query("SELECT count(*) FROM Created_RFI")
    suspend fun isClientDataAvailable(): Int

}