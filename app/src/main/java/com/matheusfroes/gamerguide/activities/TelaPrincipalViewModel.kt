package com.matheusfroes.gamerguide.activities

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.matheusfroes.gamerguide.extra.AppRepository
import com.matheusfroes.gamerguide.models.Noticia
import org.jetbrains.anko.doAsync

/**
 * Created by matheusfroes on 29/09/2017.
 */
class TelaPrincipalViewModel(val app: Application) : AndroidViewModel(app) {
    var fragmentAtual = MutableLiveData<Int>().apply { value = 2 }
    var noticias = MutableLiveData<MutableList<Noticia>>()
    private val repository = AppRepository()

    fun atualizarFeed() {
        doAsync {
            val result = repository.atualizarFeed(app)
            noticias.postValue(result)
        }
    }
}