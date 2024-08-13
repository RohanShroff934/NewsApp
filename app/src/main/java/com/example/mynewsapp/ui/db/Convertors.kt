package com.example.mynewsapp.ui.db

import androidx.room.TypeConverter
import com.example.mynewsapp.ui.models.Source

class Convertors {
    @TypeConverter
    fun fromSource(source: Source):String{
        return source.name
    }
    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}