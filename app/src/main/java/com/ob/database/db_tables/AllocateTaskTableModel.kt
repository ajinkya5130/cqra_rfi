package com.ob.database.db_tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "allocateTask")
data class AllocateTaskTableModel (
    @PrimaryKey(autoGenerate = true)
    var _id: Int = 0,
    var clientId: Int = 0,
    var projectId: Int = 0,
    var workTypeId: Int = 0,
    var structureId: Int = 0,
    var stageId: Int = 0,
    var unitId: Int = 0,
    var subUnitId: String = "",
    var checkListId: Int = 0,
    var groupId: Int = 0,
    var activitySequenceId: Int = 0,
    var userId: Int=0,
)
