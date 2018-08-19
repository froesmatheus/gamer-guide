package com.matheusfroes.gamerguide.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.matheusfroes.gamerguide.data.models.FonteNoticia

class FonteNoticiasDAO(context: Context) {
    private val db: SQLiteDatabase = Helper(context).writableDatabase

    fun obterFonteNoticias(): List<FonteNoticia> {
        val cursor = db.rawQuery(
                """SELECT *
                    FROM ${Helper.TABELA_FONTE_NOTICIAS}""", null)

        val fontesNoticias = mutableListOf<FonteNoticia>()

        if (cursor.count > 0) {
            cursor.moveToFirst()

            do {
                val fonteNoticia = FonteNoticia(
                        id = cursor.getInt(cursor.getColumnIndex(Helper.FONTE_NOTICIAS_ID)),
                        nome = cursor.getString(cursor.getColumnIndex(Helper.FONTE_NOTICIAS_NOME)),
                        website = cursor.getString(cursor.getColumnIndex(Helper.FONTE_NOTICIAS_WEBSITE)),
                        ativado = cursor.getInt(cursor.getColumnIndex(Helper.FONTE_NOTICIAS_ATIVADO)) == 1
                )
                fontesNoticias.add(fonteNoticia)
            } while (cursor.moveToNext())
        }

        cursor.close()

        return fontesNoticias
    }

    fun obterFonteNoticias(ativos: Boolean): List<FonteNoticia> {
        val cursor = db.rawQuery(
                """SELECT *
                    FROM ${Helper.TABELA_FONTE_NOTICIAS}
                    WHERE ${Helper.FONTE_NOTICIAS_ATIVADO} = ?""", arrayOf((if (ativos) 1 else 0).toString()))

        val fontesNoticias = mutableListOf<FonteNoticia>()

        if (cursor.count > 0) {
            cursor.moveToFirst()

            do {
                val fonteNoticia = FonteNoticia(
                        id = cursor.getInt(cursor.getColumnIndex(Helper.FONTE_NOTICIAS_ID)),
                        nome = cursor.getString(cursor.getColumnIndex(Helper.FONTE_NOTICIAS_NOME)),
                        website = cursor.getString(cursor.getColumnIndex(Helper.FONTE_NOTICIAS_WEBSITE)),
                        ativado = cursor.getInt(cursor.getColumnIndex(Helper.FONTE_NOTICIAS_ATIVADO)) == 1
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