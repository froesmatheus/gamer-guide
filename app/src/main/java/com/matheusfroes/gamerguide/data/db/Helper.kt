package com.matheusfroes.gamerguide.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.matheusfroes.gamerguide.data.models.FonteNoticia

class Helper(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        val DB_NAME = "GamerGuide"
        val DB_VERSION = 1

        // Tabela de Fontes de Notícias
        val TABELA_FONTE_NOTICIAS = "fonte_noticias"
        val FONTE_NOTICIAS_ID = "_id"
        val FONTE_NOTICIAS_NOME = "nome"
        val FONTE_NOTICIAS_WEBSITE = "website"
        val FONTE_NOTICIAS_ATIVADO = "ativado"

        val CREATE_TABELA_FONTE_NOTICAS = """
            CREATE TABLE $TABELA_FONTE_NOTICIAS(
                $FONTE_NOTICIAS_ID INTEGER PRIMARY KEY NOT NULL,
                $FONTE_NOTICIAS_NOME TEXT NOT NULL,
                $FONTE_NOTICIAS_WEBSITE TEXT NOT NULL,
                $FONTE_NOTICIAS_ATIVADO INTEGER NOT NULL);"""
    }


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABELA_FONTE_NOTICAS)
        insertFontesNoticias(db)
    }

    private fun insertFontesNoticias(db: SQLiteDatabase) {
        db.beginTransaction()
        val fontesNoticias = getFontesNoticias()
        try {
            val cv = ContentValues()

            fontesNoticias.forEach { fonteNoticia ->
                cv.put(FONTE_NOTICIAS_NOME, fonteNoticia.nome)
                cv.put(FONTE_NOTICIAS_WEBSITE, fonteNoticia.website)
                cv.put(FONTE_NOTICIAS_ATIVADO, if (fonteNoticia.ativado) 1 else 0)

                db.insert(TABELA_FONTE_NOTICIAS, null, cv)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE $TABELA_FONTE_NOTICIAS IF EXISTS;")
        onCreate(db)
    }

    private fun getFontesNoticias(): List<FonteNoticia> = listOf(
            FonteNoticia(nome = "Eurogamer", website = "http://www.eurogamer.pt/?format=rss", ativado = true),
            FonteNoticia(nome = "CriticalHits", website = "https://criticalhits.com.br/feed/", ativado = true)
    )
}