package com.matheusfroes.gamer_guide.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamer_guide.adapters.MeusJogosAdapter
import com.matheusfroes.gamer_guide.R
import kotlinx.android.synthetic.main.fragment_meus_jogos.view.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Created by matheus_froes on 19/09/2017.
 */
class MeusJogosFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_meus_jogos, container, false)

        val adapter = MeusJogosAdapter(activity.supportFragmentManager)
        view.viewPager.adapter = adapter

        activity.tabLayout.visibility = View.VISIBLE
        activity.tabLayout.setupWithViewPager(view.viewPager)

        return view
    }
}