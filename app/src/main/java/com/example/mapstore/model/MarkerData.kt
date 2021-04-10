package com.example.mapstore.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
class MarkerData(
    @PrimaryKey(autoGenerate = true) var markerId: Int = 0,
    @ColumnInfo var name: String = "",
    @ColumnInfo var lat: Double = 0.0,
    @ColumnInfo var long: Double = 0.0
)