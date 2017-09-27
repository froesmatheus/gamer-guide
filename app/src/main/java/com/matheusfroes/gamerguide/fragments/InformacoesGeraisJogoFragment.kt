package com.matheusfroes.gamerguide.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.content_detalhes_jogo.view.*

/**
 * Created by matheus_froes on 26/09/2017.
 */
class InformacoesGeraisJogoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.content_detalhes_jogo, container, false)

        return view
    }
}