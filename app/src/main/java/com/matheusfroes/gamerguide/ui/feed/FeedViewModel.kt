package com.matheusfroes.gamerguide.ui.feed

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.matheusfroes.gamerguide.extra.Result
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
        val cachedNews = newsLocalSource.getNewsCO()
        _feedState.value = Result.InProgress(cachedNews)

        val newsSources = newsLocalSource.getNewsSourcesByStatusCO(enabled = true)
        try {
            val news = newsSources
                    .map { newsSourceWebsite ->
                        newsRemoteSource.fetchFeed(newsSourceWebsite)
                    }
                    .flatten()
//                    .filter { news ->
//                        news.publishDate > newsLocalSource.getMostRecentNewsPublishDate()
//                    }
            newsLocalSource.saveNews(news)

            _feedState.value = Result.Complete(newsLocalSource.getNewsCO())
        } catch (e: Exception) {
            _feedState.value = Result.Error(e)
        }
    }
}