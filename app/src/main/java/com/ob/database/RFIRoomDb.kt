package com.ob.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ob.database.dao.ClientDao
import com.ob.database.dao.ProjectDao
import com.ob.database.dao.StageDao
import com.ob.database.dao.StructureDao
import com.ob.database.dao.WorkTypeDao
import com.ob.database.db_tables.ClientTableModel
import com.ob.database.db_tables.ProjectTableModel
import com.ob.database.db_tables.StageTableModel
import com.ob.database.db_tables.StructureTableModel
import com.ob.database.db_tables.WorkTypeTableModel


@Database(entities = [ClientTableModel::class,
    ProjectTableModel::class,
    WorkTypeTableModel::class,
    StructureTableModel::class,
    StageTableModel::class,
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
    abstract fun structureDao(): StructureDao
    abstract fun stageDao(): StageDao

    companion object {
        @Volatile
        private var INSTANCE: RFIRoomDb? = null

        fun getDatabase(context: Context): RFIRoomDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, RFIRoomDb::class.java, "RFI.db")
                    .openHelperFactory(ExistingDatabaseOpenHelperFactory(context, "RFI.db"))
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

private const val TAG = "RFIRoomDb"