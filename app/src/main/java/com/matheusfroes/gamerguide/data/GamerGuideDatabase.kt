package com.matheusfroes.gamerguide.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.matheusfroes.gamerguide.data.converters.DateConverter
import com.matheusfroes.gamerguide.data.converters.InsertTypeEnumConverter
import com.matheusfroes.gamerguide.data.dao.*
import com.matheusfroes.gamerguide.data.model.*


@Database(entities = [Game::class, GameList::class, GameWithGameList::class, GameWithPlatform::class, NewsSource::class,
    Platform::class, Video::class, News::class], version = 1)
@TypeConverters(DateConverter::class, InsertTypeEnumConverter::class)
abstract class GamerGuideDatabase : RoomDatabase() {
    abstract fun platformsDao(): PlatformDAO
    abstract fun listsDao(): GameListDAO
    abstract fun videosDao(): VideoDAO
    abstract fun gamesDao(): GameDAO
    abstract fun newsDao(): NewsDAO
}