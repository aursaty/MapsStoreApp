package com.example.mapstore

import androidx.lifecycle.LiveData
import com.example.mapstore.dao.MapDao
import com.example.mapstore.entity.MapData

class MapRepository(private val mapDao: MapDao) {

    val readAllData: LiveData<List<MapData>> = mapDao.getAll()

    suspend fun addMap(mapData: MapData) {
        mapDao.insert(mapData)
    }
}