package com.matheusfroes.gamerguide.di.modules

import android.arch.persistence.room.Room
import android.content.Context
import com.matheusfroes.gamerguide.data.GamerGuideDatabase
import com.matheusfroes.gamerguide.di.annotation.AppContext
import com.matheusfroes.gamerguide.di.annotation.AppScope
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    @AppScope
    fun database(@AppContext context: Context): GamerGuideDatabase {
        return Room
                .databaseBuilder(context, GamerGuideDatabase::class.java, "gamerguide.db")
                .allowMainThreadQueries()
                .build()
    }

}