package com.example.mapstore

import androidx.room.TypeConverter
import com.example.mapstore.model.MarkerData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class DataConverter {

    @TypeConverter
    fun fromCountryLangList(value: List<MarkerData>): String {
        val gson = Gson()
        val type = object : TypeToken<List<MarkerData>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toCountryLangList(value: String): List<MarkerData> {
        val gson = Gson()
        val type = object : TypeToken<List<MarkerData>>() {}.type
        return gson.fromJson(value, type)
    }
}