package com.matheusfroes.gamerguide.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import kotlinx.android.synthetic.main.fragment_meu_progresso.view.*
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar

/**
 * Created by matheus_froes on 26/09/2017.
 */
class MeuProgressoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_meu_progresso, container, false)

        view.sbProgresso.setOnProgressChangeListener(object : DiscreteSeekBar.OnProgressChangeListener {
            override fun onProgressChanged(seekBar: DiscreteSeekBar?, value: Int, fromUser: Boolean) {
                view.tvPorcentagemProgresso.text = "$value%"
            }

            override fun onStartTrackingTouch(seekBar: DiscreteSeekBar?) {}

            override fun onStopTrackingTouch(seekBar: DiscreteSeekBar?) {}

        })


        return view
    }
}