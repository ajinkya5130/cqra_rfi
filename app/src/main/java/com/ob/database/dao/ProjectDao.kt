package com.ob.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.ob.database.db_tables.ProjectTableModel

@Dao
interface ProjectDao: CustomDao<ProjectTableModel> {
    @Query("SELECT * FROM Scheme Where Scheme_Cl_Id=:clientId")
    suspend fun getAllProjects(clientId: String): List<ProjectTableModel>

    @Query("SELECT count(*) FROM Scheme")
    suspend fun isClientDataAvailable(): Int

}