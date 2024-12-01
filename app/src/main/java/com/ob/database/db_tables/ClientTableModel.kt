package com.ob.database.db_tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Client")
data class ClientTableModel (
    @PrimaryKey(autoGenerate = true)
    var pkClientId: Int = 0,
    var clientId: Int = 0,
    var clientName: String ="",
    var clientDisplay: String="",
    var clientAddress: String="",
    var userId: Int=0
)