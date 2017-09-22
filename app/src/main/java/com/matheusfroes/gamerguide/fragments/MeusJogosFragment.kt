package com.matheusfroes.gamerguide.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.activities.AdicionarJogosActivity
import com.matheusfroes.gamerguide.adapters.JogosFragmentAdapter
import kotlinx.android.synthetic.main.fab.view.*
import kotlinx.android.synthetic.main.fragment_meus_jogos.view.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Created by matheus_froes on 19/09/2017.
 */
class MeusJogosFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_meus_jogos, container, false)

        val adapter = JogosFragmentAdapter(childFragmentManager)
        view.viewPager.adapter = adapter

        activity.tabLayout.visibility = View.VISIBLE
        activity.tabLayout.setupWithViewPager(view.viewPager)

        view.fab.setOnClickListener {
            startActivity(Intent(activity, AdicionarJogosActivity::class.java))
        }

        return view
    }
}