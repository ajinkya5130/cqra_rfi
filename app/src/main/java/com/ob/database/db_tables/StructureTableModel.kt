package com.ob.database.db_tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Building")
data class StructureTableModel (
    @PrimaryKey(autoGenerate = true)
    var pk_building_id: Int = 0,
    var Bldg_ID: String = "",
    var Bldg_Name: String ="",
    var Build_scheme_id: String="",
    var FK_WorkTyp_ID: String="",
    var user_id: String="",
)
