package com.ob.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.ob.database.db_tables.LoginUserTableModel

@Dao
interface LoginUserDao: CustomDao<LoginUserTableModel> {
    @Query("SELECT * FROM LoginUser where loginUserName =:userName")
    suspend fun getLoginUserData(userName:String): LoginUserTableModel?
    @Query("SELECT * FROM LoginUser where pkLoginUserId =:userId")
    suspend fun getLoginUserDataUsingId(userId:Int): LoginUserTableModel?

}