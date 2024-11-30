package com.ob.rfi.models


import kotlinx.serialization.Serializable

@Serializable
data class ChecklistApiResponseModelItem(
    val activitySeqGroupRfiId: Int = 0,
    val chklId: Int = 0,
    val chklName: String = "",
    val clientId: Int = 0,
    val projectId: Int = 0
)