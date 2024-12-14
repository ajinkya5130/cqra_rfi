package com.ob.rfi.models


import kotlinx.serialization.Serializable

@Serializable
data class SignInResponseModel(
    val accessToken: String = "",
    val email: String = "",
    val id: Int = 0,
    val representingType: Int = 0,
    val roleId: Int = 0,
    val roles: List<Any> = listOf(),
    val tokenType: String = "",
    val userfullname: String = "",
    val username: String = ""
)