package com.matheusfroes.gamerguide.data.converters

import android.arch.persistence.room.TypeConverter
import java.util.*

class DateConverter {

    @TypeConverter
    fun fromDate(value: Date?): Long? {
        return value?.time
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

}