package com.ob.database.db_tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Scheme")
data class ProjectTableModel (
    @PrimaryKey(autoGenerate = true)
    var scheme_id: Int = 0,
    var PK_Scheme_ID: String = "",
    var Scheme_Name: String ="",
    var Scheme_Cl_Id: String="",
    var Scheme_Diplay_Name: String="",
    var Scheme_Adrs: String="",
    var Scheme_Region: String="",
    var scrolling_status: String="true",
    var user_id: String="",
)