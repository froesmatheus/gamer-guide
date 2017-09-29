package com.matheusfroes.gamerguide.activities

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

/**
 * Created by matheusfroes on 29/09/2017.
 */
class TelaPrincipalViewModel : ViewModel() {
    var fragmentAtual = MutableLiveData<Int>().apply { value = 1 }
}