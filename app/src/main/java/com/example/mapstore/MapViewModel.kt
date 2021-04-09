package com.example.mapstore

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.mapstore.entity.MapData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<MapData>>
    private val repository: MapRepository

    init {
        val userDao = AppDatabase.getInstance(application).mapDao()
        repository = MapRepository(userDao)
        readAllData = repository.readAllData
    }

    fun addMap(map: MapData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMap(map)
        }
    }
}
