package com.example.mapstore.repository

import androidx.lifecycle.LiveData
import com.example.mapstore.dao.MapDao
import com.example.mapstore.model.MapData

class MapRepository(private val mapDao: MapDao) {

    val readAllData: LiveData<List<MapData>> = mapDao.getAll()

    suspend fun addMap(mapData: MapData) {
        mapDao.insert(mapData)
    }

    suspend fun updateMap(mapData: MapData) {
        mapDao.update(mapData)
    }

    suspend fun getMapById(id: Int): MapData {
        return mapDao.getById(id)
    }

    suspend fun deleteMapById(id: Int) {
        return mapDao.deleteMap(id)
    }

    suspend fun deleteAllFromMaps() {
        return mapDao.deleteAllFromMaps()
    }
}