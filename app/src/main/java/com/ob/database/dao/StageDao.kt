package com.ob.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.ob.database.db_tables.StageTableModel

@Dao
interface StageDao : CustomDao<StageTableModel> {
    @Query("SELECT * FROM floor WHERE Floor_Scheme_ID =:projectId AND FK_Bldg_ID = :buildingId AND FK_WorkTyp_ID = :workTypeId")
    suspend fun getAllStagesOrFloor(projectId: String,buildingId:String,workTypeId:String): List<StageTableModel>

}