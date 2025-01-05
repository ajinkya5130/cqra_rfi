package com.ob.database.db_tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NodeUserDetails")
data class NodeUserTableModel (
    @PrimaryKey(autoGenerate = true)
    var nodePKId: Int = 0,
    var checkerRepId: Int = 0,
    var checkerUserType: Int = 0,

    var approverRepId: Int = 0,
    var approverUserType: Int = 0,

    var checker2RepId: Int = 0,
    var checker2UserType: Int = 0,

    var checker3RepId: Int = 0,
    var checker3UserType: Int = 0,

    var makerRepId: Int = 0,
    var makerUserType: Int = 0,

    var contractorId: Int = 0,

)