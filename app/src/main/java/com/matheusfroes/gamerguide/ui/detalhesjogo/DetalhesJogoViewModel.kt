package com.matheusfroes.gamerguide.ui.detalhesjogo

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.matheusfroes.gamerguide.data.models.Jogo

class DetalhesJogoViewModel : ViewModel() {
    var jogo = MutableLiveData<Jogo>()
    val temaAtual = MutableLiveData<String>()
}