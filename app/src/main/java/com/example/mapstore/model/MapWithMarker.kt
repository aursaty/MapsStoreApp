//package com.example.mapstore.entity
//
//import androidx.room.Embedded
//import androidx.room.Entity
//import androidx.room.ForeignKey
//import androidx.room.ForeignKey.CASCADE
//import androidx.room.Relation
//
//@Entity(
//    foreignKeys = [ForeignKey(
//        entity = MapData::class,
//                parentColumns = ["id"],
//        childColumns = ["markerId"],
//        onDelete = CASCADE
//    )]
//)
//class MapWithMarker(
//    @Embedded var map: MapData = MapData(),
//    @Relation(
//        parentColumn = "id",
//        entityColumn = "markerId",
//        entity = MapData::class
//    )
//    var markers: List<MarkerData> = emptyList()
//)