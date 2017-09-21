package com.matheusfroes.gamer_guide.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamer_guide.R
import com.matheusfroes.gamer_guide.adapters.JogosNaoTerminadosAdapter
import kotlinx.android.synthetic.main.activity_tela_principal.*
import kotlinx.android.synthetic.main.fragment_jogos_nao_terminados.view.*

/**
 * Created by matheusfroes on 20/09/2017.
 */
class JogosNaoTerminadosFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_jogos_nao_terminados, container, false)

        val adapter = JogosNaoTerminadosAdapter(activity)

        view.rvJogosNaoTerminados.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        view.rvJogosNaoTerminados.adapter = adapter

        view.rvJogosNaoTerminados.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0 && activity.bottomNavigation.isShown) {
                    activity.bottomNavigation.visibility = View.GONE
                } else if (dy < 0) {
                    activity.bottomNavigation.visibility = View.VISIBLE
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    activity.bottomNavigation.visibility = View.VISIBLE;
//                }

                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        return view
    }
}