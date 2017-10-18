package com.matheusfroes.gamerguide.fragments

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
import com.matheusfroes.gamerguide.activities.DetalhesJogoViewModel
import com.matheusfroes.gamerguide.db.ProgressoDAO
import com.matheusfroes.gamerguide.formatarData
import com.matheusfroes.gamerguide.models.Jogo
import kotlinx.android.synthetic.main.fragment_informacoes_gerais.view.*
import kotlinx.android.synthetic.main.fragment_meu_progresso.view.*
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar
import org.jetbrains.anko.toast


/**
 * Created by matheus_froes on 26/09/2017.
 */
class InformacoesGeraisJogoFragment : Fragment() {
    private val viewModel: DetalhesJogoViewModel by lazy {
        ViewModelProviders.of(activity).get(DetalhesJogoViewModel::class.java)
    }
    private val progressosDAO: ProgressoDAO by lazy {
        ProgressoDAO(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_informacoes_gerais, container, false)

        viewModel.jogo.observe(this, Observer { jogo ->
            preencherDadosJogo(view, jogo)
        })

        return view
    }

    private fun preencherDadosJogo(view: View, jogo: Jogo?) {
        view.tvNomeJogo.text = jogo?.nome
        view.tvNomeDesenvolvedores.text = jogo?.desenvolvedores
        view.tvNomePublicadora.text = jogo?.publicadoras
        view.tvGenero.text = jogo?.generos
        view.textview.text = jogo?.dataLancamento?.formatarData("dd 'de' MMMM 'de' yyyy")
        view.tvDescricao.text = jogo?.descricao
        view.tvPlataformas.text = jogo?.plataformas?.joinToString()
        view.tvGameEngine.text = jogo?.gameEngine

        if (jogo?.descricao.isNullOrEmpty()) {
            view.cvDescricaoJogo.visibility = View.GONE
        }

        if (jogo?.plataformas?.joinToString().isNullOrEmpty()) {
            view.tvTituloPlataformas.visibility = View.GONE
            view.tvPlataformas.visibility = View.GONE
        }

        if (jogo?.gameEngine.isNullOrEmpty()) {
            view.tvTituloGameEngine.visibility = View.GONE
            view.tvGameEngine.visibility = View.GONE
        }

        if (jogo?.desenvolvedores.isNullOrEmpty()) {
            view.tvNomeDesenvolvedores.visibility = View.GONE
            view.tituloDesenvolvedores.visibility = View.GONE
        }

        if (jogo?.publicadoras.isNullOrEmpty()) {
            view.tvNomePublicadora.visibility = View.GONE
            view.tituloPublicadoras.visibility = View.GONE
        }

        if (jogo?.generos.isNullOrEmpty()) {
            view.tvGenero.visibility = View.GONE
            view.tvTituloGenero.visibility = View.GONE
        }

        view.cvDescricaoJogo.setOnClickListener { dialogDescricao() }

//        view.btnMeuProgresso.setOnClickListener {
//            dialogAtualizarProgresso(jogo?.id!!)
//        }

        view.btnZoom.setOnClickListener {
            val urlZoom = getString(R.string.url_zoom, jogo?.nome)

            val customTabsIntent = CustomTabsIntent.Builder()
                    .setToolbarColor(ContextCompat.getColor(activity, R.color.cor_zoom))
                    .build()
            customTabsIntent.launchUrl(activity, Uri.parse(urlZoom))
        }

        view.btnBuscape.setOnClickListener {
            val urlZoom = getString(R.string.url_buscape, jogo?.nome?.replace(" ", "-"))

            val customTabsIntent = CustomTabsIntent.Builder()
                    .setToolbarColor(ContextCompat.getColor(activity, R.color.cor_buscape))
                    .build()
            customTabsIntent.launchUrl(activity, Uri.parse(urlZoom))
        }

        if (jogo?.plataformas != null) {
            val jogoParaPC = jogo.plataformas.any { it.nome == "PC" }

            if (jogoParaPC) {
                view.btnSteam.visibility = View.VISIBLE
                view.btnSteam.setOnClickListener {
                    val urlZoom = getString(R.string.url_steam, jogo.nome)

                    val customTabsIntent = CustomTabsIntent.Builder()
                            .setToolbarColor(ContextCompat.getColor(activity, R.color.cor_steam))
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
            view.tvTTBSpeedrun.text = context.getString(R.string.horas_ttb, jogo.timeToBeat.hastly.div(3600))
            view.tvTTBModoHistoria.text = context.getString(R.string.horas_ttb, jogo.timeToBeat.normally.div(3600))
            view.tvTTB100Perc.text = context.getString(R.string.horas_ttb, jogo.timeToBeat.completely.div(3600))
        } else {
            view.cvCardTimeToBeat.visibility = View.GONE
        }
    }

    private fun dialogDescricao() {
        val dialog = AlertDialog.Builder(activity)
                .setTitle(getString(R.string.descricao))
                .setMessage(viewModel.jogo.value?.descricao)
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

        view.etHorasJogadas.setText("${progressoJogo.horasJogadas}")
        view.sbProgresso.progress = progressoJogo.progressoPerc
        view.chkJogoZerado.isChecked = progressoJogo.zerado


        val dialog = AlertDialog.Builder(context)
                .setView(view)
                .setPositiveButton(getString(R.string.atualizar)) { dialogInterface, i ->
                    var horasJogadasStr = view.etHorasJogadas.text.toString().trim()
                    horasJogadasStr = if (horasJogadasStr.isEmpty()) "0" else horasJogadasStr

                    progressoJogo.horasJogadas = Integer.parseInt(horasJogadasStr)
                    progressoJogo.progressoPerc = view.sbProgresso.progress
                    progressoJogo.zerado = view.chkJogoZerado.isChecked

                    progressosDAO.atualizarProgresso(progressoJogo, jogoId)

                    context.toast(getString(R.string.progresso_atualizado))
                }
                .setNegativeButton(getString(R.string.cancelar), null)
                .create()

        dialog.show()
    }
}