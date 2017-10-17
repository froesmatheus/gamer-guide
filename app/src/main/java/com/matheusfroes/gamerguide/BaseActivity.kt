package com.matheusfroes.gamerguide

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.matheusfroes.gamerguide.activities.*
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import kotlinx.android.synthetic.main.toolbar.*


/**
 * Created by matheus_froes on 17/10/2017.
 */

open class BaseActivity : AppCompatActivity() {
    lateinit var drawer: Drawer

    companion object {
        val FEED_IDENTIFIER = 2L
        val LISTAS_IDENTIFIER = 3L
        val ESTATISTICAS_IDENTIFIER = 4L
        val CALENDARIO_IDENTIFIER = 1L
        val MEUS_JOGOS_IDENTIFIER = 0L
    }

    fun configurarDrawer(): Drawer {
        val item1 = PrimaryDrawerItem().withIdentifier(MEUS_JOGOS_IDENTIFIER).withIcon(R.drawable.ic_gamepad).withName(R.string.titulo_jogos)
        val item2 = PrimaryDrawerItem().withIdentifier(CALENDARIO_IDENTIFIER).withIcon(R.drawable.ic_calendario).withName(R.string.titulo_calendario)
        val item3 = PrimaryDrawerItem().withIdentifier(FEED_IDENTIFIER).withIcon(R.drawable.ic_folded_newspaper).withName(R.string.titulo_feed)
        val item4 = PrimaryDrawerItem().withIdentifier(LISTAS_IDENTIFIER).withIcon(R.drawable.ic_list).withName(R.string.titulo_listas)
        val item5 = PrimaryDrawerItem().withIdentifier(ESTATISTICAS_IDENTIFIER).withIcon(R.drawable.ic_estatistica).withName(R.string.titulo_estatiticas)
        val item6 = PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.ic_configuracoes).withName(R.string.configuracoes)


        val headerResult = AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.colorPrimary)
                .build()

        drawer = DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(this)
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

        drawer.setOnDrawerItemClickListener { view, position, drawerItem ->
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
            else -> Intent(this, ListasActivity::class.java)
        }

        startActivity(intent)
        overridePendingTransition(R.anim.activity_fade_enter, R.anim.activity_fade_exit)
        drawer.closeDrawer()
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen) {
            drawer.closeDrawer()
            return
        }

        super.onBackPressed()
    }
}
