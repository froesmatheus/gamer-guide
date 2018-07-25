package com.matheusfroes.gamerguide.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.ui.calendario.CalendarioActivity
import com.matheusfroes.gamerguide.ui.configuracoes.ConfiguracoesActivity
import com.matheusfroes.gamerguide.ui.estatisticas.EstatisticasActivity
import com.matheusfroes.gamerguide.ui.feed.FeedActivity
import com.matheusfroes.gamerguide.ui.intro.IntroducaoActivity
import com.matheusfroes.gamerguide.ui.listas.ListasActivity
import com.matheusfroes.gamerguide.ui.meusjogos.MeusJogosActivity
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import kotlinx.android.synthetic.main.toolbar.*


open class BaseActivityDrawer : AppCompatActivity() {
    lateinit var drawer: Drawer
    private val NAVDRAWER_CLOSE_DELAY = 200L
    private val preferences: SharedPreferences by lazy {
        getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
    }

    companion object {
        val FEED_IDENTIFIER = 2L
        val LISTAS_IDENTIFIER = 3L
        val ESTATISTICAS_IDENTIFIER = 4L
        val CALENDARIO_IDENTIFIER = 1L
        val MEUS_JOGOS_IDENTIFIER = 0L
        val CONFIGURACOES_IDENTIFIER = 5L
        val AJUDA_IDENTIFIER = 6L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val appTheme = preferences.getString("APP_THEME", "DEFAULT")
        val theme = if (appTheme == "DEFAULT") R.style.AppTheme_NoActionBar else R.style.AppTheme_OLED
        setTheme(theme)
        super.onCreate(savedInstanceState)
    }

    fun configurarDrawer(): Drawer {
        val item1 = PrimaryDrawerItem().withIdentifier(MEUS_JOGOS_IDENTIFIER).withIconTintingEnabled(true).withIcon(R.drawable.ic_gamepad).withName(R.string.titulo_jogos)
        val item2 = PrimaryDrawerItem().withIdentifier(CALENDARIO_IDENTIFIER).withIconTintingEnabled(true).withIcon(R.drawable.ic_calendario).withName(R.string.titulo_calendario)
        val item3 = PrimaryDrawerItem().withIdentifier(FEED_IDENTIFIER).withIconTintingEnabled(true).withIcon(R.drawable.ic_folded_newspaper).withName(R.string.titulo_feed)
        val item4 = PrimaryDrawerItem().withIdentifier(LISTAS_IDENTIFIER).withIconTintingEnabled(true).withIcon(R.drawable.ic_list).withName(R.string.titulo_listas)
        val item5 = PrimaryDrawerItem().withIdentifier(ESTATISTICAS_IDENTIFIER).withIconTintingEnabled(true).withIcon(R.drawable.ic_estatistica).withName(R.string.titulo_estatiticas)
        val item6 = PrimaryDrawerItem().withIdentifier(CONFIGURACOES_IDENTIFIER).withIconTintingEnabled(true).withIcon(R.drawable.ic_configuracoes).withName(R.string.configuracoes)
        val item7 = PrimaryDrawerItem().withIdentifier(AJUDA_IDENTIFIER).withIconTintingEnabled(true).withIcon(R.drawable.ic_ajuda).withName(R.string.ajuda)


        drawer = DrawerBuilder()
                .withActivity(this)
                .withHeader(R.layout.header_drawer)
                .withToolbar(toolbar)
                .addDrawerItems(
                        item1,
                        item2,
                        item3,
                        item4,
                        item5,
                        DividerDrawerItem(),
                        item6
                ).build()

        drawer.setOnDrawerItemClickListener { _, _, drawerItem ->
            mudarTela(drawerItem.identifier)
            true
        }

        return drawer
    }

    fun setDrawerSelectedItem(identifier: Long) {
        drawer.setSelection(identifier)
    }

    private fun mudarTela(position: Long) {

        val intent = when (position) {
            FEED_IDENTIFIER -> {
                Intent(this, FeedActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            LISTAS_IDENTIFIER -> {
                Intent(this, ListasActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            ESTATISTICAS_IDENTIFIER -> {
                Intent(this, EstatisticasActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            CALENDARIO_IDENTIFIER -> {
                Intent(this, CalendarioActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            MEUS_JOGOS_IDENTIFIER -> {
                Intent(this, MeusJogosActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            CONFIGURACOES_IDENTIFIER -> {
                Intent(this, ConfiguracoesActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            AJUDA_IDENTIFIER -> {
                Intent(this, IntroducaoActivity::class.java)
            }
            else -> Intent(this, ListasActivity::class.java)
        }

        drawer.closeDrawer()

        Handler().postDelayed({
            startActivity(intent)
//            overridePendingTransition(R.anim.activity_fade_enter, R.anim.activity_fade_exit)
        }, NAVDRAWER_CLOSE_DELAY)
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen) {
            drawer.closeDrawer()
            return
        }

        super.onBackPressed()
    }
}
