package com.ob.rfi.models


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class StructureResponseModelItem(
    @SerializedName("activitySequenceGroupRfiId")
    val activitySequenceGroupRfiId: Int = 0,

    @SerializedName("clientId")
    val clientId: Int = 0,

    @SerializedName("projectId")
    val projectId: Int = 0,

    @SerializedName("structureId")
    val structureId: Int = 0,

    @SerializedName("structureName")
    val structureName: String = ""
)