package com.ob.database.db_tables


data class ProjectAllocateTaskModel (
    var schemaOrProjectName: String? = null,
    var schemaOrProjectId: Int? =null,
    var scrollingStatus: String?=null
)
