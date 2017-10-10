package com.matheusfroes.gamerguide.db

import android.content.ContentValues
import android.content.Context
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import com.matheusfroes.gamerguide.models.Lista

/**
 * Created by matheus_froes on 28/09/2017.
 */
class ListasDAO(val context: Context) {
    private val db: SQLiteDatabase = Helper(context).writableDatabase
    private val jogosDAO: JogosDAO by lazy {
        JogosDAO(context)
    }

    fun inserir(lista: Lista) {
        val cv = ContentValues()

        cv.put(Helper.LISTAS_NOME, lista.nome)

        val listaId = db.insert(Helper.TABELA_LISTAS, null, cv)


        val cvJogos = ContentValues()
        lista.jogos.forEach { jogo ->
            cvJogos.put(Helper.LISTAS_JOGOS_ID_JOGO, jogo.id)
            cvJogos.put(Helper.LISTAS_JOGOS_ID_LISTA, listaId)

            db.insert(Helper.TABELA_LISTAS_JOGOS, null, cvJogos)
        }
    }

    fun verificarListaJaExistente(nomeLista: String): Boolean {
        val count = DatabaseUtils.queryNumEntries(db, Helper.TABELA_LISTAS, "${Helper.LISTAS_NOME} = ?", arrayOf(nomeLista))

        return count > 0
    }

    fun excluirLista(listaId: Int) {
        db.delete(Helper.TABELA_LISTAS_JOGOS, "id_lista = ?", arrayOf(listaId.toString()))
        db.delete(Helper.TABELA_LISTAS, "_id = ?", arrayOf(listaId.toString()))
    }

    fun obterLista(id: Int): Lista? {
        val cursor = db.rawQuery("SELECT * FROM ${Helper.TABELA_LISTAS} WHERE ${Helper.LISTAS_ID} = ?", arrayOf(id.toString()))

        var jogo: Lista? = null

        if (cursor.count > 0) {
            cursor.moveToFirst()

            jogo = Lista(
                    id = cursor.getInt(cursor.getColumnIndex(Helper.LISTAS_ID)),
                    nome = cursor.getString(cursor.getColumnIndex(Helper.LISTAS_NOME)),
                    jogos = jogosDAO.obterJogosPorLista(id)
            )
        }

        cursor.close()

        return jogo
    }

    fun adicionarJogoNaLista(jogoId: Long, listaId: Int) {
        val cv = ContentValues()

        cv.put(Helper.LISTAS_JOGOS_ID_JOGO, jogoId)
        cv.put(Helper.LISTAS_JOGOS_ID_LISTA, listaId)

        db.insert(Helper.TABELA_LISTAS_JOGOS, null, cv)
    }

    fun removerJogoDaLista(jogoId: Long, listaId: Int) {
        db.delete(Helper.TABELA_LISTAS_JOGOS, "${Helper.LISTAS_JOGOS_ID_JOGO} = ? AND ${Helper.LISTAS_JOGOS_ID_LISTA} = ?", arrayOf(jogoId.toString(), listaId.toString()))
    }

    fun listaContemJogo(jogoId: Long, listaId: Int): Boolean {
        val count = DatabaseUtils.queryNumEntries(db, Helper.TABELA_LISTAS_JOGOS, "${Helper.LISTAS_JOGOS_ID_LISTA} = ? AND ${Helper.LISTAS_JOGOS_ID_JOGO} = ?", arrayOf(listaId.toString(), jogoId.toString()))

        return count > 0
    }

    fun obterListas(): List<Lista> {
        val cursor = db.rawQuery("SELECT * FROM ${Helper.TABELA_LISTAS}", null)

        val listas = mutableListOf<Lista>()

        if (cursor.count > 0) {
            cursor.moveToFirst()

            do {
                val listaId = cursor.getInt(cursor.getColumnIndex(Helper.LISTAS_ID))
                val lista = Lista(
                        id = listaId,
                        nome = cursor.getString(cursor.getColumnIndex(Helper.LISTAS_NOME)),
                        jogos = jogosDAO.obterJogosPorLista(listaId)
                )

                listas.add(lista)
            } while (cursor.moveToNext())

        }

        cursor.close()

        return listas
    }
}