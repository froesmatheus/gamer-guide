package com.matheusfroes.gamerguide.data.converters

import android.arch.persistence.room.TypeConverter
import com.matheusfroes.gamerguide.data.model.InsertType

class InsertTypeEnumConverter {

    @TypeConverter
    fun from(value: InsertType?): String? {
        return value?.name
    }

    @TypeConverter
    fun to(value: String?): InsertType? {
        return value?.let { InsertType.valueOf(value) }
    }

}