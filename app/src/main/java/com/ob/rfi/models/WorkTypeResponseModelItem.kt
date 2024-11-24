package com.ob.rfi.models


import com.google.gson.annotations.SerializedName

data class WorkTypeResponseModelItem(
    @SerializedName("activity_sequence_groupid")
    val activitySequenceGroupId: Int = 0,
    @SerializedName("activity_sequence_level")
    val activitySequenceLevel: Int? = 0,
    @SerializedName("activity_sequence_name")
    val activitySequenceName: String = "",
    @SerializedName("activity_sequence_status")
    val activitySequenceStatus: Int = 0,
    @SerializedName("prj_id")
    val prjId: Int = 0
)