package com.matheusfroes.gamerguide.di.modules

import android.arch.persistence.room.Room
import android.content.Context
import com.matheusfroes.gamerguide.data.GamerGuideDatabase
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    fun database(context: Context): GamerGuideDatabase {
        return Room
                .databaseBuilder(context, GamerGuideDatabase::class.java, "gamerguide.db")
                .allowMainThreadQueries()
                .build()
    }

}