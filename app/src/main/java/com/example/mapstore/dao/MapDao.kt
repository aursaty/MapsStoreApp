package com.example.mapstore.dao

import androidx.room.*
import com.example.mapstore.entity.MapData

@Dao
interface MapDao {
    @Query("SELECT * FROM maps")
    fun getAll(): List<MapData>

    @Query("SELECT * FROM maps WHERE id =(:mapId)")
    fun loadAllByIds(mapId: Int): MapData

    @Insert
    fun insert(map: MapData)

    @Update
    fun update(map: MapData)

    @Query("DELETE FROM maps WHERE id =(:mapId)")
    fun delete(mapId: Int)
}