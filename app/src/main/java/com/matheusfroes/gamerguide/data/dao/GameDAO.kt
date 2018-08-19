package com.matheusfroes.gamerguide.data.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.data.model.InsertType
import io.reactivex.Flowable

@Dao
interface GameDAO {

    @Insert
    fun insert(game: Game)

    @Update
    fun update(game: Game)

    @Query("DELETE FROM games WHERE id = :gameId")
    fun delete(gameId: Long)

    @Query("SELECT * FROM games WHERE id = :gameId")
    fun get(gameId: Long): Game?

    @Query("SELECT * FROM games WHERE game_progress_beaten = :beaten")
    fun getGameByProgressStatus(beaten: Boolean): List<Game>

    @Query("SELECT * FROM games")
    fun getAll(): List<Game>

    @Query("SELECT COUNT(*) FROM games WHERE game_progress_beaten = :beaten")
    fun getGameCountByProgressStatus(beaten: Boolean): Int

    @Query("SELECT genres FROM games WHERE insertType <> 'INSERT_TO_LIST'")
    fun getGenres(): List<String>

    @Query("SELECT SUM(game_progress_hoursPlayed) FROM games")
    fun getTotalHoursPlayed(): Int

    @Query("""
        SELECT g.* FROM games g
        INNER JOIN games_lists gl ON g.id = gl.gameId
        WHERE gl.gameListId = :listId""")
    fun getGamesByList(listId: Long): Flowable<List<Game>>

    @Query("""
        SELECT g.* FROM games g
        INNER JOIN games_lists gl ON g.id = gl.gameId
        WHERE gl.gameListId = :listId""")
    fun gamesByList(listId: Long): List<Game>

    @Query("SELECT COUNT(*) FROM games WHERE game_progress_beaten = :beaten AND insertType <> 'INSERT_TO_LIST'")
    fun getGameCount(beaten: Boolean): Int

    @Query("SELECT * FROM games WHERE id = :gameId AND insertType = :insertType")
    fun getGamesByInsertType(gameId: Long, insertType: InsertType): Game?

    @Query("SELECT * FROM games WHERE game_progress_beaten = 0 AND insertType = 'INSERT_BY_SEARCH'")
    fun getUnfinishedGames(): Flowable<List<Game>>

    @Query("SELECT * FROM games WHERE game_progress_beaten = 1")
    fun getBeatenGames(): Flowable<List<Game>>

    @Query("SELECT COUNT(*) from games WHERE id = :gameId")
    fun isGameAdded(gameId: Long): Boolean
}
