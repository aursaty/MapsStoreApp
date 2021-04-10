package com.example.mapstore.model

import androidx.room.*

@TypeConverters(MapData.DataConverter::class)
@Entity(tableName = "maps")
data class MapData(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo var name: String,
    @ColumnInfo var description: String,
    @ColumnInfo var createdDatetime: String,
    @ColumnInfo var pointsMap: List<MarkerData>
) {
    class DataConverter {

        companion object {
            @JvmStatic
            @TypeConverter
            fun fromCountryLangList(value: List<MarkerData>): String {
                val gson = com.google.gson.Gson()
                val type = object :
                    com.google.gson.reflect.TypeToken<List<MarkerData>>() {}.type
                return gson.toJson(value, type)
            }

            @JvmStatic
            @TypeConverter
            fun toCountryLangList(value: String): List<MarkerData> {
                val gson = com.google.gson.Gson()
                val type = object :
                    com.google.gson.reflect.TypeToken<List<MarkerData>>() {}.type
                return gson.fromJson(value, type)
            }
        }
    }
}