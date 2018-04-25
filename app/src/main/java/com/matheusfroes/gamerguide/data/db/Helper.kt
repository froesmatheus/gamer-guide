package com.matheusfroes.gamerguide.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.matheusfroes.gamerguide.data.models.FonteNoticia
import com.matheusfroes.gamerguide.data.models.Plataforma

class Helper(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        val DB_NAME = "GamerGuide"
        val DB_VERSION = 1

        // Tabela de Jogos
        val TABELA_JOGOS = "jogos"
        val JOGOS_ID = "_id"
        val JOGOS_NOME = "nome"
        val JOGOS_DESCRICAO = "descricao"
        val JOGOS_DESENVOLVEDORAS = "desenvolvedores"
        val JOGOS_PUBLICADORAS = "publicadoras"
        val JOGOS_GENEROS = "generos"
        val JOGOS_DATA_LANCAMENTO = "data_lancamento"
        val JOGOS_IMAGEM_CAPA = "imagem_capa"
        val JOGOS_GAME_ENGINE = "game_engine"
        val JOGOS_FORMA_CADASTRO = "forma_cadastro"


        // Tabela de Plataformas
        val TABELA_PLATAFORMAS = "plataformas"
        val PLATAFORMAS_ID = "_id"
        val PLATAFORMAS_NOME = "nome"


        // Tabela ligação Jogos/Plataformas
        val TABELA_JOGOS_PLATAFORMAS = "jogos_plataformas"
        val JOGOS_PLATAFORMAS_ID_JOGO = "id_jogo"
        val JOGOS_PLATAFORMAS_ID_PLATAFORMA = "id_plataforma"

        // Tabela de Listas
        val TABELA_LISTAS = "listas"
        val LISTAS_ID = "_id"
        val LISTAS_NOME = "nome"

        // Tabela ligação Listas/Jogos
        val TABELA_LISTAS_JOGOS = "listas_jogos"
        val LISTAS_JOGOS_ID_LISTA = "id_lista"
        val LISTAS_JOGOS_ID_JOGO = "id_jogo"

        // Tabela de Vídeos
        val TABELA_VIDEOS = "videos"
        val VIDEOS_ID_JOGO = "id_jogo"
        val VIDEOS_NOME = "nome"
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
        val FONTE_NOTICIAS_NOME = "nome"
        val FONTE_NOTICIAS_WEBSITE = "website"
        val FONTE_NOTICIAS_ATIVADO = "ativado"

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


    private fun inserirListaPadrao(db: SQLiteDatabase) {
        val cv = ContentValues()

        cv.put(LISTAS_NOME, "Lista de compras")

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
                cv.put(PLATAFORMAS_NOME, plataforma.nome.trim())

                db.insert(TABELA_PLATAFORMAS, null, cv)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }


    private fun getFontesNoticias(): List<FonteNoticia> = listOf(
            FonteNoticia(nome = "Eurogamer", website = "http://www.eurogamer.pt/?format=rss", ativado = true),
            FonteNoticia(nome = "CriticalHits", website = "https://criticalhits.com.br/feed/", ativado = true)
//            FonteNoticia(nome = "Combo Infinito", website = "http://www.comboinfinito.com.br/principal/feed/", ativado = true)
    )

    private fun getPlataformas() = listOf(
            Plataforma(3, "Linux	"),
            Plataforma(4, "Nintendo 64	"),
            Plataforma(5, "Wii	"),
            Plataforma(6, "PC"),
            Plataforma(7, "PlayStation	"),
            Plataforma(8, "PS2	"),
            Plataforma(9, "PS3	"),
            Plataforma(11, "Xbox	"),
            Plataforma(12, "Xbox 360	"),
            Plataforma(13, "PC DOS	"),
            Plataforma(14, "Mac	"),
            Plataforma(15, "Commodore C64/128	"),
            Plataforma(16, "Amiga	"),
            Plataforma(18, "Nintendo Entertainment System (NES)	"),
            Plataforma(19, "Super Nintendo Entertainment System (SNES)	"),
            Plataforma(20, "Nintendo DS	"),
            Plataforma(21, "Nintendo GameCube	"),
            Plataforma(22, "Game Boy Color	"),
            Plataforma(23, "Dreamcast	"),
            Plataforma(24, "Game Boy Advance	"),
            Plataforma(25, "Amstrad CPC	"),
            Plataforma(26, "ZX Spectrum	"),
            Plataforma(27, "MSX	"),
            Plataforma(29, "Sega Mega Drive/Genesis	"),
            Plataforma(30, "Sega 32X	"),
            Plataforma(32, "Sega Saturn	"),
            Plataforma(33, "Game Boy	"),
            Plataforma(34, "Android	"),
            Plataforma(35, "Sega Game Gear	"),
            Plataforma(36, "Xbox Live Arcade	"),
            Plataforma(37, "Nintendo 3DS	"),
            Plataforma(38, "PlayStation Portable	"),
            Plataforma(39, "iOS	"),
            Plataforma(41, "Wii U	"),
            Plataforma(42, "N-Gage	"),
            Plataforma(44, "Tapwave Zodiac	"),
            Plataforma(45, "PlayStation Network	"),
            Plataforma(46, "PlayStation Vita	"),
            Plataforma(47, "Virtual Console (Nintendo)	"),
            Plataforma(48, "PlayStation 4	"),
            Plataforma(49, "Xbox One	"),
            Plataforma(50, "3DO Interactive Multiplayer	"),
            Plataforma(51, "Family Computer Disk System	"),
            Plataforma(52, "Arcade	"),
            Plataforma(53, "MSX2	"),
            Plataforma(55, "Mobile	"),
            Plataforma(56, "WiiWare	"),
            Plataforma(57, "WonderSwan	"),
            Plataforma(58, "Super Famicom	"),
            Plataforma(59, "Atari 2600	"),
            Plataforma(60, "Atari 7800	"),
            Plataforma(61, "Atari Lynx	"),
            Plataforma(62, "Atari Jaguar	"),
            Plataforma(63, "Atari ST/STE	"),
            Plataforma(64, "Sega Master System	"),
            Plataforma(65, "Atari 8-bit	"),
            Plataforma(66, "Atari 5200	"),
            Plataforma(67, "Intellivision	"),
            Plataforma(68, "ColecoVision	"),
            Plataforma(69, "BBC Microcomputer System	"),
            Plataforma(70, "Vectrex	"),
            Plataforma(71, "Commodore VIC-20	"),
            Plataforma(72, "Ouya	"),
            Plataforma(73, "BlackBerry OS	"),
            Plataforma(74, "Windows Phone	"),
            Plataforma(75, "Apple II	"),
            Plataforma(77, "Sharp X1	"),
            Plataforma(78, "Sega CD	"),
            Plataforma(79, "Neo Geo MVS	"),
            Plataforma(80, "Neo Geo AES	"),
            Plataforma(82, "Web browser	"),
            Plataforma(84, "SG-1000	"),
            Plataforma(85, "Donner Model 30	"),
            Plataforma(86, "TurboGrafx-16/PC Engine	"),
            Plataforma(87, "Virtual Boy	"),
            Plataforma(88, "Odyssey	"),
            Plataforma(89, "Microvision	"),
            Plataforma(90, "Commodore PET	"),
            Plataforma(91, "Bally Astrocade	"),
            Plataforma(92, "SteamOS	"),
            Plataforma(93, "Commodore 16	"),
            Plataforma(94, "Commodore Plus/4	"),
            Plataforma(95, "PDP-1	"),
            Plataforma(96, "PDP-10	"),
            Plataforma(97, "PDP-8	"),
            Plataforma(98, "DEC GT40	"),
            Plataforma(99, "Family Computer (FAMICOM)	"),
            Plataforma(100, "Analogue electronics	"),
            Plataforma(101, "Ferranti Nimrod Computer	"),
            Plataforma(102, "EDSAC	"),
            Plataforma(103, "PDP-7	"),
            Plataforma(104, "HP 2100	"),
            Plataforma(105, "HP 3000	"),
            Plataforma(106, "SDS Sigma 7	"),
            Plataforma(107, "Call-A-Computer time-shared mainframe computer system	"),
            Plataforma(108, "PDP-11	"),
            Plataforma(109, "CDC Cyber 70	"),
            Plataforma(110, "PLATO	"),
            Plataforma(111, "Imlac PDS-1	"),
            Plataforma(112, "Microcomputer	"),
            Plataforma(113, "OnLive Game System	"),
            Plataforma(114, "Amiga CD32	"),
            Plataforma(115, "Apple IIGS	"),
            Plataforma(116, "Acorn Archimedes	"),
            Plataforma(117, "Philips CD-i	"),
            Plataforma(118, "FM Towns	"),
            Plataforma(119, "Neo Geo Pocket	"),
            Plataforma(120, "Neo Geo Pocket Color	"),
            Plataforma(121, "Sharp X68000	"),
            Plataforma(122, "Nuon	"),
            Plataforma(123, "WonderSwan Color	"),
            Plataforma(124, "SwanCrystal	"),
            Plataforma(125, "PC-8801	"),
            Plataforma(126, "TRS-80	"),
            Plataforma(127, "Fairchild Channel F	"),
            Plataforma(128, "PC Engine SuperGrafx	"),
            Plataforma(129, "Texas Instruments TI-99	"),
            Plataforma(130, "Nintendo Switch	"),
            Plataforma(131, "Nintendo PlayStation	"),
            Plataforma(132, "Amazon Fire TV	"),
            Plataforma(133, "Philips Videopac G7000	"),
            Plataforma(134, "Acorn Electron	"),
            Plataforma(135, "Hyper Neo Geo 64	"),
            Plataforma(136, "Neo Geo CD	"),
            Plataforma(137, "New Nintendo 3DS	"),
            Plataforma(138, "VC 4000	"),
            Plataforma(139, "1292 Advanced Programmable Video System	"),
            Plataforma(140, "AY-3-8500	"),
            Plataforma(141, "AY-3-8610	"),
            Plataforma(142, "PC-50X Family	"),
            Plataforma(143, "AY-3-8760	"),
            Plataforma(144, "AY-3-8710	"),
            Plataforma(145, "AY-3-8603	"),
            Plataforma(146, "AY-3-8605	"),
            Plataforma(147, "AY-3-8606	"),
            Plataforma(148, "AY-3-8607	"))
}