package com.matheusfroes.gamerguide.ui.feed

import android.arch.lifecycle.ViewModel
import com.matheusfroes.gamerguide.SingleLiveEvent
import com.matheusfroes.gamerguide.data.model.News
import com.matheusfroes.gamerguide.data.source.local.FeedLocalSource
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FeedViewModel @Inject constructor(private val source: FeedLocalSource) : ViewModel() {
    val news = SingleLiveEvent<List<News>>()

    fun refreshFeed() {
        Observable.fromArray(source.refreshFeed())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    news.postValue(it)
                }
    }
}