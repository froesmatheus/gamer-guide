package com.matheusfroes.gamerguide.data.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.matheusfroes.gamerguide.data.model.Game

@Dao
interface JogosDAOr {

    @Insert
    fun insert(game: Game)

    @Update
    fun update(game: Game)

    @Query("DELETE FROM games WHERE id = :gameId")
    fun delete(gameId: Long)

    @Query("SELECT * FROM games WHERE id = :gameId")
    fun get(gameId: Long): Game

    @Query("SELECT * FROM games WHERE game_progress_beaten = :beaten")
    fun getGameByProgressStatus(beaten: Boolean): List<Game>

    @Query("SELECT * FROM games")
    fun getAll(): List<Game>

    @Query("SELECT COUNT(*) FROM games WHERE game_progress_beaten = :beaten")
    fun getGameCountByProgressStatus(beaten: Boolean): Int

    @Query("SELECT SUM(game_progress_hoursPlayed) FROM games")
    fun getTotalHoursPlayed(): Int
}
