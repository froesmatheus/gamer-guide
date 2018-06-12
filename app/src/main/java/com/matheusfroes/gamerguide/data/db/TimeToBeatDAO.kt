package com.matheusfroes.gamerguide.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.matheusfroes.gamerguide.data.model.TimeToBeat


class TimeToBeatDAO(val context: Context) {
    private val db: SQLiteDatabase = Helper(context).writableDatabase
    private val jogosDAO: JogosDAO by lazy {
        JogosDAO(context)
    }

    fun inserir(timeToBeat: TimeToBeat?, idJogo: Long) {
        val cv = ContentValues()

        timeToBeat?.apply {
            cv.put(Helper.TTB_ID_JOGO, idJogo)
            cv.put(Helper.TTB_SPEEDRUN, timeToBeat.hastly)
            cv.put(Helper.TTB_MODO_HISTORIA, timeToBeat.normally)
            cv.put(Helper.TTB_100PERC, timeToBeat.completely)

            db.insert(Helper.TABELA_TTB, null, cv)
        }
    }

    fun excluir(jogoId: Long) {
        db.delete(Helper.TABELA_TTB, "${Helper.TTB_ID_JOGO} = ?", arrayOf(jogoId.toString()))
    }

    fun obterTTBPorJogo(idJogo: Long): TimeToBeat? {
        val cursor = db.rawQuery("SELECT * FROM ${Helper.TABELA_TTB} WHERE ${Helper.TTB_ID_JOGO} = ?", arrayOf(idJogo.toString()))

        var ttb: TimeToBeat? = null

        if (cursor.count > 0) {
            cursor.moveToFirst()

            ttb = TimeToBeat(
                    hastly = cursor.getLong(cursor.getColumnIndex(Helper.TTB_SPEEDRUN)),
                    normally = cursor.getLong(cursor.getColumnIndex(Helper.TTB_MODO_HISTORIA)),
                    completely = cursor.getLong(cursor.getColumnIndex(Helper.TTB_100PERC))
            )
        }

        cursor.close()

        return ttb
    }
}