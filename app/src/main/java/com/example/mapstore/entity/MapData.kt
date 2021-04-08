package com.example.mapstore.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "maps")
data class MapData(
    @PrimaryKey var id: Int,
    @ColumnInfo var name: String,
    @ColumnInfo var description: String,
    @ColumnInfo var createdDatetime: String
//    @ColumnInfo val pointsMap: Array<MarkerData>
)