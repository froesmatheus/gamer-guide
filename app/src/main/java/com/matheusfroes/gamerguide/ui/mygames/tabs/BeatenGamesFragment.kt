package com.matheusfroes.gamerguide.ui.mygames.tabs

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.extra.*
import com.matheusfroes.gamerguide.ui.gameprogressdialog.GameProgressDialog
import com.matheusfroes.gamerguide.ui.mygames.MyGamesAdapter
import com.matheusfroes.gamerguide.ui.mygames.MyGamesViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_jogos_nao_terminados.view.*
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject


class BeatenGamesFragment : Fragment() {
    val adapter: MyGamesAdapter by lazy { MyGamesAdapter() }
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MyGamesViewModel

    private val subscriptions = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appInjector.inject(this)

        viewModel = activityViewModelProvider(viewModelFactory)

        view.rvJogosNaoTerminados.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        view.rvJogosNaoTerminados.adapter = adapter

        subscriptions += viewModel.getBeatenGames().subscribe { games ->
            view.rvJogosNaoTerminados.emptyView = view.layoutEmpty
            adapter.jogos = games
        }


        adapter.setOnMenuItemClickListener(object : MyGamesAdapter.OnMenuOverflowClickListener {
            override fun onMenuItemClick(menu: MenuItem, game: Game) {
                when (menu.itemId) {
                    R.id.navRemover -> {
                        EventBus.getDefault().post(ExcluirJogoEvent(game.id))
                    }
                    R.id.navGerenciarListas -> {
                        EventBus.getDefault().post(GerenciarListasEvent(game.id))
                    }
                    R.id.navAtualizarProgresso -> {
                        openGameProgressDialog(game)
                    }
                    R.id.navMarcarComoNaoTerminado -> {
                        game.progress.beaten = false
                        viewModel.updateGameProgress(game)
                        requireContext().toast(getString(R.string.msg_jogo_movido_nao_terminados))
                    }
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_jogos_nao_terminados, container, false)

    private fun openGameProgressDialog(game: Game) {
        val gameProgressDialog = GameProgressDialog.newInstance(game)
        gameProgressDialog.show(childFragmentManager, "GAME_PROGRESS_DIALOG")
    }

    override fun onDestroy() {
        super.onDestroy()

        subscriptions.dispose()
    }
}