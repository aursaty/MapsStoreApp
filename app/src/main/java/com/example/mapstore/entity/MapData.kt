package com.example.mapstore.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "maps")
data class MapData(
    @PrimaryKey val id: Int,
    @ColumnInfo var name: String,
    @ColumnInfo var description: String,
    @ColumnInfo val createdDatetime: String
//    val pointsMap: List<MarkerDao>
)