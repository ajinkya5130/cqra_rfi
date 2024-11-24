package com.ob.database.db_tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Client")
data class ClientTableModel (

    @PrimaryKey(autoGenerate = true)
    val _id: Int = 0,
    val Client_ID: String,
    val Clnt_Name: String,
    val CL_Dispaly_Name: String,
    val Clnt_Adrs: String,
    val user_id: String,

)