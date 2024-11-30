package com.ob.rfi.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnitListApiResponseModelItem(
    val activitySequenceGroupRfiId: Int = 0,
    @SerialName("ChecklistId")
    val checklistId: Int = 0,
    val clientId: Int = 0,
    val projectId: Int = 0,
    var stageId: Int? = 0,
    val unitId: Int = 0,
    val unitName: String = ""
)