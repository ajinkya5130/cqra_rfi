package com.ob.rfi.api

import com.ob.AppUtil
import com.ob.rfi.models.ClientApiResponseModel
import com.ob.rfi.models.ProjectApiResponseModel
import com.ob.rfi.models.WorkTypeResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface APIInterface {

    @GET("clients_rfi/clientsRfiAndroid/{userID}/{userRole}")
   suspend fun getClientsAPI(
        @Path("userID") userId: Int?,
        @Path("userRole") userRole: String?,
        @Header("Authorization") authHeader: String = "${AppUtil.BEARER_STRING_CONST} ${AppUtil.FIREBASE_AUTH_TOKEN}"
    ): Response<ClientApiResponseModel>

    @GET("projects_rfi/getProjectsByUserIdAndRoleAndroid/{userID}/{userRole}")
   suspend fun getProjectApi(
        @Path("userID") userId: Int?,
        @Path("userRole") userRole: String?,
        @Header("Authorization") authHeader: String = "${AppUtil.BEARER_STRING_CONST} ${AppUtil.FIREBASE_AUTH_TOKEN}"
    ): Response<ProjectApiResponseModel>

    @GET("activity_sequence/getActivitySequencesAndroid/{userID}/{userRole}")
   suspend fun workTypeSequenceApi(
        @Path("userID") userId: Int?,
        @Path("userRole") userRole: String?,
        @Header("Authorization") authHeader: String = "${AppUtil.BEARER_STRING_CONST} ${AppUtil.FIREBASE_AUTH_TOKEN}"
    ): Response<WorkTypeResponseModel>
}
