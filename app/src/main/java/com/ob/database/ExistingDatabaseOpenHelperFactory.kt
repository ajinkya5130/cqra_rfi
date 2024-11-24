package com.ob.database

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory

class ExistingDatabaseOpenHelperFactory(private val context: Context, private val databaseName: String) :
    SupportSQLiteOpenHelper.Factory {
    override fun create(configuration: SupportSQLiteOpenHelper.Configuration): SupportSQLiteOpenHelper {
        val databaseFile = context.getDatabasePath(databaseName)
        return FrameworkSQLiteOpenHelperFactory().create(
            SupportSQLiteOpenHelper.Configuration.builder(context)
                .name(databaseFile.absolutePath)
                .callback(object : SupportSQLiteOpenHelper.Callback(configuration.callback.version) {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        // No need to create the database, it already exists
                    }

                    override fun onUpgrade(db: SupportSQLiteDatabase, oldVersion: Int, newVersion: Int) {
                        // Handle database upgrades if needed
                    }
                })
                .build()
        )
    }
}