package com.matheusfroes.gamerguide.data.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.matheusfroes.gamerguide.data.model.GameList

@Dao
interface ListasDAOr {

    @Insert
    fun insert(list: GameList)

    @Update
    fun update(list: GameList)

    @Query("SELECT COUNT(*) FROM lists WHERE name = :listName")
    fun listAlreadyAdded(listName: String): Int

    @Query("DELETE FROM lists WHERE id = :listId")
    fun delete(listId: Int)

    @Query("SELECT * FROM lists WHERE id = :listId")
    fun get(listId: Int): GameList

    @Query("SELECT * FROM lists")
    fun getAll(): List<GameList>
}