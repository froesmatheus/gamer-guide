package com.matheusfroes.gamerguide.data.source.local

import com.matheusfroes.gamerguide.SingleLiveEvent
import com.matheusfroes.gamerguide.data.GamerGuideDatabase
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.data.model.GameList
import com.matheusfroes.gamerguide.data.model.GameWithGameList
import io.reactivex.Flowable
import javax.inject.Inject

class GameListLocalSource @Inject constructor(private val database: GamerGuideDatabase) {
    val gamelists: SingleLiveEvent<List<GameList>> = SingleLiveEvent()


    fun getLists(): Flowable<List<GameList>> {
        return database.listsDao()
                .getAll()
    }

    fun getList(gameListId: Long): GameList {
        return database.listsDao().get(gameListId)
    }

    fun delete(gameListId: Long) {
        database.listsDao().delete(gameListId)
    }

    fun deleteGameFromLists(gameId: Long) {
        database.listsDao().deleteGameFromLists(gameId)
    }

    fun deleteGameFromList(gameId: Long, listId: Long) {
        database.listsDao().deleteGameFromList(gameId, listId)
    }

    fun getGamesByList(gameListId: Long): Flowable<List<Game>> {
        return database.gamesDao()
                .getGamesByList(gameListId)
    }

    fun gamesByList(gameListId: Long): List<Game> {
        return database.gamesDao()
                .gamesByList(gameListId)
    }

    fun addList(gameList: GameList) {
        database.listsDao().insert(gameList)
    }

    fun addGameToList(gameId: Long, listId: Long) {
        database.listsDao().addGameToList(GameWithGameList(gameId, listId))
    }

    fun update(gameList: GameList) {
        database.listsDao().update(gameList)
    }

    fun listContainsGame(gameId: Long, listId: Long): Boolean {
        return database.listsDao().listContainsGame(gameId, listId) > 0
    }

    fun gameIsInGameLists(gameId: Long): Boolean {
        return database.listsDao().gameIsInGameLists(gameId) > 0
    }

    fun listAlreadyAdded(listName: String): Boolean {
        return database.listsDao().listAlreadyAdded(listName) > 0
    }
}