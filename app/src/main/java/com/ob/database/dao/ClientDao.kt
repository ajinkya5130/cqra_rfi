package com.ob.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.ob.database.db_tables.ClientTableModel

@Dao
interface ClientDao: CustomDao<ClientTableModel> {
    @Query("SELECT * FROM Client")
    suspend fun getAllClient(): List<ClientTableModel>

    @Query("SELECT count(*) FROM Client")
    suspend fun isClientDataAvailable(): Int

}