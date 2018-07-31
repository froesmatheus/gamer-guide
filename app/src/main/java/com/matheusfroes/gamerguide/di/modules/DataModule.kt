package com.matheusfroes.gamerguide.di.modules

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.matheusfroes.gamerguide.data.GamerGuideDatabase
import com.matheusfroes.gamerguide.data.model.Platform
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun database(context: Context): GamerGuideDatabase {
        return Room
                .databaseBuilder(context, GamerGuideDatabase::class.java, "gamerguide.db")
                .allowMainThreadQueries()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Executors.newSingleThreadScheduledExecutor().execute {
                            insertPlataforms(db)
                            insertGameLists(db)
                        }
                    }
                })
                .build()
    }

    private fun insertPlataforms(db: SupportSQLiteDatabase) {
        db.beginTransaction()
        val platforms = Platform.getPlatforms()
        try {
            val cv = ContentValues()

            platforms.forEach { platform ->
                cv.put("id", platform.id)
                cv.put("name", platform.name.trim())

                db.insert("platforms", SQLiteDatabase.CONFLICT_REPLACE, cv)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    private fun insertGameLists(db: SupportSQLiteDatabase) {
        val cv = ContentValues()

        cv.put("name", "Lista de compras")

        db.insert("lists", SQLiteDatabase.CONFLICT_REPLACE, cv)
    }
}