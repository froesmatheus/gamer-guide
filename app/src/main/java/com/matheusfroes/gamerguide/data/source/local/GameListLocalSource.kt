package com.matheusfroes.gamerguide.data.source.local

import com.matheusfroes.gamerguide.data.GamerGuideDatabase
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.data.model.GameList
import io.reactivex.Flowable
import javax.inject.Inject

class GameListLocalSource @Inject constructor(private val database: GamerGuideDatabase) {

    fun getLists(): Flowable<List<GameList>> {
        return database.listsDao().getAll()
    }

    fun getList(gameListId: Long): GameList {
        return database.listsDao().get(gameListId)
    }

    fun delete(gameListId: Long) {
        database.listsDao().delete(gameListId)
    }

    fun getGamesByList(gameListId: Long): Flowable<List<Game>> {
        return database.gamesDao().getGamesByList(gameListId)
    }

    fun addList(gameList: GameList) {
        database.listsDao().insert(gameList)
    }

    fun update(gameList: GameList) {
        database.listsDao().update(gameList)
    }

    fun listAlreadyAdded(listName: String): Boolean {
        return database.listsDao().listAlreadyAdded(listName) > 0
    }
}