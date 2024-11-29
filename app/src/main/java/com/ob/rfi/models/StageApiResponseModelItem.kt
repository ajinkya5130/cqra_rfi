package com.ob.rfi.models


import kotlinx.serialization.Serializable

@Serializable
data class StageApiResponseModelItem(
    val activitySequenceGroupRfiId: Int = 0,
    val clientId: Int = 0,
    val projectId: Int = 0,
    val stageId: Int = 0,
    val stageName: String = "",
    val structureId: Int = 0
)