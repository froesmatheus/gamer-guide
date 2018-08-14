package com.matheusfroes.gamerguide.ui.calendario

import android.arch.lifecycle.ViewModel
import com.matheusfroes.gamerguide.data.model.Release
import com.matheusfroes.gamerguide.data.source.remote.GameRemoteSource
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CalendarViewModel @Inject constructor(
        private val gameRemoteSource: GameRemoteSource
) : ViewModel() {
    private var nextPageId = ""

    fun getGameReleases(): Observable<List<Release>> {
        return gameRemoteSource.getGameReleases(nextPageId)
                .map {
                    nextPageId = it.second
                    it.first
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}