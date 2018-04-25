package com.matheusfroes.gamerguide.data.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import com.matheusfroes.gamerguide.data.models.FormaCadastro
import com.matheusfroes.gamerguide.data.models.Jogo
import java.util.*


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
    private val progressosDAO: ProgressoDAO by lazy {
        ProgressoDAO(context)
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
        cv.put(Helper.JOGOS_GAME_ENGINE, jogo.gameEngine)
        cv.put(Helper.JOGOS_FORMA_CADASTRO, jogo.formaCadastro.name)

        val cvPlataformas = ContentValues()
        jogo.plataformas.forEach { plataforma ->
            cvPlataformas.put(Helper.JOGOS_PLATAFORMAS_ID_JOGO, jogo.id)
            cvPlataformas.put(Helper.JOGOS_PLATAFORMAS_ID_PLATAFORMA, plataforma.id)

            db.insert(Helper.TABELA_JOGOS_PLATAFORMAS, null, cvPlataformas)
        }

        videosDAO.inserir(jogo.videos, jogo.id)
        timeToBeatDAO.inserir(jogo.timeToBeat, jogo.id)
        progressosDAO.atualizarProgresso(jogo.progresso, jogo.id)

        db.insert(Helper.TABELA_JOGOS, null, cv)
    }

    fun atualizar(jogo: Jogo) {
        val cv = ContentValues()

        cv.put(Helper.JOGOS_NOME, jogo.nome)
        cv.put(Helper.JOGOS_DATA_LANCAMENTO, jogo.dataLancamento.time)
        cv.put(Helper.JOGOS_DESENVOLVEDORAS, jogo.desenvolvedores)
        cv.put(Helper.JOGOS_PUBLICADORAS, jogo.publicadoras)
        cv.put(Helper.JOGOS_DESCRICAO, jogo.descricao)
        cv.put(Helper.JOGOS_IMAGEM_CAPA, jogo.imageCapa)
        cv.put(Helper.JOGOS_GENEROS, jogo.generos)
        cv.put(Helper.JOGOS_GAME_ENGINE, jogo.gameEngine)
        cv.put(Helper.JOGOS_FORMA_CADASTRO, jogo.formaCadastro.name)


        db.update(Helper.TABELA_JOGOS, cv, "_id = ?", arrayOf(jogo.id.toString()))
    }

    fun remover(jogoId: Long) {
        val parametros = arrayOf(jogoId.toString())

        db.delete(Helper.TABELA_JOGOS_PLATAFORMAS, "id_jogo = ?", parametros)
        db.delete(Helper.TABELA_PROGRESSOS, "_id = ?", parametros)
        db.delete(Helper.TABELA_TTB, "_id = ?", parametros)
        db.delete(Helper.TABELA_JOGOS, "_id = ?", parametros)
        db.delete(Helper.TABELA_VIDEOS, "id_jogo = ?", parametros)
    }

    fun obterJogoPorFormaCadastro(id: Long, formaCadastro: FormaCadastro = FormaCadastro.CADASTRO_POR_BUSCA): Jogo? {
        val cursor = db.rawQuery("SELECT * FROM ${Helper.TABELA_JOGOS} WHERE ${Helper.JOGOS_ID} = ? AND ${Helper.JOGOS_FORMA_CADASTRO} = ?", arrayOf(id.toString(), formaCadastro.name))

        var jogo: Jogo? = null

        if (cursor.count > 0) {
            cursor.moveToFirst()

            val jogoId = cursor.getLong(cursor.getColumnIndex(Helper.JOGOS_ID))

            jogo = criarObjetoJogo(cursor, jogoId)
        }

        cursor.close()

        return jogo
    }

    fun obterJogo(id: Long): Jogo? {
        val cursor = db.rawQuery("SELECT * FROM ${Helper.TABELA_JOGOS} WHERE ${Helper.JOGOS_ID} = ?", arrayOf(id.toString()))

        var jogo: Jogo? = null

        if (cursor.count > 0) {
            cursor.moveToFirst()

            val jogoId = cursor.getLong(cursor.getColumnIndex(Helper.JOGOS_ID))

            jogo = criarObjetoJogo(cursor, jogoId)
        }

        cursor.close()

        return jogo
    }

    fun obterJogosPorStatus(zerados: Boolean): List<Jogo> {
        val status = if (zerados) 1 else 0

        val cursor = db.rawQuery("""
            SELECT *
            FROM ${Helper.TABELA_JOGOS} J
            INNER JOIN ${Helper.TABELA_PROGRESSOS} P ON J._id = P._id
            WHERE P.jogo_zerado = ? AND ${Helper.JOGOS_FORMA_CADASTRO} = 'CADASTRO_POR_BUSCA'""", arrayOf(status.toString()))

        val jogos = mutableListOf<Jogo>()
        if (cursor.count > 0) {
            cursor.moveToFirst()

            do {
                val jogoId = cursor.getLong(cursor.getColumnIndex(Helper.JOGOS_ID))

                jogos.add(criarObjetoJogo(cursor, jogoId))
            } while (cursor.moveToNext())

        }

        cursor.close()

        return jogos
    }

    fun obterJogos(): List<Jogo> {
        val cursor = db.rawQuery("""
            SELECT *
            FROM ${Helper.TABELA_JOGOS} WHERE ${Helper.JOGOS_FORMA_CADASTRO} = 'CADASTRO_POR_BUSCA'""", null)

        val jogos = mutableListOf<Jogo>()
        if (cursor.count > 0) {
            cursor.moveToFirst()

            do {

                val jogoId = cursor.getLong(cursor.getColumnIndex(Helper.JOGOS_ID))

                jogos.add(criarObjetoJogo(cursor, jogoId))
            } while (cursor.moveToNext())

        }

        cursor.close()

        return jogos
    }

    fun obterJogosPorLista(idLista: Int): List<Jogo> {
        val cursor = db.rawQuery("""
            SELECT *
            FROM ${Helper.TABELA_JOGOS} J
            INNER JOIN ${Helper.TABELA_LISTAS_JOGOS} LJ ON J._id = LJ.id_jogo
            WHERE LJ.id_lista = ?""", arrayOf(idLista.toString()))

        val jogos = mutableListOf<Jogo>()
        if (cursor.count > 0) {
            cursor.moveToFirst()

            do {
                val jogoId = cursor.getLong(cursor.getColumnIndex(Helper.LISTAS_JOGOS_ID_JOGO))

                jogos.add(criarObjetoJogo(cursor, jogoId))
            } while (cursor.moveToNext())

        }

        cursor.close()

        return jogos
    }

    fun quantidadeJogos(zerado: Boolean): Long {
        val condicao = if (zerado) 1 else 0

        return DatabaseUtils.longForQuery(
                db, """
                    SELECT COUNT(*)
                    FROM ${Helper.TABELA_JOGOS} J
                    JOIN ${Helper.TABELA_PROGRESSOS} P ON J._id = P._id WHERE P.jogo_zerado = ?""", arrayOf(condicao.toString())
        )
    }

    fun quantidadeHorasJogadas(): Long {
        return DatabaseUtils.longForQuery(
                db, """
                    SELECT SUM(${Helper.PROGRESSOS_HORAS_JOGADAS})
                    FROM ${Helper.TABELA_JOGOS} J
                    JOIN ${Helper.TABELA_PROGRESSOS} P ON J._id = P._id""", null)
    }

    fun obterGenerosMaisJogados(): List<Pair<String, Int>> {
        val cursor = db.rawQuery("SELECT ${Helper.JOGOS_GENEROS} FROM ${Helper.TABELA_JOGOS}", null)

        val generos = mutableListOf<String>()
        if (cursor.count > 0) {
            cursor.moveToFirst()

            do {
                val genero = cursor.getString(cursor.getColumnIndex(Helper.JOGOS_GENEROS))

                if (genero.isNotEmpty()) {
                    val generoSplit = genero.split(",")
                    generos.addAll(generoSplit)
                }

            } while (cursor.moveToNext())
        }

        val generosDiferentes = mutableSetOf<Pair<String, Int>>()

        generos.forEach { genero ->
            val count = generos.count { it == genero }
            if (count > 0) {
                generosDiferentes.add(Pair(genero, count))
            }
        }

        val generosMaisJogados = generosDiferentes.sortedByDescending { it.second }.take(5)

        cursor.close()

        return generosMaisJogados
    }

    private fun criarObjetoJogo(cursor: Cursor, jogoId: Long): Jogo {
        return Jogo(
                id = jogoId,
                descricao = cursor.getString(cursor.getColumnIndex(Helper.JOGOS_DESCRICAO)),
                desenvolvedores = cursor.getString(cursor.getColumnIndex(Helper.JOGOS_DESENVOLVEDORAS)),
                imageCapa = cursor.getString(cursor.getColumnIndex(Helper.JOGOS_IMAGEM_CAPA)),
                publicadoras = cursor.getString(cursor.getColumnIndex(Helper.JOGOS_PUBLICADORAS)),
                generos = cursor.getString(cursor.getColumnIndex(Helper.JOGOS_GENEROS)),
                nome = cursor.getString(cursor.getColumnIndex(Helper.JOGOS_NOME)),
                plataformas = plataformasDAO.obterPlataformasPorJogo(jogoId),
                videos = videosDAO.getVideosPorJogo(jogoId),
                gameEngine = cursor.getString(cursor.getColumnIndex(Helper.JOGOS_GAME_ENGINE)),
                progresso = progressosDAO.obterProgressoPorJogo(jogoId)!!,
                timeToBeat = timeToBeatDAO.obterTTBPorJogo(jogoId),
                formaCadastro = FormaCadastro.valueOf(cursor.getString(cursor.getColumnIndex(Helper.JOGOS_FORMA_CADASTRO))),
                dataLancamento = Date(cursor.getLong(cursor.getColumnIndex(Helper.JOGOS_DATA_LANCAMENTO)))
        )
    }
}