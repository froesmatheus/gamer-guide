package com.matheusfroes.gamerguide.ui.detalhesjogo

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.matheusfroes.gamerguide.data.model.Game

class DetalhesJogoViewModel : ViewModel() {
    var jogo = MutableLiveData<Game>()
    val temaAtual = MutableLiveData<String>()
}