package com.ob.database.db_tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "WorkTypeSeq")
data class WorkTypeTableModel (
    @PrimaryKey(autoGenerate = true)
    var workTypeId: Int = 0,
    var activitySequenceGroupId: Int = 0,
    var projectId: Int = 0,
    var activitySequenceLevel: Int = 0,
    var activitySequenceStatus: Int = 0,
    var activitySequenceName: String=""
)