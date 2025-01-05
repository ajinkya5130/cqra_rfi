package com.ob.rfi.models


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectApiResponseModelItem(
    @Expose
    val project: Project,
    @SerializedName("node_user_detail")
    val nodeUserDetail: NodeUserDetail = NodeUserDetail(),
)