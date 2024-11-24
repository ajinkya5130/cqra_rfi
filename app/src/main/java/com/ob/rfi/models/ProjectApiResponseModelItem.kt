package com.ob.rfi.models


import com.google.gson.annotations.Expose
import kotlinx.serialization.Serializable

@Serializable
data class ProjectApiResponseModelItem(
    @Expose
    val project: Project
)