package com.ob.database.db_tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CheckList")
data class ChecklistTableModel (
    @PrimaryKey(autoGenerate = true)
    var pk_checklist_id: Int = 0,
    var Checklist_ID: String = "",
    var Checklist_Name: String ="",
    var Node_Id: String="",
    var FK_WorkTyp_ID: String="",
    var user_id: String="",
)
