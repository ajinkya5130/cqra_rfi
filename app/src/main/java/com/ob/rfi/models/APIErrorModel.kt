package com.ob.rfi.models


import kotlinx.serialization.Serializable

@Serializable
data class APIErrorModel(
    val error: String = "",
    var message: String = "",
    val path: String = "",
    val status: Int = 0,
    val timestamp: String = "",
    var spinnerType: SpinnerType = SpinnerType.NONE
)

enum class SpinnerType(int: Int){
    NONE(0),
    CLIENT(1),
    PROJECT(2),
    WORK_TYPE(3),
    STRUCTURE(4),
    STAGE(5),
    CHECKLIST(6),
    GroupLIST(7),
}