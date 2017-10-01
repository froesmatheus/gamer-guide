package com.matheusfroes.gamerguide.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.matheusfroes.gamerguide.models.Plataforma

/**
 * Created by matheus_froes on 28/09/2017.
 */
class PlataformasDAO(context: Context) {
    private val db: SQLiteDatabase = Helper(context).writableDatabase

    fun obterPlataforma(id: Long): Plataforma {
        val cursor = db.rawQuery("SELECT * FROM ${Helper.TABELA_PLATAFORMAS} WHERE ${Helper.PLATAFORMAS_ID} = ?", arrayOf(id.toString()))

        var plataforma: Plataforma? = null

        if (cursor.count > 0) {
            cursor.moveToFirst()

            plataforma = Plataforma(
                    id = cursor.getLong(cursor.getColumnIndex(Helper.PLATAFORMAS_ID)),
                    nome = cursor.getString(cursor.getColumnIndex(Helper.PLATAFORMAS_NOME))
            )
        }

        cursor.close()

        return plataforma!!
    }

    fun obterPlataformas(): List<Plataforma> {
        val cursor = db.rawQuery("SELECT * FROM ${Helper.TABELA_PLATAFORMAS}", null)

        val plataformas = mutableListOf<Plataforma>()
        if (cursor.count > 0) {
            cursor.moveToFirst()

            do {
                val plataforma = Plataforma(
                        id = cursor.getLong(cursor.getColumnIndex(Helper.PLATAFORMAS_ID)),
                        nome = cursor.getString(cursor.getColumnIndex(Helper.PLATAFORMAS_NOME)))

                plataformas.add(plataforma)
            } while (cursor.moveToNext())

        }

        cursor.close()

        return plataformas
    }
}