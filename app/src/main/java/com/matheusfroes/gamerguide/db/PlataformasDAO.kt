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

    fun obterPlataformasPorJogo(id: Long): List<Plataforma> {
        val cursor = db.rawQuery("""
            SELECT P.${Helper.PLATAFORMAS_ID}, P.${Helper.PLATAFORMAS_NOME}
            FROM ${Helper.TABELA_PLATAFORMAS} P
            INNER JOIN ${Helper.TABELA_JOGOS_PLATAFORMAS} JP ON P._id = JP.id_plataforma
            WHERE JP.id_jogo = ?""", arrayOf(id.toString()))

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