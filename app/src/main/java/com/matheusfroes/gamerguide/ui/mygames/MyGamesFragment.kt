package com.matheusfroes.gamerguide.ui.mygames

import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.data.model.GameList
import com.matheusfroes.gamerguide.extra.*
import com.matheusfroes.gamerguide.ui.addgames.AddGamesActivity
import com.matheusfroes.gamerguide.ui.mygames.tabs.BeatenGamesFragment
import com.matheusfroes.gamerguide.ui.mygames.tabs.UnfinishedGamesFragment
import com.matheusfroes.gamerguide.ui.removegamedialog.RemoveGameDialog
import kotlinx.android.synthetic.main.activity_meus_jogos.*
import kotlinx.android.synthetic.main.toolbar_mygames.*
import kotlinx.android.synthetic.main.toolbar_mygames.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

class MyGamesFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MyGamesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.activity_meus_jogos, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appInjector.inject(this)
        setupToolbar()

        viewModel = viewModelProvider(viewModelFactory)

        val adapter = GamesAdapter(childFragmentManager)
        viewPager.adapter = adapter

        tabLayout.visibility = View.VISIBLE
        tabLayout.setupWithViewPager(viewPager)
        fab.setOnClickListener {
            startActivity(Intent(activity, AddGamesActivity::class.java))
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun removerJogoEvent(event: ExcluirJogoEvent) {
        openRemoveGameDialog(event.jogoId)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun gerenciarListasEvent(event: GerenciarListasEvent) {
        dialogGerenciarListas(event.jogoId)
    }

    override fun onStart() {
        super.onStart()

        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    private fun openRemoveGameDialog(gameId: Long) {
        val removeGameDialog = RemoveGameDialog.newInstance(gameId)
        removeGameDialog.show(childFragmentManager, "REMOVE_GAME_DIALOG")

        removeGameDialog.gameRemovedEvent = { gameRemoved ->
            if (gameRemoved) {
                toast(R.string.jogo_removido)
            } else {
                toast(R.string.game_removed_error)
            }
        }
    }

    private fun dialogGerenciarListas(jogoId: Long) {
        val listas = viewModel.getLists()
        val jogoJaCadastrado = mutableListOf<Boolean>()

        listas.forEach { lista ->
            if (viewModel.listContainsGame(jogoId, lista.id)) {
                jogoJaCadastrado.add(true)
            } else {
                jogoJaCadastrado.add(false)
            }
        }

        val listasStr = listas.map { it.toString() }.toTypedArray()

        val jogosAdicionarNaLista = mutableListOf<GameList>()
        val jogosRemoverDaLista = mutableListOf<GameList>()

        val dialog = AlertDialog.Builder(requireActivity())
                .setTitle(getString(R.string.gerenciar_listas))
                .setNegativeButton(getString(R.string.cancelar)) { _, _ -> }
                .setMultiChoiceItems(listasStr, jogoJaCadastrado.toBooleanArray()) { _, which: Int, isChecked: Boolean ->
                    if (isChecked && !jogoJaCadastrado[which]) {
                        jogosAdicionarNaLista.add(listas[which])
                    } else if (isChecked && jogoJaCadastrado[which]) {
                        jogosRemoverDaLista.remove(listas[which])
                    } else if (jogosAdicionarNaLista.contains(listas[which])) {
                        jogosAdicionarNaLista.remove(listas[which])
                    } else if (!isChecked && jogoJaCadastrado[which] && !jogosRemoverDaLista.contains(listas[which])) {
                        jogosRemoverDaLista.add(listas[which])
                    }
                }
                .setPositiveButton(getString(R.string.confirmar)) { _, _ ->
                    adicionarJogosLista(jogosAdicionarNaLista, jogoId)
                    removerJogosLista(jogosRemoverDaLista, jogoId)
                }
                .create()

        dialog.show()

    }

    private fun removerJogosLista(jogosRemoverDaLista: MutableList<GameList>, jogoId: Long) {
        jogosRemoverDaLista.forEach { lista ->
            viewModel.removeGameFromList(jogoId, lista.id)
        }

        if (jogosRemoverDaLista.size == 1) {
            requireActivity().toast(getString(R.string.msg_jogo_removido_lista))
        } else if (jogosRemoverDaLista.size > 1) {
            requireActivity().toast(getString(R.string.msg_jogo_removido_listas))
        }
    }

    private fun adicionarJogosLista(jogosAdicionarNaLista: MutableList<GameList>, jogoId: Long) {
        jogosAdicionarNaLista.forEach { lista ->
            viewModel.addGameToList(jogoId, lista.id)
        }
        if (jogosAdicionarNaLista.size == 1) {
            requireActivity().toast(getString(R.string.msg_jogo_adicionado_lista))
        } else if (jogosAdicionarNaLista.size > 1) {
            requireActivity().toast(getString(R.string.msg_jogo_adicionado_listas))
        }
    }

    private fun setupToolbar() {
        appCompatActivity.setSupportActionBar(toolbar)
        appCompatActivity.supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.toolbarTitle.text = "Jogos"
    }

    inner class GamesAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int) = GAMES_PAGES[position]()

        override fun getCount() = GAMES_TITLES.size

        override fun getPageTitle(position: Int): String = resources.getString(GAMES_TITLES[position])
    }

    companion object {
        private val GAMES_TITLES = arrayOf(
                R.string.nao_terminados,
                R.string.zerados
        )
        private val GAMES_PAGES = arrayOf(
                { UnfinishedGamesFragment() },
                { BeatenGamesFragment() }
        )
    }
}