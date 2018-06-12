package com.matheusfroes.gamerguide.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.matheusfroes.gamerguide.data.model.GameProgress


class ProgressoDAO(val context: Context) {
    private val db: SQLiteDatabase = Helper(context).writableDatabase

    private fun inserir(progresso: GameProgress, idJogo: Long) {
        val cv = ContentValues()

        cv.put(Helper.PROGRESSOS_HORAS_JOGADAS, progresso.hoursPlayed)
        cv.put(Helper.PROGRESSOS_PROGRESSO_PERC, progresso.percentage)
        cv.put(Helper.PROGRESSOS_ZERADO, if (progresso.beaten) 1 else 0)
        cv.put(Helper.PROGRESSOS_ID_JOGO, idJogo)

        db.insert(Helper.TABELA_PROGRESSOS, null, cv)
    }

    fun atualizarProgresso(progresso: GameProgress, idJogo: Long) {
        val progressoAtual = obterProgressoPorJogo(idJogo)

        if (progressoAtual == null) {
            inserir(progresso, idJogo)
        } else {
            val cv = ContentValues()

            cv.put(Helper.PROGRESSOS_HORAS_JOGADAS, progresso.hoursPlayed)
            cv.put(Helper.PROGRESSOS_PROGRESSO_PERC, progresso.percentage)
            cv.put(Helper.PROGRESSOS_ZERADO, if (progresso.beaten) 1 else 0)

            db.update(Helper.TABELA_PROGRESSOS, cv, "${Helper.PROGRESSOS_ID_JOGO} = ?", arrayOf(idJogo.toString()))
        }
    }

    fun obterProgressoPorJogo(idJogo: Long): GameProgress? {
        val cursor = db.rawQuery("""
            SELECT *
            FROM ${Helper.TABELA_PROGRESSOS}
            WHERE ${Helper.PROGRESSOS_ID_JOGO} = ?""", arrayOf(idJogo.toString()))

        var progresso: GameProgress? = null

        if (cursor.count > 0) {
            cursor.moveToFirst()

            progresso = GameProgress(
                    hoursPlayed = cursor.getInt(cursor.getColumnIndex(Helper.PROGRESSOS_HORAS_JOGADAS)),
                    percentage = cursor.getInt(cursor.getColumnIndex(Helper.PROGRESSOS_PROGRESSO_PERC)),
                    beaten = cursor.getInt(cursor.getColumnIndex(Helper.PROGRESSOS_ZERADO)) == 1
            )
        }

        cursor.close()

        return progresso
    }
}