package com.matheusfroes.gamerguide.data.mapper

import com.matheusfroes.gamerguide.data.model.TimeToBeat
import com.matheusfroes.gamerguide.network.data.TimeToBeatResponse

class TimeToBeatMapper {
    companion object {
        fun map(timeToBeatResponse: TimeToBeatResponse?): TimeToBeat? {
            return if (timeToBeatResponse == null) {
                null
            } else {
                return TimeToBeat(
                        hastly = timeToBeatResponse.hastly,
                        normally = timeToBeatResponse.normally,
                        completely = timeToBeatResponse.completely)
            }
        }
    }
}