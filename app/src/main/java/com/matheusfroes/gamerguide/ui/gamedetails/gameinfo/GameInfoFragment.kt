package com.matheusfroes.gamerguide.ui.gamedetails.gameinfo

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.*
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.ui.gamedetails.GameDetailsViewModel
import kotlinx.android.synthetic.main.fragment_informacoes_gerais.*
import javax.inject.Inject


class GameInfoFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var userPreferences: UserPreferences
    private lateinit var viewModel: GameDetailsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        appInjector.inject(this)
        viewModel = activityViewModelProvider(viewModelFactory)

        val theme = userPreferences.getCurrentAppTheme()

        val context = ContextThemeWrapper(activity, theme)
        val localInflater = inflater.cloneInContext(context)
        val view = localInflater.inflate(R.layout.fragment_informacoes_gerais, container, false)

        viewModel.game.observe(this, Observer { game ->
            if (game != null) preencherDadosJogo(game)
        })

        return view
    }

    private fun preencherDadosJogo(game: Game) {
        tvNomeJogo.text = game.name
        tvNomeDesenvolvedores.text = game.developers
        tvNomePublicadora.text = game.publishers
        tvGenero.text = game.genres
        tvDataLancamento.text = game.releaseDate?.formatarData("dd 'de' MMMM 'de' yyyy")
        tvDescricao.text = game.description
        tvPlataformas.text = game.platforms.joinToString()
        tvGameEngine.text = game.gameEngine


        if (game.releaseDate == null) {
            tvDataLancamento.visibility = View.GONE
            tituloDataLancamento.visibility = View.GONE
        }

        if (game.description.isEmpty()) {
            cvDescricaoJogo.visibility = View.GONE
        }

        if (game.platforms.joinToString().isEmpty()) {
            tvTituloPlataformas.visibility = View.GONE
            tvPlataformas.visibility = View.GONE
        }

        if (game.gameEngine.isEmpty()) {
            tvTituloGameEngine.visibility = View.GONE
            tvGameEngine.visibility = View.GONE
        }

        if (game.developers.isEmpty()) {
            tvNomeDesenvolvedores.visibility = View.GONE
            tituloDesenvolvedores.visibility = View.GONE
        }

        if (game.publishers.isEmpty()) {
            tvNomePublicadora.visibility = View.GONE
            tituloPublicadoras.visibility = View.GONE
        }

        if (game.genres.isEmpty()) {
            tvGenero.visibility = View.GONE
            tvTituloGenero.visibility = View.GONE
        }

        cvDescricaoJogo.setOnClickListener { dialogDescricao(game) }

        btnZoom.setOnClickListener {
            val urlZoom = getString(R.string.url_zoom, "${game.publishers} ${game.name}")

            val customTabsIntent = CustomTabsIntent.Builder()
                    .setToolbarColor(ContextCompat.getColor(requireActivity(), R.color.cor_zoom))
                    .build()
            customTabsIntent.launchUrl(activity, Uri.parse(urlZoom))
        }

        btnBuscape.setOnClickListener {
            val urlZoom = getString(R.string.url_buscape, game.name.replace(" ", "-"))

            val customTabsIntent = CustomTabsIntent.Builder()
                    .setToolbarColor(ContextCompat.getColor(requireActivity(), R.color.cor_buscape))
                    .build()
            customTabsIntent.launchUrl(activity, Uri.parse(urlZoom))
        }

        val jogoParaPC = game.platforms.any { it.name == "PC" }

        if (jogoParaPC) {
            btnSteam.visibility = View.VISIBLE
            btnSteam.setOnClickListener {
                val urlZoom = getString(R.string.url_steam, "${game.publishers} ${game.name}")

                val customTabsIntent = CustomTabsIntent.Builder()
                        .setToolbarColor(ContextCompat.getColor(requireActivity(), R.color.cor_steam))
                        .build()
                customTabsIntent.launchUrl(activity, Uri.parse(urlZoom))
            }
        }



        if (game.timeToBeat != null) {
            if (game.timeToBeat.hastly == 0L) {
                tituloTTBSpeedrun.visibility = View.GONE
                tvTTBSpeedrun.visibility = View.GONE
            }
            if (game.timeToBeat.normally == 0L) {
                tituloTTBModoHistoria.visibility = View.GONE
                tvTTBModoHistoria.visibility = View.GONE
            }
            if (game.timeToBeat.completely == 0L) {
                tituloTTB100Perc.visibility = View.GONE
                tvTTB100Perc.visibility = View.GONE
            }
            tvTTBSpeedrun.text = requireContext().getString(R.string.horas_ttb, game.timeToBeat.hastly.div(3600))
            tvTTBModoHistoria.text = requireContext().getString(R.string.horas_ttb, game.timeToBeat.normally.div(3600))
            tvTTB100Perc.text = requireContext().getString(R.string.horas_ttb, game.timeToBeat.completely.div(3600))
        } else {
            cvCardTimeToBeat.visibility = View.GONE
        }
    }

    private fun dialogDescricao(jogo: Game?) {
        val dialog = AlertDialog.Builder(requireActivity())
                .setTitle(getString(R.string.descricao))
                .setMessage(jogo?.description)
                .create()

        dialog.show()
    }
}