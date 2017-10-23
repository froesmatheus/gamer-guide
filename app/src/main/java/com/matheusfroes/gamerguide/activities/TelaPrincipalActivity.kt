package com.matheusfroes.gamerguide.activities

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.matheusfroes.gamerguide.R
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import kotlinx.android.synthetic.main.toolbar.*


class TelaPrincipalActivity : AppCompatActivity() {
    private val viewModel: TelaPrincipalViewModel by lazy {
        ViewModelProviders.of(this).get(TelaPrincipalViewModel::class.java)
    }
    private val drawer: Drawer by lazy {
        montarDrawer()
    }

    private fun montarDrawer(): Drawer {
        val item1 = PrimaryDrawerItem().withIdentifier(0).withIcon(R.drawable.ic_joystick).withName(R.string.titulo_jogos)
        val item2 = PrimaryDrawerItem().withIdentifier(1).withIcon(R.drawable.ic_calendario).withName(R.string.titulo_calendario)
        val item3 = PrimaryDrawerItem().withIdentifier(2).withIcon(R.drawable.ic_folded_newspaper).withName(R.string.titulo_feed)
        val item4 = PrimaryDrawerItem().withIdentifier(3).withIcon(R.drawable.ic_list).withName(R.string.titulo_listas)
        val item5 = PrimaryDrawerItem().withIdentifier(4).withIcon(R.drawable.ic_estatistica).withName(R.string.titulo_estatiticas)
        val item6 = PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.ic_configuracoes).withName(R.string.configuracoes)


        val headerResult = AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.colorPrimary)
                .build()

        return DrawerBuilder()
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_principal)
        setSupportActionBar(toolbar)

        //bottomNavigation.selectTabAtPosition(viewModel.fragmentAtual.value!!)

        drawer.setOnDrawerItemClickListener { view, position, drawerItem ->
            //mudarTela(drawerItem.identifier.toInt())
            true
        }

        drawer.setSelection(2, true)


//        bottomNavigation.setOnTabSelectListener { item ->
//            val posicao = when (item) {
//                R.id.navMeusJogos -> 0
//                R.id.navCalendario -> 1
//                R.id.navFeed -> 2
//                R.id.navListas -> 3
//                R.id.navEstatisticas -> 4
//                else -> 0
//            }
//            mudarTela(posicao)
//        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun mudarTela(position: Int) {
        viewModel.fragmentAtual.value = position

//        val fragment = when (position) {
//            0 -> MeusJogosActivity()
//            else -> MeusJogosActivity()
//        }
//        supportFragmentManager.beginTransaction().replace(R.id.content, fragment).commit()
//        supportFragmentManager.executePendingTransactions()
//
//        drawer.closeDrawer()
    }
}
