package com.silvanoalbuquerque.mynotesapp.db

import androidx.room.TypeConverter
import com.silvanoalbuquerque.mynotesapp.other.Constants.DATABASE_DATE_TIME_PATTERN
import java.text.SimpleDateFormat
import java.util.*

class Converters {

    @TypeConverter
    fun toCalendar(dateStr: String?): Calendar? {
        return dateStr?.let {
            val dateFormat = SimpleDateFormat(
                DATABASE_DATE_TIME_PATTERN,
                Locale.getDefault()
            )
            val date = dateFormat.parse(dateStr)
            val resultCalendar = Calendar.getInstance()
            resultCalendar.time = date!!
            return resultCalendar
        }
    }

    @TypeConverter
    fun toString(date: Calendar?): String? {
        return date?.let {
            val dateFormat = SimpleDateFormat(
                DATABASE_DATE_TIME_PATTERN,
                Locale.getDefault()
            )

            return dateFormat.format(date.time)
        }
    }
}