package com.matheusfroes.gamerguide.ui.listas

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.matheusfroes.gamerguide.data.db.ListasDAO
import com.matheusfroes.gamerguide.data.models.Lista

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