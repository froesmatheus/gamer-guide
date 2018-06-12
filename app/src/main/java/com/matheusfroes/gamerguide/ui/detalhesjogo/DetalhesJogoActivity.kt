package com.matheusfroes.gamerguide.ui.detalhesjogo

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import com.matheusfroes.gamerguide.JogoAdicionadoRemovidoEvent
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.data.db.JogosDAO
import com.matheusfroes.gamerguide.data.db.ListasDAO
import com.matheusfroes.gamerguide.data.model.FormaCadastro
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.obterImagemJogoCapa
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detalhes_jogo.*
import kotlinx.android.synthetic.main.dialog_remover_jogo.view.*
import org.greenrobot.eventbus.EventBus


class DetalhesJogoActivity : AppCompatActivity() {
    private val viewModel: DetalhesJogoViewModel by lazy {
        ViewModelProviders.of(this).get(DetalhesJogoViewModel::class.java)
    }
    private val jogosDAO: JogosDAO by lazy {
        JogosDAO(this)
    }
    private val preferences: SharedPreferences by lazy {
        getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
    }
    var jogoSalvo = false

    override fun onCreate(savedInstanceState: Bundle?) {
        val appTheme = preferences.getString("APP_THEME", "DEFAULT")
        val theme = if (appTheme == "DEFAULT") R.style.AppTheme_DetalhesJogo else R.style.AppTheme_DetalhesJogo_OLED
        setTheme(theme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_jogo)
        setSupportActionBar(toolbar)

        viewModel.temaAtual.value = preferences.getString("APP_THEME", "DEFAULT")

        appBar.post {
            val heightPx = ivCapaJogo.height
            setAppBarOffset(heightPx / 2)
        }


        intent ?: return

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        val jogo: Game
        var jogoId = intent.getLongExtra("id_jogo", 0L)
        jogo = if (jogoId != 0L) {
            jogosDAO.obterJogo(jogoId)!!
        } else {
            intent.getSerializableExtra("jogo") as Game
        }
        jogoId = jogo.id
        jogoSalvo = jogosDAO.obterJogoPorFormaCadastro(jogo.id) != null
        if (jogoSalvo) {
            fabAdicinarJogo.setImageResource(R.drawable.ic_adicionado)
        } else {
            fabAdicinarJogo.setImageResource(R.drawable.ic_adicionar)
        }

        viewModel.jogo.value = jogo


        val tabAdapter = DetalhesJogosFragmentAdapter(this, supportFragmentManager)
        viewPager.adapter = tabAdapter
        tabLayout.setupWithViewPager(viewPager)
        viewPager.setCurrentItem(0, false)

        if (viewModel.jogo.value?.videos?.size == 0) {
            tabAdapter.removeTabPage(1)
        }

        if (jogo.coverImage.isNotEmpty()) {
            Picasso
                    .with(this)
                    .load(obterImagemJogoCapa(jogo.coverImage))
                    .into(ivCapaJogo)
        } else {
            appBar.setExpanded(false, false)
        }

        if (jogoSalvo) {
            fabAdicinarJogo.setImageResource(R.drawable.ic_adicionado)
        }

        fabAdicinarJogo.setOnClickListener {

            if (jogoSalvo) {
                dialogRemoverJogo(jogoId)
            } else {
                val snackbar = Snackbar.make(coordinatorLayout, getString(R.string.jogo_adicionado), Snackbar.LENGTH_LONG)
                jogosDAO.inserir(jogo)
                jogoSalvo = true
                EventBus.getDefault().postSticky(JogoAdicionadoRemovidoEvent())
                fabAdicinarJogo.setImageResource(R.drawable.ic_adicionado)
                snackbar.show()
            }

        }
    }

    private fun dialogRemoverJogo(jogoId: Long) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_remover_jogo, null, false)

        val listasDAO = ListasDAO(this)
        val dialog = AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton(getString(R.string.confirmar)) { _, _ ->
                    val removerDasListas = view.chkRemoverDasListas.isChecked

                    val jogo = jogosDAO.obterJogoPorFormaCadastro(jogoId)

                    if (removerDasListas) {
                        listasDAO.removerJogoTodasListas(jogoId)
                        jogosDAO.remover(jogoId)
                    } else {
                        jogo?.formaCadastro = FormaCadastro.CADASTRO_POR_LISTA
                        jogosDAO.atualizar(jogo!!)
                    }

                    jogoSalvo = false
                    EventBus.getDefault().postSticky(JogoAdicionadoRemovidoEvent())
                    fabAdicinarJogo.setImageResource(R.drawable.ic_adicionar)
                    val snackbar = Snackbar.make(coordinatorLayout, getString(R.string.jogo_removido), Snackbar.LENGTH_LONG)
                    snackbar.show()
                }
                .setNegativeButton(getString(R.string.cancelar), null)
                .create()

        dialog.show()
    }

    private fun setAppBarOffset(offsetPx: Int) {
        val params = appBar.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior as AppBarLayout.Behavior?
        behavior!!.onNestedPreScroll(coordinatorLayout, appBar, toolbar, 0, offsetPx, intArrayOf(0, 0), 0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
