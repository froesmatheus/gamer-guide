package com.matheusfroes.gamerguide.data.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.matheusfroes.gamerguide.data.model.Video

@Dao
interface VideosDAOr {

    fun inserir(videos: List<Video>, jogoId: Long)

    fun getVideosPorJogo(idJogo: Long): List<Video> {
        val cursor = db.rawQuery("""
            SELECT *
            FROM ${Helper.TABELA_VIDEOS}
            WHERE ${Helper.VIDEOS_ID_JOGO} = ?""", arrayOf(idJogo.toString()))

        val videos = mutableListOf<Video>()
        if (cursor.count > 0) {
            cursor.moveToFirst()

            do {
                val video = Video(
                        name = cursor.getString(cursor.getColumnIndex(Helper.VIDEOS_NOME)),
                        videoId = cursor.getString(cursor.getColumnIndex(Helper.VIDEOS_VIDEOID)))

                videos.add(video)
            } while (cursor.moveToNext())

        }

        cursor.close()

        return videos
    }
}