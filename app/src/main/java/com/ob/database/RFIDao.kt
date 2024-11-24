package com.ob.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ob.database.db_tables.ClientTableModel

@Dao
interface RFIDao {
    @Query("SELECT * FROM Client")
    suspend fun getAllClient(): List<ClientTableModel>

    @Query("SELECT count(*) FROM Client")
    suspend fun isClientDataAvailable(): Int

    @Insert
    fun insert(entity: ClientTableModel)

    // ... other DAO methods
}