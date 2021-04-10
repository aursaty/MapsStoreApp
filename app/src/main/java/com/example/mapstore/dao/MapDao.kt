package com.example.mapstore.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mapstore.model.MapData

@Dao
interface MapDao {
    @Query("SELECT * FROM maps")
    fun getAll(): LiveData<List<MapData>> // list on youtube

    @Query("SELECT * FROM maps WHERE id =(:mapId)")
    fun getById(mapId: Int): MapData

    @Insert
    suspend fun insert(map: MapData)

    @Update
    fun update(map: MapData)

    @Query("DELETE FROM maps WHERE id =(:mapId)")
    fun delete(mapId: Int)
}