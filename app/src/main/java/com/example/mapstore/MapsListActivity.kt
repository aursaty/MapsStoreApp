package com.example.mapstore

import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.CursorAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MapsListActivity : AppCompatActivity() {

    private lateinit var mapsListView : ListView
    private lateinit var emptyView : View
    private lateinit var listViewAdapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps_list)

        listViewAdapter = ArrayAdapter(this, R.layout.maps_list_item, Array(0) {})

        mapsListView = findViewById(R.id.lvPets)
        emptyView = findViewById(R.id.empty_view)
    }
}