package com.matheusfroes.gamerguide.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "lists")
data class GameList(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        var name: String) {

    @Ignore
    val games: List<Game> = listOf()

    override fun toString() = name
}