package com.matheusfroes.gamerguide.ui.feed

import android.support.v7.util.DiffUtil
import com.matheusfroes.gamerguide.data.model.News

class NewsDiff : DiffUtil.ItemCallback<News>() {
    override fun areItemsTheSame(oldItem: News, newItem: News) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: News, newItem: News) = oldItem.id == newItem.id
}