package com.ob.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.ob.database.db_tables.SubUnitTableModel

@Dao
interface SubUnitDao : CustomDao<SubUnitTableModel> {
    @Query("SELECT * FROM SubUnit WHERE Fk_Unit_ID = :unitId AND Sub_Unit_Scheme_id =:schemeId AND Fk_WorkTyp_ID =:workTypeId")
    suspend fun getAllSubUnitList(unitId:String,schemeId:String,workTypeId:String): List<SubUnitTableModel>

    @Query("SELECT * FROM SubUnit WHERE Sub_Unit_ID in (:subUnits) GROUP by Sub_Unit_ID")
    suspend fun getSubUnitDataFromDB(subUnits:List<String>): List<SubUnitTableModel>

}