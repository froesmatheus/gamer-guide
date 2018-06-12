package com.matheusfroes.gamerguide.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.matheusfroes.gamerguide.data.model.NewsSource

class FonteNoticiasDAO(private val context: Context) {
    private val db: SQLiteDatabase = Helper(context).writableDatabase
    private val jogosDAO: JogosDAO by lazy {
        JogosDAO(context)
    }

    fun obterFonteNoticias(): List<NewsSource> {
        val cursor = db.rawQuery(
                """SELECT *
                    FROM ${Helper.TABELA_FONTE_NOTICIAS}""", null)

        val fontesNoticias = mutableListOf<NewsSource>()

        if (cursor.count > 0) {
            cursor.moveToFirst()

            do {
                val fonteNoticia = NewsSource(
                        id = cursor.getInt(cursor.getColumnIndex(Helper.FONTE_NOTICIAS_ID)),
                        name = cursor.getString(cursor.getColumnIndex(Helper.FONTE_NOTICIAS_NOME)),
                        website = cursor.getString(cursor.getColumnIndex(Helper.FONTE_NOTICIAS_WEBSITE)),
                        enabled = cursor.getInt(cursor.getColumnIndex(Helper.FONTE_NOTICIAS_ATIVADO)) == 1
                )
                fontesNoticias.add(fonteNoticia)
            } while (cursor.moveToNext())
        }

        cursor.close()

        return fontesNoticias
    }

    fun obterFonteNoticias(ativos: Boolean): List<NewsSource> {
        val cursor = db.rawQuery(
                """SELECT *
                    FROM ${Helper.TABELA_FONTE_NOTICIAS}
                    WHERE ${Helper.FONTE_NOTICIAS_ATIVADO} = ?""", arrayOf((if (ativos) 1 else 0).toString()))

        val fontesNoticias = mutableListOf<NewsSource>()

        if (cursor.count > 0) {
            cursor.moveToFirst()

            do {
                val fonteNoticia = NewsSource(
                        id = cursor.getInt(cursor.getColumnIndex(Helper.FONTE_NOTICIAS_ID)),
                        name = cursor.getString(cursor.getColumnIndex(Helper.FONTE_NOTICIAS_NOME)),
                        website = cursor.getString(cursor.getColumnIndex(Helper.FONTE_NOTICIAS_WEBSITE)),
                        enabled = cursor.getInt(cursor.getColumnIndex(Helper.FONTE_NOTICIAS_ATIVADO)) == 1
                )
                fontesNoticias.add(fonteNoticia)
            } while (cursor.moveToNext())
        }

        cursor.close()

        return fontesNoticias
    }

    fun alterarStatusFonteNoticia(fonteId: Int, ativo: Boolean) {
        val cv = ContentValues()

        cv.put(Helper.FONTE_NOTICIAS_ATIVADO, if (ativo) 1 else 0)

        db.update(Helper.TABELA_FONTE_NOTICIAS, cv, "_id = ?", arrayOf(fonteId.toString()))
    }
}