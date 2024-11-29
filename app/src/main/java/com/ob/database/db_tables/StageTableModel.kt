package com.ob.database.db_tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "floor")
data class StageTableModel (
    @PrimaryKey(autoGenerate = true)
    var pk_floor_id: Int = 0,
    var floor_Id: String = "",
    var floor_Name: String ="",
    var Floor_Scheme_ID: String="",
    var FK_Bldg_ID: String="",
    var FK_WorkTyp_ID: String="",
    var user_id: String="",
)
