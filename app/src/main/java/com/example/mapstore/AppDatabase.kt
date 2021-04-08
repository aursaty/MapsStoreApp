package com.example.mapstore

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mapstore.dao.MapDao
import com.example.mapstore.entity.MapData


@Database(entities = [MapData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        //    @Volatile
        private var appDatabase: AppDatabase? = null

        //    @Synchronized
        fun getInstance(context: Context): AppDatabase? {
            if (appDatabase == null) appDatabase = create(context)
            return appDatabase
        }

        private const val DB_NAME = "Database.db"
        fun create(context: Context): AppDatabase? {
            return Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME).allowMainThreadQueries().build()
        }
    }

    abstract fun mapDao(): MapDao
}