package com.example.celulareport.db

import androidx.room.TypeConverter
import java.sql.Date
import java.util.*

class Converters {

    @TypeConverter
    fun stringToDate(string: String): Date{
        val (day, month, year) = string.split("\\")
        val date = Date.valueOf("$year-$month-$day")
        return date
    }

    @TypeConverter
    fun dateToString(date: Date) = date.toString()
}