package com.matheusfroes.gamerguide.data.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.matheusfroes.gamerguide.data.model.GameList
import com.matheusfroes.gamerguide.data.model.GameWithGameList
import io.reactivex.Flowable

@Dao
interface GameListDAO {

    @Insert
    fun insert(list: GameList)

    @Insert
    fun addGameToList(gameToList: GameWithGameList)

    @Update
    fun update(list: GameList)

    @Query("SELECT COUNT(*) FROM lists WHERE name = :listName")
    fun listAlreadyAdded(listName: String): Int

    @Query("DELETE FROM games_lists WHERE gameId = :gameId")
    fun deleteGameFromLists(gameId: Long)

    @Query("DELETE FROM lists WHERE id = :listId")
    fun delete(listId: Long)

    @Query("SELECT * FROM lists WHERE id = :listId")
    fun get(listId: Long): GameList

    @Query("SELECT * FROM lists")
    fun getAll(): Flowable<List<GameList>>

    @Insert
    fun insertGameIntoList(gameWithGameList: GameWithGameList)

    @Query("DELETE FROM games_lists WHERE gameId = :gameId AND gameListId = :gameListId")
    fun deleteGameFromList(gameId: Long, gameListId: Long)

    @Query("SELECT COUNT(*) FROM games_lists WHERE gameId = :gameId AND gameListId = :gameListId")
    fun listContainsGame(gameId: Long, gameListId: Long): Int
}