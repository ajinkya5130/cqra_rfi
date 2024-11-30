package com.ob.database.db_tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SubUnit")
data class SubUnitTableModel (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("pk_subunit_id")
    var pkSubunitId: Int = 0,
    @ColumnInfo("Sub_Unit_ID")
    var subUnitId: String = "",
    @ColumnInfo(name = "Sub_Unit_Des")
    var subUnitName: String ="",
    @ColumnInfo("Sub_Unit_Scheme_id")
    var subUnitSchemeId: String ="",
    @ColumnInfo("FK_Unit_ID")
    var fkUnitId: String ="",
    @ColumnInfo("FK_WorkTyp_ID")
    var fkWorkTypId: String ="",
    @ColumnInfo("user_id")
    var userId: String="",
)
