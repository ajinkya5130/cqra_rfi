package com.ob.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ob.database.db_tables.ClientTableModel


@Database(entities = [ClientTableModel::class/*, ProjectModel::class, WorkTypeModel::class, StructureModel::class, StageModel::class, UnitModel::class, SubUnitModel::class*/], version = 13)
abstract class RFIRoomDb : RoomDatabase() {
    abstract fun myRfiDao(): RFIDao

    companion object {
        @Volatile
        private var INSTANCE: RFIRoomDb? = null

        fun getDatabase(context: Context): RFIRoomDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, RFIRoomDb::class.java, "RFI.db")
                    .openHelperFactory(ExistingDatabaseOpenHelperFactory(context, "RFI.db"))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}