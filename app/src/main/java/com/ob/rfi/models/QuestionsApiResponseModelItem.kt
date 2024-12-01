package com.ob.rfi.models


import kotlinx.serialization.Serializable

@Serializable
data class QuestionsApiResponseModelItem(
    val checklistRfiId: Int = 0,
    val clientId: Int = 0,
    val description: String = "",
    val groupRfiId: Int = 0,
    val projectId: Int = 0,
    val questionRfiId: Int = 0,
    val questionType: String = "",
    val sequenceNo: Int = 0,
    val structureRfiId: Int = 0
)