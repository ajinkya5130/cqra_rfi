package com.ob.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface CustomDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: T)

    @Insert
    suspend fun insertAll(data: List<T>)

}