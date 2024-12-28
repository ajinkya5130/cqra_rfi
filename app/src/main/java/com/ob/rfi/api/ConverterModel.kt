package com.ob.rfi.api

import com.ob.database.db_tables.ChecklistTableModel
import com.ob.database.db_tables.ClientTableModel
import com.ob.database.db_tables.GroupListTableModel
import com.ob.database.db_tables.ProjectTableModel
import com.ob.database.db_tables.QuestionsTableModel
import com.ob.database.db_tables.StageTableModel
import com.ob.database.db_tables.StructureTableModel
import com.ob.database.db_tables.SubUnitTableModel
import com.ob.database.db_tables.UnitTableModel
import com.ob.database.db_tables.WorkTypeTableModel
import com.ob.rfi.db.RfiDatabase
import com.ob.rfi.models.ChecklistApiResponseModelItem
import com.ob.rfi.models.ClientApiResponseModelItem
import com.ob.rfi.models.GroupListApiResponseModelItem
import com.ob.rfi.models.Project
import com.ob.rfi.models.QuestionsApiResponseModelItem
import com.ob.rfi.models.StageApiResponseModelItem
import com.ob.rfi.models.StructureResponseModelItem
import com.ob.rfi.models.SubUnitListApiResponseModelItem
import com.ob.rfi.models.UnitListApiResponseModelItem
import com.ob.rfi.models.WorkTypeResponseModelItem

object ConverterModel {

    fun convertClientData(model: ClientApiResponseModelItem): ClientTableModel {
        val clModel = ClientTableModel().apply {
            clientId = model.clId
            clientName = model.clName
            clientDisplay = model.clDisplayname
            clientAddress = model.clAddress

        }
        return clModel
    }

    fun convertProjectData(model: Project): ProjectTableModel {
        val projModel = ProjectTableModel().apply {
            Scheme_Name = model.prjName
            Scheme_Cl_Id = model.clientId.toString()
            Scheme_Diplay_Name = model.prjDisplayname
            Scheme_Adrs = model.prjAddress
            Scheme_Region = model.prjRegion
            PK_Scheme_ID = model.prjId.toString()
            user_id = RfiDatabase.userId
            //scrolling_status = model.prjScrollinguistatus
        }
        return projModel
    }

    fun convertWorkTypeModel(listOfWorkType: ArrayList<WorkTypeResponseModelItem>): List<WorkTypeTableModel> {
        val list = ArrayList<WorkTypeTableModel>()
        listOfWorkType.forEach { model ->
            list.add(WorkTypeTableModel().apply {
                activitySequenceGroupId  = model.activitySequenceGroupId
                activitySequenceLevel = model.activitySequenceLevel?:0
                activitySequenceName = model.activitySequenceName
                activitySequenceStatus = model.activitySequenceStatus
                projectId = model.prjId
            })
        }
        return list
    }

    fun convertStructureModel(listOfStructure: ArrayList<StructureResponseModelItem>): List<StructureTableModel> {
        val list = ArrayList<StructureTableModel>()
        listOfStructure.forEach { model ->
            list.add(StructureTableModel().apply{
                //pk_building_id  = model.structureId
                Bldg_ID = model.structureId.toString()
                Bldg_Name = model.structureName
                Build_scheme_id = model.projectId.toString()
                FK_WorkTyp_ID = model.activitySequenceGroupRfiId.toString()
                user_id = RfiDatabase.userId
            })
        }
        return list
    }

    fun convertCheckListModel(selectedNodeId:String,checkList: ArrayList<ChecklistApiResponseModelItem>): List<ChecklistTableModel> {
        val list = ArrayList<ChecklistTableModel>()
        checkList.forEach { model ->
            list.add(ChecklistTableModel().apply{
                //pk_building_id  = model.structureId
                Checklist_ID = model.chklId.toString()
                Checklist_Name = model.chklName
                Node_Id = selectedNodeId
                FK_WorkTyp_ID = model.activitySeqGroupRfiId.toString()
                user_id = RfiDatabase.userId
            })
        }
        return list
    }
    fun convertGroupListModel(selectedNodeId:String,checkList: ArrayList<GroupListApiResponseModelItem>): List<GroupListTableModel> {
        val list = ArrayList<GroupListTableModel>()
        checkList.forEach { model ->
            list.add(GroupListTableModel().apply{
                Grp_ID = model.groupId.toString()
                Grp_Name = model.groupName
                Node_id = selectedNodeId
                FK_Checklist_ID = model.checklistRfiId.toString()
                GRP_Sequence_tint = model.groupSequence.toString()
                user_id = RfiDatabase.userId
            })
        }
        return list
    }

    fun convertStageModel(listOfStage: ArrayList<StageApiResponseModelItem>): List<StageTableModel> {
        val list = ArrayList<StageTableModel>()
        listOfStage.forEach { model ->
            list.add(StageTableModel().apply{
                //pk_building_id  = model.structureId
                floor_Id = model.stageId.toString()
                floor_Name = model.stageName
                FK_Bldg_ID = model.structureId.toString()
                FK_WorkTyp_ID = model.activitySequenceGroupRfiId.toString()
                Floor_Scheme_ID = model.projectId.toString()
                user_id = RfiDatabase.userId

            })
        }
        return list
    }

    fun convertUnitModel(listOfStage: ArrayList<UnitListApiResponseModelItem>): List<UnitTableModel> {
        val list = ArrayList<UnitTableModel>()
        listOfStage.forEach { model ->
            list.add(UnitTableModel().apply{
                //pk_building_id  = model.structureId
                Unit_ID = model.unitId.toString()
                Unit_Name = model.unitName
                Fk_Floor_ID = (model.stageId?:0).toString()
                Unit_Scheme_id = model.projectId.toString()
                FK_WorkTyp_ID = model.activitySequenceGroupRfiId.toString()
                user_id = RfiDatabase.userId
            })
        }
        return list
    }

    fun convertSubUnitModel(listOfStage: ArrayList<SubUnitListApiResponseModelItem>): List<SubUnitTableModel> {
        val list = ArrayList<SubUnitTableModel>()
        listOfStage.forEach { model ->
            list.add(SubUnitTableModel().apply{
                //pk_building_id  = model.structureId
                subUnitId = model.subUnitId.toString()
                subUnitName = model.subUnitName
                fkUnitId = model.unitId.toString()
                fkWorkTypId = model.activitySequenceGroupRfiId.toString()
                subUnitSchemeId = model.projectId.toString()
                userId = RfiDatabase.userId
            })
        }
        return list
    }

    fun convertQuestionsModel(
        listOfStage: ArrayList<QuestionsApiResponseModelItem>,
        selectedBuildingId: String =""
    ): List<QuestionsTableModel> {
        val list = ArrayList<QuestionsTableModel>()
        listOfStage.forEach { model ->
            list.add(QuestionsTableModel().apply{
                questionId = model.questionRfiId
                question = model.description
                groupId = model.groupRfiId
                questionSequence = model.sequenceNo
                questionType = model.questionType
                checklistId = model.checklistRfiId
                nodeId = selectedBuildingId
                structureId = model.structureRfiId
                clientId = model.clientId
                projectId = model.projectId
                userId = RfiDatabase.userId.toInt()
            })
        }
        return list
    }
}