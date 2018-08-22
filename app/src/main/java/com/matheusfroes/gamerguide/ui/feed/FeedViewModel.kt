package com.matheusfroes.gamerguide.ui.feed

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.matheusfroes.gamerguide.Result
import com.matheusfroes.gamerguide.data.model.News
import com.matheusfroes.gamerguide.data.source.local.NewsLocalSource
import com.matheusfroes.gamerguide.data.source.remote.NewsRemoteSource
import com.matheusfroes.gamerguide.network.uiContext
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class FeedViewModel @Inject constructor(
        private val newsRemoteSource: NewsRemoteSource,
        private val newsLocalSource: NewsLocalSource
) : ViewModel() {
    private val _feedState = MutableLiveData<Result<List<News>>>()

    val feedState: LiveData<Result<List<News>>>
    get() = _feedState




    fun fetchNews() = launch(uiContext) {
        val newsSources = newsLocalSource.getNewsSourcesByStatusCO(enabled = true)

        val cachedNews = newsLocalSource.getNewsCO()
        _feedState.postValue(Result.InProgress(cachedNews))

        try {
            val news = newsSources
                    .map { newsSourceWebsite ->
                        newsRemoteSource.fetchFeed(newsSourceWebsite)
                    }
                    .flatten()
                    .filter { news ->
                        news.publishDate > newsLocalSource.getMostRecentNewsPublishDate()
                    }
            newsLocalSource.saveNews(news)

            _feedState.postValue(Result.Complete(newsLocalSource.getNewsCO()))
        } catch (e: Exception) {
            _feedState.postValue(Result.Error(e))
        }
    }
}