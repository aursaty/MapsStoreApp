package com.example.mapstore

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mapstore.dao.MapDao
import com.example.mapstore.entity.MapData

@Database(entities = [MapData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mapDao(): MapDao
}