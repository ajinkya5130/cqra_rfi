package com.ob.database.db_tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LoginUser")
data class LoginUserTableModel (
    @PrimaryKey
    var pkLoginUserId: Int = 0,
    var loginUserName: String = "",
    var loginUserPassword: String = "",
    var loginUserRole: String = "",
    var dashboardRole: Boolean =false,
)