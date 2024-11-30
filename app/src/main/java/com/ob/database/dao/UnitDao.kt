package com.ob.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.ob.database.db_tables.UnitTableModel

@Dao
interface UnitDao : CustomDao<UnitTableModel> {
    @Query("SELECT * FROM Unit WHERE Fk_Floor_ID = :floorId")
    suspend fun getAllUnitList(floorId:String): List<UnitTableModel>

}