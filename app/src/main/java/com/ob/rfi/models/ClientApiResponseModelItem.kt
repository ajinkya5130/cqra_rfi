package com.ob.rfi.models


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class ClientApiResponseModelItem(
    @SerializedName("cl_address")
    @Expose
    var clAddress: String ="",
    @SerializedName("cl_displayname")
    @Expose
    var clDisplayname: String = "",
    @SerializedName("cl_id")
    @Expose
    var clId: Int = 0,
    @SerializedName("cl_name")
    @Expose
    var clName: String = ""
)