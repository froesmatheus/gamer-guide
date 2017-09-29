package com.matheusfroes.gamerguide.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.matheusfroes.gamerguide.models.Plataforma

/**
 * Created by matheus_froes on 28/09/2017.
 */
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
        val JOGOS_DATA_LANCAMENTO = "data_lascamento"
        val JOGOS_IMAGEM_CAPA = "imagem_capa"


        // Tabela de Plataformas
        val TABELA_PLATAFORMAS = "plataformas"
        val PLATAFORMAS_ID = "_id"
        val PLATAFORMAS_NOME = "nome"


        // Tabela ligação Jogos/Plataformas
        val TABELA_JOGOS_PLATAFORMAS = "jogos_plataformas"
        val JOGOS_PLATAFORMAS_ID = "_id"
        val JOGOS_PLATAFORMAS_ID_JOGO = "id_jogo"
        val JOGOS_PLATAFORMAS_ID_PLATAFORMA = "id_plataforma"

        // Tabela de Listas
        val TABELA_LISTAS = "listas"
        val LISTAS_ID = "_id"
        val LISTAS_NOME = "nome"

        // Tabela ligação Listas/Jogos
        val TABELA_LISTAS_JOGOS = "listas_jogos"
        val LISTAS_JOGOS_ID = "_id"
        val LISTAS_JOGOS_ID_LISTA = "id_lista"
        val LISTAS_JOGOS_ID_JOGO = "id_jogo"

        val CREATE_TABLE_JOGOS = """
             CREATE TABLE $TABELA_JOGOS(
                $JOGOS_ID INTEGER PRIMARY KEY,
                $JOGOS_NOME TEXT,
                $JOGOS_DESCRICAO TEXT,
                $JOGOS_DESENVOLVEDORAS TEXT,
                $JOGOS_PUBLICADORAS INTEGER,
                $JOGOS_GENEROS TEXT,
                $JOGOS_DATA_LANCAMENTO INTEGER,
                $JOGOS_IMAGEM_CAPA TEXT);"""

        val CREATE_TABLE_PLATAFORMAS = """
            CREATE TABLE $TABELA_PLATAFORMAS(
                $PLATAFORMAS_ID INTEGER PRIMARY KEY,
                $PLATAFORMAS_NOME TEXT);"""

        val CREATE_TABLE_JOGOS_PLATAFORMAS = """
            CREATE TABLE $TABELA_JOGOS_PLATAFORMAS(
                $JOGOS_PLATAFORMAS_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                $JOGOS_PLATAFORMAS_ID_JOGO INTEGER NOT NULL,
                $JOGOS_PLATAFORMAS_ID_PLATAFORMA INTEGER NOT NULL,
                FOREIGN KEY($JOGOS_PLATAFORMAS_ID_JOGO) REFERENCES $TABELA_JOGOS($JOGOS_ID),
                FOREIGN KEY($JOGOS_PLATAFORMAS_ID_PLATAFORMA) REFERENCES $TABELA_PLATAFORMAS($PLATAFORMAS_ID));"""

        val CREATE_TABLE_LISTAS = """
            CREATE TABLE $TABELA_LISTAS(
                $LISTAS_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                $LISTAS_NOME TEXT NOT NULL);"""

        val CREATE_TABLE_LISTAS_JOGOS = """
            CREATE TABLE $TABELA_LISTAS_JOGOS(
                $LISTAS_JOGOS_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                $LISTAS_JOGOS_ID_JOGO INTEGER NOT NULL,
                $LISTAS_JOGOS_ID_LISTA INTEGER NOT NULL,
                FOREIGN KEY($LISTAS_JOGOS_ID_JOGO) REFERENCES $TABELA_JOGOS($JOGOS_ID),
                FOREIGN KEY($LISTAS_JOGOS_ID_LISTA) REFERENCES $TABELA_LISTAS($LISTAS_ID));"""
    }


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_JOGOS)
        db.execSQL(CREATE_TABLE_PLATAFORMAS)
        db.execSQL(CREATE_TABLE_JOGOS_PLATAFORMAS)
        db.execSQL(CREATE_TABLE_LISTAS)
        db.execSQL(CREATE_TABLE_LISTAS_JOGOS)

        inserirPlataformas(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE $TABELA_JOGOS_PLATAFORMAS IF EXISTS;")
        db.execSQL("DROP TABLE $TABELA_LISTAS_JOGOS IF EXISTS;")
        db.execSQL("DROP TABLE $TABELA_LISTAS IF EXISTS;")
        db.execSQL("DROP TABLE $TABELA_JOGOS IF EXISTS;")
        db.execSQL("DROP TABLE $TABELA_PLATAFORMAS IF EXISTS;")
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

    private fun getPlataformas() = listOf(
            Plataforma(3, "Linux	"),
            Plataforma(4, "Nintendo 64	"),
            Plataforma(5, "Wii	"),
            Plataforma(6, "PC"),
            Plataforma(7, "PlayStation	"),
            Plataforma(8, "PlayStation 2	"),
            Plataforma(9, "PlayStation 3	"),
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