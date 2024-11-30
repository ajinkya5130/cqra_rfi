package com.ob.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ob.database.dao.CheckListDao
import com.ob.database.dao.ClientDao
import com.ob.database.dao.GroupListDao
import com.ob.database.dao.ProjectDao
import com.ob.database.dao.StageDao
import com.ob.database.dao.StructureDao
import com.ob.database.dao.SubUnitDao
import com.ob.database.dao.UnitDao
import com.ob.database.dao.WorkTypeDao
import com.ob.database.db_tables.ChecklistTableModel
import com.ob.database.db_tables.ClientTableModel
import com.ob.database.db_tables.GroupListTableModel
import com.ob.database.db_tables.ProjectTableModel
import com.ob.database.db_tables.StageTableModel
import com.ob.database.db_tables.StructureTableModel
import com.ob.database.db_tables.SubUnitTableModel
import com.ob.database.db_tables.UnitTableModel
import com.ob.database.db_tables.WorkTypeTableModel
import java.util.concurrent.Executors


@Database(entities = [
    ClientTableModel::class,
    ProjectTableModel::class,
    WorkTypeTableModel::class,
    StructureTableModel::class,
    ChecklistTableModel::class,
    GroupListTableModel::class,
    StageTableModel::class,
    UnitTableModel::class,
    SubUnitTableModel::class,
                     ],
    version = 1,
    exportSchema = true/*,
    autoMigrations = [
        AutoMigration (from = 13, to = 14)
    ]*/)
abstract class RFIRoomDb : RoomDatabase() {
    abstract fun clientDao(): ClientDao
    abstract fun projectDao(): ProjectDao
    abstract fun workTypeDao(): WorkTypeDao
    abstract fun checkListDao(): CheckListDao
    abstract fun groupListDao(): GroupListDao
    abstract fun structureDao(): StructureDao
    abstract fun stageDao(): StageDao
    abstract fun unitDao(): UnitDao
    abstract fun subUnitDao(): SubUnitDao

    companion object {
        const val TAG = "RFIRoomDb"
        @Volatile
        private var INSTANCE: RFIRoomDb? = null

        fun getDatabase(context: Context): RFIRoomDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, RFIRoomDb::class.java, "RFI.db")
                    .openHelperFactory(ExistingDatabaseOpenHelperFactory(context, "RFI.db"))
                    .setQueryCallback({
                            sqlQuery, bindArgs ->
                        Log.i(TAG, "SQL Query: $sqlQuery SQL Args: $bindArgs")
                    },Executors.newSingleThreadExecutor())
                    //.addMigrations(MIGRATION_13_14)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

/*val MIGRATION_13_14 = object : Migration(14, 15) {
    override fun migrate(db: SupportSQLiteDatabase) {
        Log.d(TAG, "migrate: version- ${db.version}, Path - ${db.path}")
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `WorkTypeSeq` (`workTypeId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `activitySequenceGroupId` INTEGER NOT NULL, `projectId` INTEGER NOT NULL, `activitySequenceLevel` INTEGER NOT NULL," +
                    " `activitySequenceStatus` INTEGER NOT NULL, `activitySequenceName` TEXT NOT NULL)"
        )
    }
}*/