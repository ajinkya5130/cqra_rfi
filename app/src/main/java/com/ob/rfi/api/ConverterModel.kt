package com.ob.rfi.api

import com.ob.database.db_tables.ClientTableModel
import com.ob.database.db_tables.ProjectTableModel
import com.ob.database.db_tables.StructureTableModel
import com.ob.database.db_tables.WorkTypeTableModel
import com.ob.rfi.models.ClientApiResponseModelItem
import com.ob.rfi.models.Project
import com.ob.rfi.models.StructureResponseModelItem
import com.ob.rfi.models.WorkTypeResponseModelItem

object ConverterModel {

    fun convertClientData(model: ClientApiResponseModelItem): ClientTableModel {
        val clModel = ClientTableModel().apply {
            Client_ID = model.clId.toString()
            Clnt_Name = model.clName
            CL_Dispaly_Name = model.clDisplayname
            Clnt_Adrs = model.clAddress

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
                pk_building_id  = model.structureId
                Bldg_ID = model.structureId.toString()
                Bldg_Name = model.structureName
                Build_scheme_id = model.projectId.toString()
                FK_WorkTyp_ID = model.activitySequenceGroupRfiId.toString()
            })
        }
        return list
    }
}