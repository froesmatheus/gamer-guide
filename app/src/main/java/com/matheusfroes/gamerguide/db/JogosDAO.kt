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
    private val videosDAO: VideosDAO by lazy {
        VideosDAO(context)
    }
    private val plataformasDAO: PlataformasDAO by lazy {
        PlataformasDAO(context)
    }
    private val timeToBeatDAO: TimeToBeatDAO by lazy {
        TimeToBeatDAO(context)
    }

    fun inserir(jogo: Jogo) {
        val cv = ContentValues()

        cv.put(Helper.JOGOS_ID, jogo.id)
        cv.put(Helper.JOGOS_NOME, jogo.nome)
        cv.put(Helper.JOGOS_DATA_LANCAMENTO, jogo.dataLancamento.time)
        cv.put(Helper.JOGOS_DESENVOLVEDORAS, jogo.desenvolvedores)
        cv.put(Helper.JOGOS_PUBLICADORAS, jogo.publicadoras)
        cv.put(Helper.JOGOS_DESCRICAO, jogo.descricao)
        cv.put(Helper.JOGOS_IMAGEM_CAPA, jogo.imageCapa)
        cv.put(Helper.JOGOS_GENEROS, jogo.generos)

        val cvPlataformas = ContentValues()
        jogo.plataformas.forEach { plataforma ->
            cvPlataformas.put(Helper.JOGOS_PLATAFORMAS_ID_JOGO, jogo.id)
            cvPlataformas.put(Helper.JOGOS_PLATAFORMAS_ID_PLATAFORMA, plataforma.id)

            db.insert(Helper.TABELA_JOGOS_PLATAFORMAS, null, cvPlataformas)
        }

        videosDAO.inserir(jogo.videos, jogo.id, db)
        timeToBeatDAO.inserir(jogo.timeToBeat, jogo.id, db)

        db.insert(Helper.TABELA_JOGOS, null, cv)
    }

    fun remover(jogoId: Int) {
        db.delete(Helper.TABELA_JOGOS_PLATAFORMAS, "id_jogo = ?", arrayOf(jogoId.toString()))
        db.delete(Helper.TABELA_JOGOS, "_id = ?", arrayOf(jogoId.toString()))
    }

    fun obterJogo(id: Int): Jogo? {
        val cursor = db.rawQuery("SELECT * FROM ${Helper.TABELA_JOGOS} WHERE ${Helper.JOGOS_ID} = ?", arrayOf(id.toString()))

        var jogo: Jogo? = null

        if (cursor.count > 0) {
            cursor.moveToFirst()

            val jogoId = cursor.getLong(cursor.getColumnIndex(Helper.JOGOS_ID))

            jogo = Jogo(
                    id = jogoId,
                    descricao = cursor.getString(cursor.getColumnIndex(Helper.JOGOS_DESCRICAO)),
                    desenvolvedores = cursor.getString(cursor.getColumnIndex(Helper.JOGOS_DESENVOLVEDORAS)),
                    imageCapa = cursor.getString(cursor.getColumnIndex(Helper.JOGOS_IMAGEM_CAPA)),
                    publicadoras = cursor.getString(cursor.getColumnIndex(Helper.JOGOS_PUBLICADORAS)),
                    generos = cursor.getString(cursor.getColumnIndex(Helper.JOGOS_GENEROS)),
                    nome = cursor.getString(cursor.getColumnIndex(Helper.JOGOS_NOME)),
                    plataformas = plataformasDAO.obterPlataformasPorJogo(id),
                    videos = videosDAO.getVideosPorJogo(jogoId),
                    timeToBeat = timeToBeatDAO.obterTTBPorJogo(jogoId),
                    dataLancamento = Date(cursor.getLong(cursor.getColumnIndex(Helper.JOGOS_DATA_LANCAMENTO)))
            )
        }

        cursor.close()

        return jogo
    }

    fun obterJogosPorLista(id: Int): List<Jogo> {
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
                        plataformas = plataformasDAO.obterPlataformasPorJogo(id),
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