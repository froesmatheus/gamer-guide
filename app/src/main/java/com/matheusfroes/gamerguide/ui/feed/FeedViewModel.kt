package com.matheusfroes.gamerguide.ui.feed

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.matheusfroes.gamerguide.Result
import com.matheusfroes.gamerguide.data.model.News
import com.matheusfroes.gamerguide.data.source.local.NewsLocalSource
import com.matheusfroes.gamerguide.data.source.remote.NewsRemoteSource
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class FeedViewModel @Inject constructor(
        private val newsRemoteSource: NewsRemoteSource,
        private val newsLocalSource: NewsLocalSource
) : ViewModel() {

    init {
        fetchNews()
    }

    val feedState = MutableLiveData<Result<List<News>>>()

    fun getNews(): Observable<Result<List<News>>> {
        val enabledNewsSources = newsLocalSource.getNewsSourcesByStatus(enabled = true)
        return Observable.fromArray(enabledNewsSources)
                .startWith { Result.InProgress(newsLocalSource.getNews()) }
                .flatMapIterable { it }
                .flatMap { newsSource -> newsRemoteSource.getRssFeed(newsSource) }
                .map { newsLocalSource.saveNews(it) }
                .map<Result<List<News>>> { Result.Complete(newsLocalSource.getNews()) }
                .onErrorReturn { Result.Error(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    }


    private fun fetchNews() = launch(UI) {
        val newsSources = newsLocalSource.getNewsSourcesByStatusCO(enabled = true).await()

        val cachedNews = newsLocalSource.getNewsCO().await()
        feedState.postValue(Result.InProgress(cachedNews))

        try {
            newsSources.map { newsSourceWebsite ->
                val rssFeed = newsRemoteSource.fetchRssFeed(newsSourceWebsite).await()

                newsLocalSource.saveNews(rssFeed)
            }
        } catch (e: Exception) {
            feedState.postValue(Result.Error(e))
        }

        val news = newsLocalSource.getNewsCO().await()
        feedState.postValue(Result.Complete(news))
    }
}