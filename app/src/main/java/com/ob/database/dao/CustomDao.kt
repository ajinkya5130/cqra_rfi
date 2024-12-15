package com.ob.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

@Dao
interface CustomDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: T):Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(data: T):Int

    @Insert
    suspend fun insertAll(data: List<T>)

}