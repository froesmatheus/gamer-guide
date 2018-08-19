package com.matheusfroes.gamerguide.ui

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.matheusfroes.gamerguide.data.models.Noticia
import com.matheusfroes.gamerguide.extra.AppRepository
import org.jetbrains.anko.doAsync

class TelaPrincipalViewModel(val app: Application) : AndroidViewModel(app) {
    private val repository = AppRepository()

    var noticias = MutableLiveData<MutableList<Noticia>>()

    fun atualizarFeed() {
        doAsync {
            val result = repository.atualizarFeed(app)
            noticias.postValue(result)
        }
    }
}