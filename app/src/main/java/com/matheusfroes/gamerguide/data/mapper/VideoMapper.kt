package com.matheusfroes.gamerguide.data.mapper

import com.matheusfroes.gamerguide.data.model.Video
import com.matheusfroes.gamerguide.network.data.VideoResponse

class VideoMapper {
    companion object {
        fun map(videoResponse: VideoResponse): Video {
            return Video(
                    name = videoResponse.nome,
                    videoId = videoResponse.videoId
            )
        }

        fun map(videosResponse: List<VideoResponse>?): List<Video> {
            return videosResponse.orEmpty().map { video -> map(video) }
        }
    }
}