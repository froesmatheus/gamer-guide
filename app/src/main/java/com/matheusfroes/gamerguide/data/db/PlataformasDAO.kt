package com.matheusfroes.gamerguide.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.matheusfroes.gamerguide.data.model.Platform

class PlataformasDAO(context: Context) {
    private val db: SQLiteDatabase = Helper(context).writableDatabase

    fun obterPlataforma(id: Long): Platform {
        val cursor = db.rawQuery("SELECT * FROM ${Helper.TABELA_PLATAFORMAS} WHERE ${Helper.PLATAFORMAS_ID} = ?", arrayOf(id.toString()))

        var plataforma: Platform? = null

        if (cursor.count > 0) {
            cursor.moveToFirst()

            plataforma = Platform(
                    id = cursor.getLong(cursor.getColumnIndex(Helper.PLATAFORMAS_ID)),
                    name = cursor.getString(cursor.getColumnIndex(Helper.PLATAFORMAS_NOME))
            )
        }

        cursor.close()

        return plataforma!!
    }

    fun obterPlataformas(): List<Platform> {
        val cursor = db.rawQuery("SELECT * FROM ${Helper.TABELA_PLATAFORMAS}", null)

        val plataformas = mutableListOf<Platform>()
        if (cursor.count > 0) {
            cursor.moveToFirst()

            do {
                val plataforma = Platform(
                        id = cursor.getLong(cursor.getColumnIndex(Helper.PLATAFORMAS_ID)),
                        name = cursor.getString(cursor.getColumnIndex(Helper.PLATAFORMAS_NOME)))

                plataformas.add(plataforma)
            } while (cursor.moveToNext())

        }

        cursor.close()

        return plataformas
    }

    fun obterPlataformasPorJogo(id: Long): List<Platform> {
        val cursor = db.rawQuery("""
            SELECT P.${Helper.PLATAFORMAS_ID}, P.${Helper.PLATAFORMAS_NOME}
            FROM ${Helper.TABELA_PLATAFORMAS} P
            INNER JOIN ${Helper.TABELA_JOGOS_PLATAFORMAS} JP ON P._id = JP.id_plataforma
            WHERE JP.id_jogo = ?""", arrayOf(id.toString()))

        val plataformas = mutableListOf<Platform>()
        if (cursor.count > 0) {
            cursor.moveToFirst()

            do {
                val plataforma = Platform(
                        id = cursor.getLong(cursor.getColumnIndex(Helper.PLATAFORMAS_ID)),
                        name = cursor.getString(cursor.getColumnIndex(Helper.PLATAFORMAS_NOME)))

                plataformas.add(plataforma)
            } while (cursor.moveToNext())

        }

        cursor.close()

        return plataformas
    }
}