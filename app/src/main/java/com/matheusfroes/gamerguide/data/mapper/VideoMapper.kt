package com.matheusfroes.gamerguide.data.mapper

import com.matheusfroes.gamerguide.data.model.Video
import com.matheusfroes.gamerguide.network.data.VideoResponse

class VideoMapper {
    companion object {
        fun map(videoResponse: VideoResponse, gameId: Long): Video {
            return Video(
                    name = videoResponse.nome,
                    videoId = videoResponse.videoId,
                    gameId = gameId
            )
        }

        fun map(videosResponse: List<VideoResponse>?, gameId: Long): List<Video> {
            return videosResponse.orEmpty().map { video -> map(video, gameId) }
        }
    }
}