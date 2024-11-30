package com.ob.database.db_tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Unit")
data class UnitTableModel (
    @PrimaryKey(autoGenerate = true)
    var pk_ubit_id: Int = 0,
    var Unit_ID: String = "",
    @ColumnInfo(name = "Unit_Des")
    var Unit_Name: String ="",
    var Unit_Scheme_id: String="",
    var Fk_Floor_ID: String="",
    var FK_WorkTyp_ID: String="",
    var user_id: String="",
)
