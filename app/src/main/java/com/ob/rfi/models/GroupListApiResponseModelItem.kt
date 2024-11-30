package com.ob.rfi.models


import kotlinx.serialization.Serializable

@Serializable
data class GroupListApiResponseModelItem(
    val checklistRfiId: Int = 0,
    val groupId: Int = 0,
    val groupName: String = "",
    val groupSequence: Int = 0
)