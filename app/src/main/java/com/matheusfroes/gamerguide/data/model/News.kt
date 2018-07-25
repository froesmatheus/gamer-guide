package com.matheusfroes.gamerguide.data.model

data class News(
        val title: String,
        val image: String,
        val url: String,
        val publishDate: Long,
        val website: String) {
    override fun equals(other: Any?): Boolean {
        return if (other !is News) {
            false
        } else {
            other.title == this.title
        }
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + image.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + publishDate.hashCode()
        result = 31 * result + website.hashCode()
        return result
    }
}
