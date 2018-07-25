package com.matheusfroes.gamerguide.ui.gamedetail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
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
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.activity
import com.matheusfroes.gamerguide.context
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.formatarData
import kotlinx.android.synthetic.main.fragment_informacoes_gerais.view.*
import kotlinx.android.synthetic.main.fragment_meu_progresso.view.*
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar
import org.jetbrains.anko.toast


class InformacoesGeraisJogoFragment : Fragment() {
    private val viewModel: DetalhesJogoViewModel by lazy {
        ViewModelProviders.of(activity()).get(DetalhesJogoViewModel::class.java)
    }
    private val progressosDAO: ProgressoDAO by lazy {
        ProgressoDAO(context())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val theme = if (viewModel.temaAtual.value == "DEFAULT") R.style.AppTheme_NoActionBar else R.style.AppTheme_OLED

        val context = ContextThemeWrapper(activity, theme)
        val localInflater = inflater.cloneInContext(context)
        val view = localInflater.inflate(R.layout.fragment_informacoes_gerais, container, false)

        viewModel.jogo.observe(this, Observer { jogo ->
            preencherDadosJogo(view, jogo)
        })

        return view
    }

    private fun preencherDadosJogo(view: View, jogo: Game?) {
        view.tvNomeJogo.text = jogo?.name
        view.tvNomeDesenvolvedores.text = jogo?.developers
        view.tvNomePublicadora.text = jogo?.publishers
        view.tvGenero.text = jogo?.genres
        view.textview.text = jogo?.releaseDate?.formatarData("dd 'de' MMMM 'de' yyyy")
        view.tvDescricao.text = jogo?.description
        view.tvPlataformas.text = jogo?.platforms?.joinToString()
        view.tvGameEngine.text = jogo?.gameEngine

        if (jogo?.description.isNullOrEmpty()) {
            view.cvDescricaoJogo.visibility = View.GONE
        }

        if (jogo?.platforms?.joinToString().isNullOrEmpty()) {
            view.tvTituloPlataformas.visibility = View.GONE
            view.tvPlataformas.visibility = View.GONE
        }

        if (jogo?.gameEngine.isNullOrEmpty()) {
            view.tvTituloGameEngine.visibility = View.GONE
            view.tvGameEngine.visibility = View.GONE
        }

        if (jogo?.developers.isNullOrEmpty()) {
            view.tvNomeDesenvolvedores.visibility = View.GONE
            view.tituloDesenvolvedores.visibility = View.GONE
        }

        if (jogo?.publishers.isNullOrEmpty()) {
            view.tvNomePublicadora.visibility = View.GONE
            view.tituloPublicadoras.visibility = View.GONE
        }

        if (jogo?.genres.isNullOrEmpty()) {
            view.tvGenero.visibility = View.GONE
            view.tvTituloGenero.visibility = View.GONE
        }

        view.cvDescricaoJogo.setOnClickListener { dialogDescricao() }

        view.btnZoom.setOnClickListener {
            val urlZoom = getString(R.string.url_zoom, jogo?.name)

            val customTabsIntent = CustomTabsIntent.Builder()
                    .setToolbarColor(ContextCompat.getColor(activity(), R.color.cor_zoom))
                    .build()
            customTabsIntent.launchUrl(activity, Uri.parse(urlZoom))
        }

        view.btnBuscape.setOnClickListener {
            val urlZoom = getString(R.string.url_buscape, jogo?.name?.replace(" ", "-"))

            val customTabsIntent = CustomTabsIntent.Builder()
                    .setToolbarColor(ContextCompat.getColor(activity(), R.color.cor_buscape))
                    .build()
            customTabsIntent.launchUrl(activity, Uri.parse(urlZoom))
        }

        if (jogo?.platforms != null) {
            val jogoParaPC = jogo.platforms.any { it.name == "PC" }

            if (jogoParaPC) {
                view.btnSteam.visibility = View.VISIBLE
                view.btnSteam.setOnClickListener {
                    val urlZoom = getString(R.string.url_steam, jogo.name)

                    val customTabsIntent = CustomTabsIntent.Builder()
                            .setToolbarColor(ContextCompat.getColor(activity(), R.color.cor_steam))
                            .build()
                    customTabsIntent.launchUrl(activity, Uri.parse(urlZoom))
                }
            }
        }


        if (jogo?.timeToBeat != null) {
            if (jogo.timeToBeat.hastly == 0L) {
                view.tituloTTBSpeedrun.visibility = View.GONE
                view.tvTTBSpeedrun.visibility = View.GONE
            }
            if (jogo.timeToBeat.normally == 0L) {
                view.tituloTTBModoHistoria.visibility = View.GONE
                view.tvTTBModoHistoria.visibility = View.GONE
            }
            if (jogo.timeToBeat.completely == 0L) {
                view.tituloTTB100Perc.visibility = View.GONE
                view.tvTTB100Perc.visibility = View.GONE
            }
            view.tvTTBSpeedrun.text = context().getString(R.string.horas_ttb, jogo.timeToBeat.hastly.div(3600))
            view.tvTTBModoHistoria.text = context().getString(R.string.horas_ttb, jogo.timeToBeat.normally.div(3600))
            view.tvTTB100Perc.text = context().getString(R.string.horas_ttb, jogo.timeToBeat.completely.div(3600))
        } else {
            view.cvCardTimeToBeat.visibility = View.GONE
        }
    }

    private fun dialogDescricao() {
        val dialog = AlertDialog.Builder(activity())
                .setTitle(getString(R.string.descricao))
                .setMessage(viewModel.jogo.value?.description)
                .create()

        dialog.show()
    }

    private fun dialogAtualizarProgresso(jogoId: Long) {
        val progressoJogo = progressosDAO.obterProgressoPorJogo(jogoId)!!

        val view = LayoutInflater.from(context).inflate(R.layout.fragment_meu_progresso, null, false)

        view.sbProgresso.setOnProgressChangeListener(object : DiscreteSeekBar.OnProgressChangeListener {
            override fun onProgressChanged(seekBar: DiscreteSeekBar?, value: Int, fromUser: Boolean) {
                view.tvPorcentagemProgresso.text = "$value%"
                view.chkJogoZerado.isChecked = value == 100
            }

            override fun onStartTrackingTouch(seekBar: DiscreteSeekBar?) {}

            override fun onStopTrackingTouch(seekBar: DiscreteSeekBar?) {}

        })

        view.etHorasJogadas.setText("${progressoJogo.hoursPlayed}")
        view.sbProgresso.progress = progressoJogo.percentage
        view.chkJogoZerado.isChecked = progressoJogo.beaten


        val dialog = AlertDialog.Builder(context())
                .setView(view)
                .setPositiveButton(getString(R.string.atualizar)) { _, _ ->
                    var horasJogadasStr = view.etHorasJogadas.text.toString().trim()
                    horasJogadasStr = if (horasJogadasStr.isEmpty()) "0" else horasJogadasStr

                    progressoJogo.hoursPlayed = Integer.parseInt(horasJogadasStr)
                    progressoJogo.percentage = view.sbProgresso.progress
                    progressoJogo.beaten = view.chkJogoZerado.isChecked

                    progressosDAO.atualizarProgresso(progressoJogo, jogoId)

                    context().toast(getString(R.string.progresso_atualizado))
                }
                .setNegativeButton(getString(R.string.cancelar), null)
                .create()

        dialog.show()
    }
}