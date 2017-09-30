package com.matheusfroes.gamerguide.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.matheusfroes.gamerguide.models.Jogo
import com.matheusfroes.gamerguide.models.Lista
import java.util.*

/**
 * Created by matheus_froes on 28/09/2017.
 */
class ListasDAO(val context: Context) {
    private val db: SQLiteDatabase = Helper(context).writableDatabase
    private val videosDAO: VideosDAO by lazy {
        VideosDAO(context)
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
                    jogos = obterJogosDaLista(id)
            )
        }

        cursor.close()

        return jogo
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
                        jogos = obterJogosDaLista(listaId)
                )

                listas.add(lista)
            } while (cursor.moveToNext())

        }

        cursor.close()

        return listas
    }

    private fun obterJogosDaLista(id: Int): List<Jogo> {
        val jogosDAO = JogosDAO(context)

        val cursor = db.rawQuery("""
            SELECT *
            FROM ${Helper.TABELA_JOGOS} J
            INNER JOIN ${Helper.TABELA_LISTAS_JOGOS} LJ ON J._id = LJ.id_jogo
            WHERE LJ.id_lista = ?""", arrayOf(id.toString()))

        val jogos = mutableListOf<Jogo>()
        if (cursor.count > 0) {
            cursor.moveToFirst()

            do {

                val jogoId = cursor.getLong(cursor.getColumnIndex(Helper.JOGOS_ID))

                val jogo = Jogo(
                        id = jogoId,
                        descricao = cursor.getString(cursor.getColumnIndex(Helper.JOGOS_DESCRICAO)),
                        desenvolvedores = cursor.getString(cursor.getColumnIndex(Helper.JOGOS_DESENVOLVEDORAS)),
                        imageCapa = cursor.getString(cursor.getColumnIndex(Helper.JOGOS_IMAGEM_CAPA)),
                        publicadoras = cursor.getString(cursor.getColumnIndex(Helper.JOGOS_PUBLICADORAS)),
                        generos = cursor.getString(cursor.getColumnIndex(Helper.JOGOS_GENEROS)),
                        nome = cursor.getString(cursor.getColumnIndex(Helper.JOGOS_NOME)),
                        plataformas = jogosDAO.obterPlataformasPorJogo(id),
                        videos = videosDAO.getVideosPorJogo(jogoId),
                        dataLancamento = Date(cursor.getLong(cursor.getColumnIndex(Helper.JOGOS_DATA_LANCAMENTO)))
                )

                jogos.add(jogo)
            } while (cursor.moveToNext())

        }

        cursor.close()

        return jogos
    }
}