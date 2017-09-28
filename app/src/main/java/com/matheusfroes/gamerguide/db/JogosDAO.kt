package com.matheusfroes.gamerguide.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.matheusfroes.gamerguide.models.Jogo
import java.util.*

/**
 * Created by matheus_froes on 28/09/2017.
 */
class JogosDAO(context: Context) {
    private val db: SQLiteDatabase = Helper(context).writableDatabase

    fun inserir(jogo: Jogo) {
        val cv = ContentValues()

        cv.put(Helper.JOGOS_ID, jogo.id)
        cv.put(Helper.JOGOS_NOME, jogo.nome)
        cv.put(Helper.JOGOS_DATA_LANCAMENTO, jogo.dataLancamento.time)
        cv.put(Helper.JOGOS_DESENVOLVEDORAS, jogo.desenvolvedoras)
        cv.put(Helper.JOGOS_PUBLICADORAS, jogo.publicadoras)
        cv.put(Helper.JOGOS_DESCRICAO, jogo.descricao)
        cv.put(Helper.JOGOS_IMAGEM_CAPA, jogo.imageCapa)
        cv.put(Helper.JOGOS_GENEROS, jogo.generos)

        db.insert(Helper.TABELA_JOGOS, null, cv)
    }

    fun obterJogo(id: Int): Jogo? {
        val cursor = db.rawQuery("SELECT * FROM ${Helper.TABELA_JOGOS} WHERE ${Helper.JOGOS_ID} = ?", arrayOf(id.toString()))

        var jogo: Jogo? = null

        if (cursor.count > 0) {
            cursor.moveToFirst()

            jogo = Jogo(
                    id = cursor.getLong(cursor.getColumnIndex(Helper.JOGOS_ID)),
                    descricao = cursor.getString(cursor.getColumnIndex(Helper.JOGOS_DESCRICAO)),
                    desenvolvedoras = cursor.getString(cursor.getColumnIndex(Helper.JOGOS_DESENVOLVEDORAS)),
                    imageCapa = cursor.getString(cursor.getColumnIndex(Helper.JOGOS_IMAGEM_CAPA)),
                    publicadoras = cursor.getString(cursor.getColumnIndex(Helper.JOGOS_PUBLICADORAS)),
                    generos = cursor.getString(cursor.getColumnIndex(Helper.JOGOS_GENEROS)),
                    nome = cursor.getString(cursor.getColumnIndex(Helper.JOGOS_NOME)),
                    plataformas = mutableListOf(),
                    dataLancamento = Date(cursor.getLong(cursor.getColumnIndex(Helper.JOGOS_DATA_LANCAMENTO)))
            )
        }

        cursor.close()

        return jogo
    }
}