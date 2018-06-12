package com.matheusfroes.gamerguide.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.matheusfroes.gamerguide.data.model.Video

class VideosDAO(context: Context) {
    private val db: SQLiteDatabase = Helper(context).writableDatabase

    fun inserir(videos: List<Video>, jogoId: Long) {
        db.beginTransaction()
        try {
            val cv = ContentValues()

            videos.forEach { video ->
                cv.put(Helper.VIDEOS_ID_JOGO, jogoId)
                cv.put(Helper.VIDEOS_NOME, video.name)
                cv.put(Helper.VIDEOS_VIDEOID, video.videoId)

                db.insert(Helper.TABELA_VIDEOS, null, cv)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

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