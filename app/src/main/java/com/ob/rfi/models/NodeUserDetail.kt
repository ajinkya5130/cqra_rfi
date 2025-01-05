package com.ob.rfi.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NodeUserDetail(
    @SerialName("node_user_approver_repr_id")
    var nodeUserApproverReprId: Int = 0,
    @SerialName("node_user_approver_user_type")
    var nodeUserApproverUserType: Int = 0,
    @SerialName("node_user_checker2_repr_id")
    var nodeUserChecker2ReprId: Int = 0,
    @SerialName("node_user_checker2_user_type")
    var nodeUserChecker2UserType: Int = 0,
    @SerialName("node_user_checker3_repr_id")
    var nodeUserChecker3ReprId: Int = 0,
    @SerialName("node_user_checker3_user_type")
    var nodeUserChecker3UserType: Int = 0,
    @SerialName("node_user_checker_repr_id")
    var nodeUserCheckerReprId: Int = 0,
    @SerialName("node_user_checker_user_type")
    var nodeUserCheckerUserType: Int = 0,
    @SerialName("node_user_contractor_id")
    var nodeUserContractorId: Int = 0,
    @SerialName("node_user_mkr_repr_id")
    var nodeUserMkrReprId: Int = 0,
    @SerialName("node_user_mkr_user_type")
    var nodeUserMkrUserType: Int = 0
)