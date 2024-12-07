package com.ob.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.ob.database.db_tables.BuildingAllocateTaskModel
import com.ob.database.db_tables.ClientAllocateTaskModel
import com.ob.database.db_tables.FloorAllocateTaskModel
import com.ob.database.db_tables.ProjectAllocateTaskModel
import com.ob.database.db_tables.WorkTypeAllocateTaskModel

@Dao
interface ClientAllocateTaskDao {

    @Query("SELECT a.clientId, c.clientName FROM allocateTask as a JOIN Client as c where a.clientId=c.clientId")
    suspend fun getClientAllocateData(): List<ClientAllocateTaskModel>

    @Query("SELECT distinct(s.PK_Scheme_ID) as schemaOrProjectId, s.Scheme_Name as schemaOrProjectName, s.scrolling_status as scrollingStatus FROM allocateTask as a JOIN Scheme as s where s.PK_Scheme_ID = a.projectId and s.Scheme_Cl_Id =:clientId")
    suspend fun getProjectAllocateData(clientId:String): List<ProjectAllocateTaskModel>

    @Query("SELECT distinct(w.activitySequenceGroupId) as workTypeId, w.activitySequenceName as workTypeName FROM allocateTask as a JOIN WorkTypeSeq as w where w.activitySequenceGroupId = a.workTypeId and w.projectId =:projectId")
    suspend fun getWorkTypeAllocateData(projectId:String): List<WorkTypeAllocateTaskModel>

    @Query("SELECT distinct(b.Bldg_ID) as buildingId, b.Bldg_Name as buildingName FROM allocateTask as a JOIN Building as b where b.Bldg_ID = a.structureId and b.FK_WorkTyp_ID =:workTypeId")
    suspend fun getBuildingAllocateData(workTypeId:String): List<BuildingAllocateTaskModel>

    @Query("SELECT distinct(f.floor_Id) as floorId, f.floor_Name as floorName FROM allocateTask as a JOIN floor as f where f.floor_Id = a.stageId and f.FK_Bldg_ID =:buildingId")
    suspend fun getFloorAllocateData(buildingId:String): List<FloorAllocateTaskModel>

}