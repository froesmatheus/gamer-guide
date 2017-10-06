package com.matheusfroes.gamerguide.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.matheusfroes.gamerguide.models.ProgressoJogo

/**
 * Created by matheus_froes on 28/09/2017.
 */
class ProgressoDAO(val context: Context) {
    private val db: SQLiteDatabase = Helper(context).writableDatabase

    private fun inserir(progresso: ProgressoJogo, idJogo: Int) {
        val cv = ContentValues()

        cv.put(Helper.PROGRESSOS_HORAS_JOGADAS, progresso.horasJogadas)
        cv.put(Helper.PROGRESSOS_PROGRESSO_PERC, progresso.progressoPerc)
        cv.put(Helper.PROGRESSOS_ZERADO, if (progresso.zerado) 1 else 0)
        cv.put(Helper.PROGRESSOS_ID_JOGO, idJogo)

        db.insert(Helper.TABELA_PROGRESSOS, null, cv)
    }

    fun atualizarProgresso(progresso: ProgressoJogo, idJogo: Int) {
        val progressoAtual = obterProgressoPorJogo(idJogo)

        if (progressoAtual == null) {
            inserir(progresso, idJogo)
        } else {
            val cv = ContentValues()

            cv.put(Helper.PROGRESSOS_HORAS_JOGADAS, progresso.horasJogadas)
            cv.put(Helper.PROGRESSOS_PROGRESSO_PERC, progresso.progressoPerc)
            cv.put(Helper.PROGRESSOS_ZERADO, if (progresso.zerado) 1 else 0)

            db.update(Helper.TABELA_PROGRESSOS, cv, "${Helper.PROGRESSOS_ID_JOGO} = ?", arrayOf(idJogo.toString()))
        }
    }

    fun obterProgressoPorJogo(idJogo: Int): ProgressoJogo? {
        val cursor = db.rawQuery("""
            SELECT *
            FROM ${Helper.TABELA_PROGRESSOS}
            WHERE ${Helper.PROGRESSOS_ID_JOGO} = ?""", arrayOf(idJogo.toString()))

        var progresso: ProgressoJogo? = null

        if (cursor.count > 0) {
            cursor.moveToFirst()

            progresso = ProgressoJogo(
                    horasJogadas = cursor.getInt(cursor.getColumnIndex(Helper.PROGRESSOS_HORAS_JOGADAS)),
                    progressoPerc = cursor.getInt(cursor.getColumnIndex(Helper.PROGRESSOS_PROGRESSO_PERC)),
                    zerado = cursor.getInt(cursor.getColumnIndex(Helper.PROGRESSOS_ZERADO)) == 1
            )
        }

        cursor.close()

        return progresso
    }
}