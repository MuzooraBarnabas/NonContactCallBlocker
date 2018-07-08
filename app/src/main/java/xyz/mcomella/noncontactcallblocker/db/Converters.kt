package xyz.mcomella.noncontactcallblocker.db

import android.arch.persistence.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter fun fromTimestamp(timestamp: Long): Date = Date(timestamp)
    @TypeConverter fun dateToTimestamp(date: Date): Long = date.time
}