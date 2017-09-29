package com.matheusfroes.gamerguide.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.fragments.FeedFragment
import com.matheusfroes.gamerguide.fragments.ListasFragment
import com.matheusfroes.gamerguide.fragments.MeusJogosFragment
import kotlinx.android.synthetic.main.activity_tela_principal.*
import kotlinx.android.synthetic.main.toolbar.*

class TelaPrincipalActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private val viewModel: TelaPrincipalViewModel by lazy {
        ViewModelProviders.of(this).get(TelaPrincipalViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_principal)
        setSupportActionBar(toolbar)

        bottomNavigation.setOnNavigationItemSelectedListener(this)

        viewModel.fragmentAtual.observe(this, Observer { position ->
            mudarTela(position!!)
        })

        bottomNavigation.selectedItemId = R.id.navFeed
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navMeusJogos -> {
                viewModel.fragmentAtual.value = 0
            }
            R.id.navFeed -> {
                viewModel.fragmentAtual.value = 1
            }
            R.id.navListas -> {
                viewModel.fragmentAtual.value = 2
            }
        }
        return true
    }


    private fun mudarTela(position: Int) {

        val fragment = when (position) {
            0 -> MeusJogosFragment()
            1 -> FeedFragment()
            2 -> ListasFragment()
            else -> MeusJogosFragment()
        }
        supportFragmentManager.beginTransaction().replace(R.id.content, fragment).commit()
    }
}
