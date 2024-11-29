package com.ob.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.ob.database.db_tables.StructureTableModel

@Dao
interface StructureDao : CustomDao<StructureTableModel> {
    @Query("SELECT * FROM Building WHERE Build_scheme_id =:projectId AND FK_WorkTyp_ID = :workTypeId")
    suspend fun getAllStructure(projectId: String,workTypeId:String): List<StructureTableModel>

}