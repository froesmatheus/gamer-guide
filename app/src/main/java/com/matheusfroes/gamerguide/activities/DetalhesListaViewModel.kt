package com.matheusfroes.gamerguide.activities

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.matheusfroes.gamerguide.db.ListasDAO
import com.matheusfroes.gamerguide.models.Lista

/**
 * Created by matheusfroes on 29/09/2017.
 */
class DetalhesListaViewModel(application: Application) : AndroidViewModel(application) {
    private val dao: ListasDAO by lazy {
        ListasDAO(application)
    }

    private var lista = MutableLiveData<Lista>()

    fun getLista(listaId: Int): Lista? = dao.obterLista(listaId)

    fun excluirLista(listaId: Int) {
        dao.excluirLista(listaId)
    }
}