package com.matheusfroes.gamerguide.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "platforms")
data class Platform(
        @PrimaryKey
        val id: Long,
        val name: String) : Serializable {

    override fun toString() = name
}