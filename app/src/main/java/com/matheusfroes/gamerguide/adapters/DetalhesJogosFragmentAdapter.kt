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
    private val tabItems = mutableListOf(
            MeuProgressoFragment(), InformacoesGeraisJogoFragment(), VideosFragment()
    )
    private val tabTitles = mutableListOf(
            "Progresso",
            "Informações",
            "Vídeos"
    )

    override fun getItem(position: Int) = tabItems[position]

    override fun getCount() = tabItems.size

    override fun getPageTitle(position: Int) = tabTitles[position]

    fun removeTabPage(position: Int) {
        if (!tabItems.isEmpty() && position < tabItems.size) {
            tabItems.removeAt(position)
            tabTitles.removeAt(position)
            notifyDataSetChanged()
        }
    }
}