package com.ob.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.ob.database.db_tables.ClientAllocateTaskModel
import com.ob.database.db_tables.ProjectAllocateTaskModel

@Dao
interface ClientAllocateTaskDao {

    @Query("SELECT a.clientId, c.clientName FROM allocateTask as a JOIN Client as c where a.clientId=c.clientId")
    suspend fun getClientAllocateData(): List<ClientAllocateTaskModel>

    @Query("SELECT distinct(s.PK_Scheme_ID) as schemaOrProjectId, s.Scheme_Name as schemaOrProjectName, s.scrolling_status as scrollingStatus FROM allocateTask as a JOIN Scheme as s where s.PK_Scheme_ID = a.projectId and s.Scheme_Cl_Id =:clientId")
    suspend fun getProjectAllocateData(clientId:String): List<ProjectAllocateTaskModel>

}