package com.matheusfroes.gamerguide.activities

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.matheusfroes.gamerguide.models.Jogo

/**
 * Created by matheusfroes on 29/09/2017.
 */
class DetalhesJogoViewModel : ViewModel() {
    var jogo = MutableLiveData<Jogo>()
}