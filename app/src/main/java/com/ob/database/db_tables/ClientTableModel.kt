package com.ob.database.db_tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Client")
data class ClientTableModel (
    @PrimaryKey(autoGenerate = true)
    var pk_client_id: Int = 0,
    var Client_ID: String = "",
    var Clnt_Name: String ="",
    var CL_Dispaly_Name: String="",
    var Clnt_Adrs: String="",
    var user_id: String=""
)