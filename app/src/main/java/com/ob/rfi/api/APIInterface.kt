package com.ob.rfi.api

import com.ob.AppUtil
import com.ob.rfi.models.ClientApiResponseModel
import com.ob.rfi.models.ProjectApiResponseModel
import com.ob.rfi.models.StructureResponseModel
import com.ob.rfi.models.WorkTypeResponseModel
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface APIInterface {

    @GET("api/clients_rfi/clientsRfiAndroid/{userID}/{userRole}")
   suspend fun getClientsAPI(
        @Path("userID") userId: Int,
        @Path("userRole") userRole: String,
        @Header("Authorization") authHeader: String = "${AppUtil.BEARER_STRING_CONST} ${AppUtil.FIREBASE_AUTH_TOKEN}"
    ): Response<ClientApiResponseModel>

    @GET("api/projects_rfi/getProjectsByUserIdAndRoleAndroid/{userID}/{userRole}")
   suspend fun getProjectApi(
        @Path("userID") userId: Int,
        @Path("userRole") userRole: String,
        @Header("Authorization") authHeader: String = "${AppUtil.BEARER_STRING_CONST} ${AppUtil.FIREBASE_AUTH_TOKEN}"
    ): Response<ProjectApiResponseModel>

    @GET("api/activity_sequence/getActivitySequencesAndroid/{userID}/{userRole}")
   suspend fun workTypeSequenceApi(
        @Path("userID") userId: Int,
        @Path("userRole") userRole: String,
        @Header("Authorization") authHeader: String = "${AppUtil.BEARER_STRING_CONST} ${AppUtil.FIREBASE_AUTH_TOKEN}"
    ): Response<WorkTypeResponseModel>

    @GET("ActivityChecklistSeqRfi/getChecklistsWithoutNodeIdAndroid/{userID}/{userRole}/{clientId}/{projectId}/{workSeqType}")
   suspend fun getCheckListApi(
        @Path("userID") userId: Int,
        @Path("userRole") userRole: String,
        @Path("clientId") clientId: String,
        @Path("projectId") projectId: String,
        @Path("workSeqType") workTypeSeq: String,
        @Header("Authorization") authHeader: String = "${AppUtil.BEARER_STRING_CONST} ${AppUtil.FIREBASE_AUTH_TOKEN}"
    ): Response<ResponseBody>

    @GET("GroupRfiController/getGroupsWithoutUnitIdAndroid/{userID}/{userRole}/{clientId}/{projectId}/{checklistRfiId}")
   suspend fun getGroupListApi(
        @Path("userID") userId: Int,
        @Path("userRole") userRole: String,
        @Path("clientId") clientId: String,
        @Path("projectId") projectId: String,
        @Path("checklistRfiId") checklistRfiId: String,
        @Header("Authorization") authHeader: String = "${AppUtil.BEARER_STRING_CONST} ${AppUtil.FIREBASE_AUTH_TOKEN}"
    ): Response<ResponseBody>

    @GET("StructureRfi/getStructuresWithCheclistId/{userID}/{userRole}/{clientId}/{projectId}/{workSeqType}/{checkListId}")
   suspend fun getStructureApi(
        @Path("userID") userId: Int,
        @Path("userRole") userRole: String,
        @Path("clientId") clientId: String,
        @Path("projectId") projectId: String,
        @Path("workSeqType") workTypeSeq: String,
        @Path("checkListId") checkListId: String,
        @Header("Authorization") authHeader: String = "${AppUtil.BEARER_STRING_CONST} ${AppUtil.FIREBASE_AUTH_TOKEN}"
    ): Response<StructureResponseModel>

    @GET("stage/getStagesWithChecklistId/{userID}/{userRole}/{clientId}/{projectId}/{workSeqType}/{checkListId}/{structureId}")
   suspend fun getStageApi(
        @Path("userID") userId: Int,
        @Path("userRole") userRole: String,
        @Path("clientId") clientId: String,
        @Path("projectId") projectId: String,
        @Path("workSeqType") workTypeSeq: String,
        @Path("checkListId") checkListId: String,
        @Path("structureId") structureId: String,
        @Header("Authorization") authHeader: String = "${AppUtil.BEARER_STRING_CONST} ${AppUtil.FIREBASE_AUTH_TOKEN}"
    ): Response<ResponseBody>
}
