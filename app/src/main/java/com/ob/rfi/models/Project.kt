package com.ob.rfi.models


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Project(
    @SerializedName("client_id")
    @Expose
    var clientId: Int =0,
    @SerializedName("prj_address")
    @Expose
    var prjAddress: String,
    @SerializedName("prj_displayname")
    @Expose
    val prjDisplayname: String,
    @SerializedName("prj_id")
    @Expose
    val prjId: Int = 0,
    @SerializedName("prj_name")
    @Expose
    val prjName: String,
    @SerializedName("prj_region")
    @Expose
    val prjRegion: String,
    @SerializedName("prj_scrollinguistatus")
    @Expose
    val prjScrollinguistatus: String =""
)