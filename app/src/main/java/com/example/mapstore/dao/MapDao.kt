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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(map: MapData)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(map: MapData)

    @Query("DELETE FROM maps WHERE id =(:mapId)")
    suspend fun deleteMap(mapId: Int)

    @Query("DELETE FROM maps")
    suspend fun deleteAllFromMaps()

}