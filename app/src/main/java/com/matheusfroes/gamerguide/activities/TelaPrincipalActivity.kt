package com.matheusfroes.gamerguide.activities

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.fragments.*
import kotlinx.android.synthetic.main.activity_tela_principal.*
import kotlinx.android.synthetic.main.toolbar.*

class TelaPrincipalActivity : AppCompatActivity() {
    private val viewModel: TelaPrincipalViewModel by lazy {
        ViewModelProviders.of(this).get(TelaPrincipalViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_principal)
        setSupportActionBar(toolbar)

        bottomNavigation.selectTabAtPosition(viewModel.fragmentAtual.value!!)

        bottomNavigation.setOnTabSelectListener { item ->
            val posicao = when (item) {
                R.id.navMeusJogos -> 0
                R.id.navCalendario -> 1
                R.id.navFeed -> 2
                R.id.navListas -> 3
                R.id.navEstatisticas -> 4
                else -> 0
            }
            mudarTela(posicao)
        }
    }

    private fun mudarTela(position: Int) {
        viewModel.fragmentAtual.value = position

        val fragment = when (position) {
            0 -> MeusJogosFragment()
            1 -> CalendarioFragment()
            2 -> FeedFragment()
            3 -> ListasFragment()
            4 -> EstatisticasFragment()
            else -> EstatisticasFragment()
        }
        supportFragmentManager.beginTransaction().replace(R.id.content, fragment).commit()
        supportFragmentManager.executePendingTransactions()
    }
}
