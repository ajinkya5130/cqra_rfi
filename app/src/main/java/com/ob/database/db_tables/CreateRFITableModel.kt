package com.ob.database.db_tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Created_RFI")
data class CreateRFITableModel (
    @PrimaryKey(autoGenerate = true)
    var _id: Int = 0,
    var FK_rfi_Id: String = "",
    var user_id: String ="",
    var coverageText: String="",
)