package com.matheusfroes.gamerguide.ui.gamedetails

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.matheusfroes.gamerguide.data.models.Jogo

class DetalhesJogoViewModel : ViewModel() {
    var jogo = MutableLiveData<Jogo>()
    val temaAtual = MutableLiveData<String>()
}