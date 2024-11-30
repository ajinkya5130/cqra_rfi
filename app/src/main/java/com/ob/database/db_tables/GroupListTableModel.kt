package com.ob.database.db_tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Group1")
data class GroupListTableModel (
    @PrimaryKey(autoGenerate = true)
    var pk_group_id: Int = 0,
    var Grp_ID: String = "",
    var Grp_Name: String ="",
    var Node_id: String="",
    var FK_Checklist_ID: String="",
    var user_id: String="",
    var GRP_Sequence_tint: String="",
)
