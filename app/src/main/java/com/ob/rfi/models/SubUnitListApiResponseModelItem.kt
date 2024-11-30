package com.ob.rfi.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubUnitListApiResponseModelItem(
    val activitySequenceGroupRfiId: Int = 0,
    @SerialName("ChecklistId")
    val checklistId: Int = 0,
    val clientId: Int = 0,
    val projectId: Int = 0,
    val subUnitId: Int = 0,
    val subUnitName: String = "",
    val unitId: Int = 0
)