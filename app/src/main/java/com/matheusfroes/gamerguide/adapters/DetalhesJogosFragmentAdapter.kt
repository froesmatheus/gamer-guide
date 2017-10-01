package com.matheusfroes.gamerguide.adapters

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.matheusfroes.gamerguide.fragments.InformacoesGeraisJogoFragment
import com.matheusfroes.gamerguide.fragments.MeuProgressoFragment
import com.matheusfroes.gamerguide.fragments.VideosFragment

/**
 * Created by matheus_froes on 26/09/2017.
 */
class DetalhesJogosFragmentAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {
    private val informacoesGerais = InformacoesGeraisJogoFragment()
    private val meuProgresso = MeuProgressoFragment()
    private val videos = VideosFragment()

    override fun getItem(position: Int) = when (position) {
        0 -> informacoesGerais
        1 -> videos
        2 -> meuProgresso
        else -> informacoesGerais
    }

    override fun getCount() = 3

    override fun getPageTitle(position: Int) = when (position) {
        0 -> "Informações Gerais"
        1 -> "Vídeos"
        2 -> "Meu Progresso"
        else -> ""
    }
}