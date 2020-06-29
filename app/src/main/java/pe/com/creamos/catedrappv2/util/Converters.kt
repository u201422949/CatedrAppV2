package pe.com.creamos.catedrappv2.util

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let {
            LocalDateTime.ofInstant(
                Instant.ofEpochMilli(value),
                ZoneOffset.of(ZONE_OFFSET)
            )
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? {
        return date?.atZone(ZoneOffset.of(ZONE_OFFSET))?.toInstant()?.toEpochMilli()
    }
}