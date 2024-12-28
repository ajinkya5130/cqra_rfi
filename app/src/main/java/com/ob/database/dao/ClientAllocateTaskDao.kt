package com.ob.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.ob.database.db_tables.AllocateTaskTableModel
import com.ob.database.db_tables.BuildingAllocateTaskModel
import com.ob.database.db_tables.CheckListAllocateTaskModel
import com.ob.database.db_tables.ClientAllocateTaskModel
import com.ob.database.db_tables.FloorAllocateTaskModel
import com.ob.database.db_tables.GroupListAllocateTaskModel
import com.ob.database.db_tables.ProjectAllocateTaskModel
import com.ob.database.db_tables.UnitAllocateTaskModel
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

    @Query("SELECT distinct(u.Unit_ID) as unitId, u.Unit_Des as unitName FROM allocateTask as a JOIN Unit as u where u.Unit_ID = a.unitId and u.Fk_Floor_ID =:floorId and u.Unit_Scheme_id =:schemaOrProjectId")
    suspend fun getUnitAllocateData(floorId:String,schemaOrProjectId:String): List<UnitAllocateTaskModel>

    @Query("SELECT * FROM allocateTask where projectId =:schemaOrProjectId and unitId in (:unitId) and groupId =:groupId")
    suspend fun getAllocateData(schemaOrProjectId:String,unitId:String,groupId:Int): AllocateTaskTableModel

    /*@Query("SELECT Sub_Unit_Des,Sub_Unit_ID FROM SubUnit WHERE Sub_Unit_Id in (:subUnits) GROUP by Sub_Unit_Id")
    suspend fun getSubUnitDataFromDB(subUnits:String): List<SubUnitTableModel>*/

    @Query("SELECT distinct(c.Checklist_ID) as checkListId, c.CheckList_Name as checkListName FROM allocateTask as a JOIN CheckList as c where c.Checklist_ID = a.checkListId and c.FK_WorkTyp_ID =:workTypeId")
    suspend fun getCheckListAllocateData(workTypeId:String): List<CheckListAllocateTaskModel>

    @Query("SELECT distinct(g.Grp_ID) as groupId, g.Grp_Name as groupName FROM allocateTask as a JOIN Group1 as g where g.Grp_ID = a.groupId and g.FK_Checklist_ID =:checkListId ORDER BY g.GRP_Sequence_tint ASC")
    suspend fun getGroupListAllocateData(checkListId:String): List<GroupListAllocateTaskModel>

}