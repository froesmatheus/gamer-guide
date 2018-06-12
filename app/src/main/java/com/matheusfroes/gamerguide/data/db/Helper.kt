package com.matheusfroes.gamerguide.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.matheusfroes.gamerguide.data.model.NewsSource
import com.matheusfroes.gamerguide.data.model.Platform

class Helper(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        val DB_NAME = "GamerGuide"
        val DB_VERSION = 1

        // Tabela de Jogos
        val TABELA_JOGOS = "games"
        val JOGOS_ID = "_id"
        val JOGOS_NOME = "name"
        val JOGOS_DESCRICAO = "description"
        val JOGOS_DESENVOLVEDORAS = "developers"
        val JOGOS_PUBLICADORAS = "publishers"
        val JOGOS_GENEROS = "genres"
        val JOGOS_DATA_LANCAMENTO = "data_lancamento"
        val JOGOS_IMAGEM_CAPA = "imagem_capa"
        val JOGOS_GAME_ENGINE = "game_engine"
        val JOGOS_FORMA_CADASTRO = "forma_cadastro"


        // Tabela de Plataformas
        val TABELA_PLATAFORMAS = "platforms"
        val PLATAFORMAS_ID = "_id"
        val PLATAFORMAS_NOME = "name"


        // Tabela ligação Jogos/Plataformas
        val TABELA_JOGOS_PLATAFORMAS = "jogos_plataformas"
        val JOGOS_PLATAFORMAS_ID_JOGO = "id_jogo"
        val JOGOS_PLATAFORMAS_ID_PLATAFORMA = "id_plataforma"

        // Tabela de Listas
        val TABELA_LISTAS = "listas"
        val LISTAS_ID = "_id"
        val LISTAS_NOME = "name"

        // Tabela ligação Listas/Jogos
        val TABELA_LISTAS_JOGOS = "listas_jogos"
        val LISTAS_JOGOS_ID_LISTA = "id_lista"
        val LISTAS_JOGOS_ID_JOGO = "id_jogo"

        // Tabela de Vídeos
        val TABELA_VIDEOS = "videos"
        val VIDEOS_ID_JOGO = "id_jogo"
        val VIDEOS_NOME = "name"
        val VIDEOS_VIDEOID = "video_id"

        // Tabela de Progressos
        val TABELA_PROGRESSOS = "progressos_jogos"
        val PROGRESSOS_ID_JOGO = "_id"
        val PROGRESSOS_HORAS_JOGADAS = "horas_jogadas"
        val PROGRESSOS_PROGRESSO_PERC = "progresso_perc"
        val PROGRESSOS_ZERADO = "jogo_zerado"

        // Tabela de Fontes de Notícias
        val TABELA_FONTE_NOTICIAS = "fonte_noticias"
        val FONTE_NOTICIAS_ID = "_id"
        val FONTE_NOTICIAS_NOME = "name"
        val FONTE_NOTICIAS_WEBSITE = "website"
        val FONTE_NOTICIAS_ATIVADO = "enabled"

        // Tabela Time to Beat
        val TABELA_TTB = "time_to_beat"
        val TTB_ID_JOGO = "_id"
        val TTB_SPEEDRUN = "speedrun"
        val TTB_MODO_HISTORIA = "modo_historia"
        val TTB_100PERC = "cem_perc"

        val CREATE_TABLE_JOGOS = """
             CREATE TABLE $TABELA_JOGOS(
                $JOGOS_ID INTEGER PRIMARY KEY,
                $JOGOS_NOME TEXT,
                $JOGOS_DESCRICAO TEXT,
                $JOGOS_DESENVOLVEDORAS TEXT,
                $JOGOS_PUBLICADORAS INTEGER,
                $JOGOS_GENEROS TEXT,
                $JOGOS_DATA_LANCAMENTO INTEGER,
                $JOGOS_GAME_ENGINE TEXT,
                $JOGOS_IMAGEM_CAPA TEXT,
                $JOGOS_FORMA_CADASTRO TEXT);"""

        val CREATE_TABLE_PLATAFORMAS = """
            CREATE TABLE $TABELA_PLATAFORMAS(
                $PLATAFORMAS_ID INTEGER PRIMARY KEY,
                $PLATAFORMAS_NOME TEXT);"""

        val CREATE_TABLE_JOGOS_PLATAFORMAS = """
            CREATE TABLE $TABELA_JOGOS_PLATAFORMAS(
                $JOGOS_PLATAFORMAS_ID_JOGO INTEGER NOT NULL,
                $JOGOS_PLATAFORMAS_ID_PLATAFORMA INTEGER NOT NULL,
                PRIMARY KEY($JOGOS_PLATAFORMAS_ID_JOGO, $JOGOS_PLATAFORMAS_ID_PLATAFORMA),
                FOREIGN KEY($JOGOS_PLATAFORMAS_ID_JOGO) REFERENCES $TABELA_JOGOS($JOGOS_ID),
                FOREIGN KEY($JOGOS_PLATAFORMAS_ID_PLATAFORMA) REFERENCES $TABELA_PLATAFORMAS($PLATAFORMAS_ID));"""

        val CREATE_TABLE_LISTAS = """
            CREATE TABLE $TABELA_LISTAS(
                $LISTAS_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                $LISTAS_NOME TEXT NOT NULL);"""

        val CREATE_TABLE_LISTAS_JOGOS = """
            CREATE TABLE $TABELA_LISTAS_JOGOS(
                $LISTAS_JOGOS_ID_JOGO INTEGER NOT NULL,
                $LISTAS_JOGOS_ID_LISTA INTEGER NOT NULL,
                PRIMARY KEY($LISTAS_JOGOS_ID_JOGO, $LISTAS_JOGOS_ID_LISTA),
                FOREIGN KEY($LISTAS_JOGOS_ID_JOGO) REFERENCES $TABELA_JOGOS($JOGOS_ID),
                FOREIGN KEY($LISTAS_JOGOS_ID_LISTA) REFERENCES $TABELA_LISTAS($LISTAS_ID));"""

        val CREATE_TABLE_VIDEOS = """
            CREATE TABLE $TABELA_VIDEOS(
                $VIDEOS_ID_JOGO INTEGER NOT NULL,
                $VIDEOS_NOME TEXT NOT NULL,
                $VIDEOS_VIDEOID TEXT NOT NULL,
                PRIMARY KEY($VIDEOS_ID_JOGO, $VIDEOS_VIDEOID),
                FOREIGN KEY($VIDEOS_ID_JOGO) REFERENCES $TABELA_JOGOS($JOGOS_ID));"""

        val CREATE_TABLE_PROGRESSOS = """
            CREATE TABLE $TABELA_PROGRESSOS(
                $PROGRESSOS_ID_JOGO INTEGER PRIMARY KEY NOT NULL,
                $PROGRESSOS_HORAS_JOGADAS INTEGER,
                $PROGRESSOS_PROGRESSO_PERC INTEGER,
                $PROGRESSOS_ZERADO INTEGER,
                FOREIGN KEY($PROGRESSOS_ID_JOGO) REFERENCES $TABELA_JOGOS($JOGOS_ID));"""

        val CREATE_TABLE_TTB = """
            CREATE TABLE $TABELA_TTB(
                $TTB_ID_JOGO INTEGER PRIMARY KEY NOT NULL,
                $TTB_SPEEDRUN INTEGER,
                $TTB_MODO_HISTORIA INTEGER,
                $TTB_100PERC INTEGER,
                FOREIGN KEY($TTB_ID_JOGO) REFERENCES $TABELA_JOGOS($JOGOS_ID));"""

        val CREATE_TABELA_FONTE_NOTICAS = """
            CREATE TABLE $TABELA_FONTE_NOTICIAS(
                $FONTE_NOTICIAS_ID INTEGER PRIMARY KEY NOT NULL,
                $FONTE_NOTICIAS_NOME TEXT NOT NULL,
                $FONTE_NOTICIAS_WEBSITE TEXT NOT NULL,
                $FONTE_NOTICIAS_ATIVADO INTEGER NOT NULL);"""
    }


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_JOGOS)
        db.execSQL(CREATE_TABLE_PLATAFORMAS)
        db.execSQL(CREATE_TABLE_JOGOS_PLATAFORMAS)
        db.execSQL(CREATE_TABLE_LISTAS)
        db.execSQL(CREATE_TABLE_LISTAS_JOGOS)
        db.execSQL(CREATE_TABLE_VIDEOS)
        db.execSQL(CREATE_TABLE_PROGRESSOS)
        db.execSQL(CREATE_TABLE_TTB)
        db.execSQL(CREATE_TABELA_FONTE_NOTICAS)

        inserirPlataformas(db)
        inserirListaPadrao(db)
        insertFontesNoticias(db)
    }

    private fun insertFontesNoticias(db: SQLiteDatabase) {
        db.beginTransaction()
        val fontesNoticias = getFontesNoticias()
        try {
            val cv = ContentValues()

            fontesNoticias.forEach { fonteNoticia ->
                cv.put(FONTE_NOTICIAS_NOME, fonteNoticia.name)
                cv.put(FONTE_NOTICIAS_WEBSITE, fonteNoticia.website)
                cv.put(FONTE_NOTICIAS_ATIVADO, if (fonteNoticia.enabled) 1 else 0)

                db.insert(TABELA_FONTE_NOTICIAS, null, cv)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }


    private fun inserirListaPadrao(db: SQLiteDatabase) {
        val cv = ContentValues()

        cv.put(LISTAS_NOME, "GameList de compras")

        db.insert(TABELA_LISTAS, null, cv)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE $TABELA_JOGOS_PLATAFORMAS IF EXISTS;")
        db.execSQL("DROP TABLE $TABELA_LISTAS_JOGOS IF EXISTS;")
        db.execSQL("DROP TABLE $TABELA_VIDEOS IF EXISTS;")
        db.execSQL("DROP TABLE $TABELA_PROGRESSOS IF EXISTS;")
        db.execSQL("DROP TABLE $TABELA_TTB IF EXISTS;")
        db.execSQL("DROP TABLE $TABELA_LISTAS IF EXISTS;")
        db.execSQL("DROP TABLE $TABELA_JOGOS IF EXISTS;")
        db.execSQL("DROP TABLE $TABELA_PLATAFORMAS IF EXISTS;")
        db.execSQL("DROP TABLE $TABELA_FONTE_NOTICIAS IF EXISTS;")
        onCreate(db)
    }


    private fun inserirPlataformas(db: SQLiteDatabase) {
        db.beginTransaction()
        val plataformas = getPlataformas()
        try {
            val cv = ContentValues()

            plataformas.forEach { plataforma ->
                cv.put(PLATAFORMAS_ID, plataforma.id)
                cv.put(PLATAFORMAS_NOME, plataforma.name.trim())

                db.insert(TABELA_PLATAFORMAS, null, cv)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }


    private fun getFontesNoticias(): List<NewsSource> = listOf(
            NewsSource(name = "Eurogamer", website = "http://www.eurogamer.pt/?format=rss", enabled = true),
            NewsSource(name = "CriticalHits", website = "https://criticalhits.com.br/feed/", enabled = true)
//            NewsSource(name = "Combo Infinito", website = "http://www.comboinfinito.com.br/principal/feed/", enabled = true)
    )

    private fun getPlataformas() = listOf(
            Platform(3, "Linux	"),
            Platform(4, "Nintendo 64	"),
            Platform(5, "Wii	"),
            Platform(6, "PC"),
            Platform(7, "PlayStation	"),
            Platform(8, "PS2	"),
            Platform(9, "PS3	"),
            Platform(11, "Xbox	"),
            Platform(12, "Xbox 360	"),
            Platform(13, "PC DOS	"),
            Platform(14, "Mac	"),
            Platform(15, "Commodore C64/128	"),
            Platform(16, "Amiga	"),
            Platform(18, "Nintendo Entertainment System (NES)	"),
            Platform(19, "Super Nintendo Entertainment System (SNES)	"),
            Platform(20, "Nintendo DS	"),
            Platform(21, "Nintendo GameCube	"),
            Platform(22, "Game Boy Color	"),
            Platform(23, "Dreamcast	"),
            Platform(24, "Game Boy Advance	"),
            Platform(25, "Amstrad CPC	"),
            Platform(26, "ZX Spectrum	"),
            Platform(27, "MSX	"),
            Platform(29, "Sega Mega Drive/Genesis	"),
            Platform(30, "Sega 32X	"),
            Platform(32, "Sega Saturn	"),
            Platform(33, "Game Boy	"),
            Platform(34, "Android	"),
            Platform(35, "Sega Game Gear	"),
            Platform(36, "Xbox Live Arcade	"),
            Platform(37, "Nintendo 3DS	"),
            Platform(38, "PlayStation Portable	"),
            Platform(39, "iOS	"),
            Platform(41, "Wii U	"),
            Platform(42, "N-Gage	"),
            Platform(44, "Tapwave Zodiac	"),
            Platform(45, "PlayStation Network	"),
            Platform(46, "PlayStation Vita	"),
            Platform(47, "Virtual Console (Nintendo)	"),
            Platform(48, "PlayStation 4	"),
            Platform(49, "Xbox One	"),
            Platform(50, "3DO Interactive Multiplayer	"),
            Platform(51, "Family Computer Disk System	"),
            Platform(52, "Arcade	"),
            Platform(53, "MSX2	"),
            Platform(55, "Mobile	"),
            Platform(56, "WiiWare	"),
            Platform(57, "WonderSwan	"),
            Platform(58, "Super Famicom	"),
            Platform(59, "Atari 2600	"),
            Platform(60, "Atari 7800	"),
            Platform(61, "Atari Lynx	"),
            Platform(62, "Atari Jaguar	"),
            Platform(63, "Atari ST/STE	"),
            Platform(64, "Sega Master System	"),
            Platform(65, "Atari 8-bit	"),
            Platform(66, "Atari 5200	"),
            Platform(67, "Intellivision	"),
            Platform(68, "ColecoVision	"),
            Platform(69, "BBC Microcomputer System	"),
            Platform(70, "Vectrex	"),
            Platform(71, "Commodore VIC-20	"),
            Platform(72, "Ouya	"),
            Platform(73, "BlackBerry OS	"),
            Platform(74, "Windows Phone	"),
            Platform(75, "Apple II	"),
            Platform(77, "Sharp X1	"),
            Platform(78, "Sega CD	"),
            Platform(79, "Neo Geo MVS	"),
            Platform(80, "Neo Geo AES	"),
            Platform(82, "Web browser	"),
            Platform(84, "SG-1000	"),
            Platform(85, "Donner Model 30	"),
            Platform(86, "TurboGrafx-16/PC Engine	"),
            Platform(87, "Virtual Boy	"),
            Platform(88, "Odyssey	"),
            Platform(89, "Microvision	"),
            Platform(90, "Commodore PET	"),
            Platform(91, "Bally Astrocade	"),
            Platform(92, "SteamOS	"),
            Platform(93, "Commodore 16	"),
            Platform(94, "Commodore Plus/4	"),
            Platform(95, "PDP-1	"),
            Platform(96, "PDP-10	"),
            Platform(97, "PDP-8	"),
            Platform(98, "DEC GT40	"),
            Platform(99, "Family Computer (FAMICOM)	"),
            Platform(100, "Analogue electronics	"),
            Platform(101, "Ferranti Nimrod Computer	"),
            Platform(102, "EDSAC	"),
            Platform(103, "PDP-7	"),
            Platform(104, "HP 2100	"),
            Platform(105, "HP 3000	"),
            Platform(106, "SDS Sigma 7	"),
            Platform(107, "Call-A-Computer time-shared mainframe computer system	"),
            Platform(108, "PDP-11	"),
            Platform(109, "CDC Cyber 70	"),
            Platform(110, "PLATO	"),
            Platform(111, "Imlac PDS-1	"),
            Platform(112, "Microcomputer	"),
            Platform(113, "OnLive Game System	"),
            Platform(114, "Amiga CD32	"),
            Platform(115, "Apple IIGS	"),
            Platform(116, "Acorn Archimedes	"),
            Platform(117, "Philips CD-i	"),
            Platform(118, "FM Towns	"),
            Platform(119, "Neo Geo Pocket	"),
            Platform(120, "Neo Geo Pocket Color	"),
            Platform(121, "Sharp X68000	"),
            Platform(122, "Nuon	"),
            Platform(123, "WonderSwan Color	"),
            Platform(124, "SwanCrystal	"),
            Platform(125, "PC-8801	"),
            Platform(126, "TRS-80	"),
            Platform(127, "Fairchild Channel F	"),
            Platform(128, "PC Engine SuperGrafx	"),
            Platform(129, "Texas Instruments TI-99	"),
            Platform(130, "Nintendo Switch	"),
            Platform(131, "Nintendo PlayStation	"),
            Platform(132, "Amazon Fire TV	"),
            Platform(133, "Philips Videopac G7000	"),
            Platform(134, "Acorn Electron	"),
            Platform(135, "Hyper Neo Geo 64	"),
            Platform(136, "Neo Geo CD	"),
            Platform(137, "New Nintendo 3DS	"),
            Platform(138, "VC 4000	"),
            Platform(139, "1292 Advanced Programmable Video System	"),
            Platform(140, "AY-3-8500	"),
            Platform(141, "AY-3-8610	"),
            Platform(142, "PC-50X Family	"),
            Platform(143, "AY-3-8760	"),
            Platform(144, "AY-3-8710	"),
            Platform(145, "AY-3-8603	"),
            Platform(146, "AY-3-8605	"),
            Platform(147, "AY-3-8606	"),
            Platform(148, "AY-3-8607	"))
}