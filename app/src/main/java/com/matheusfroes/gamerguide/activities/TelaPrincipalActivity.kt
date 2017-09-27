package com.matheusfroes.gamerguide.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.api.IGDBService
import com.matheusfroes.gamerguide.fragments.FeedFragment
import com.matheusfroes.gamerguide.fragments.ListasFragment
import com.matheusfroes.gamerguide.fragments.MeusJogosFragment
import com.matheusfroes.gamerguide.models.GameResponse
import com.matheusfroes.gamerguide.normalizarDadosJogo
import kotlinx.android.synthetic.main.activity_tela_principal.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.doAsync
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TelaPrincipalActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_principal)
        setSupportActionBar(toolbar)

        bottomNavigation.setOnNavigationItemSelectedListener(this)

        bottomNavigation.selectedItemId = R.id.navMeusJogos

        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(IGDBService.URL_BASE)
                .build()

        val service = retrofit.create(IGDBService::class.java)

        val call = service.pesquisarJogos(query = "D")

        doAsync {
            val response = call.execute()

            if (response.isSuccessful) {
                guardarListaJogos(response.body())
            }
        }
    }

    private fun guardarListaJogos(jogos: List<GameResponse>?) {
        val listaJogos = jogos?.map {
            normalizarDadosJogo(it)
        }

        Log.d("GAMERGUIDE", listaJogos.toString())
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navMeusJogos -> {
                mudarTela(0)
                return true
            }
            R.id.navFeed -> {
                mudarTela(1)
                return true
            }
            R.id.navListas -> {
                mudarTela(2)
                return true
            }
        }
        return false
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
