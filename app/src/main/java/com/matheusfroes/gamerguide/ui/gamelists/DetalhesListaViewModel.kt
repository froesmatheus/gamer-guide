package com.matheusfroes.gamerguide.ui.gamelists

import android.arch.lifecycle.ViewModel
import com.matheusfroes.gamerguide.data.model.GameList
import com.matheusfroes.gamerguide.data.source.local.GameListLocalSource
import javax.inject.Inject

class DetalhesListaViewModel @Inject constructor(private val gameListLocalSource: GameListLocalSource) : ViewModel() {
    fun getList(gameListId: Long): GameList {
        return gameListLocalSource.getList(gameListId)
    }

//    fun excluirLista(listaId: Int) {
//        dao.excluirLista(listaId)
//    }
}