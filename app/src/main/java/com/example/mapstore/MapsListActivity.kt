package com.example.mapstore

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mapstore.model.MapData
import com.example.mapstore.viewmodel.MapViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class MapsListActivity : AppCompatActivity() {
    companion object {
        const val ACTION_KEY = "ACTION_KEY"
        const val MAP_ID_KEY = "MAP_ID_KEY"
        const val MAP_NAME_KEY = "MAP_NAME_KEY"
        const val MARKERS_KEY = "MARKERS_KEY"
        const val MAP_DESCRIPTION_KEY = "MAP_DESCRIPTION_KEY"
        const val CREATE = "CREATE"
        const val UPDATE = "UPDATE"
    }


    private lateinit var emptyView: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var mapsAdapter: CustomAdapter
    private var mMapsList: MutableList<MapData> = emptyList<MapData>().toMutableList()

    private lateinit var mUserViewModel: MapViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps_list)


        // Setup FAB to open EditorActivity
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            openDialog()
        }

        recyclerView = findViewById(R.id.mapsRecyclerView)
        mapsAdapter = CustomAdapter(mMapsList)
        recyclerView.adapter = mapsAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

    }

    override fun onResume() {
        super.onResume()
        Log.d("MLA", "in onResume")
//            whenStarted {
//        val db = AppDatabase.getInstance(context = applicationContext)

        mUserViewModel = ViewModelProvider(this).get(MapViewModel::class.java)
        mUserViewModel.readAllData.observe(this, Observer { data ->
            mapsAdapter.addAll(data)
//            runOnUiThread { mapsAdapter.notifyDataSetChanged() }
            if (data.isNotEmpty()) {
//                emptyView.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        })

//        val mapsDbArray = db!!.mapDao().getAll().toTypedArray()

//        val a = AppDatabase.getAllAsyncTask(mapsDB = db!!).execute().get()
//        mMapsArray = a!!
//        recyclerViewAdapter.notifyDataSetChanged()
//        Log.d("MLA", mapsDbArray.toString())
//
//        if (mMapsArray.isEmpty()) {
//            recyclerView.visibility = View.GONE
//            emptyView.visibility = View.VISIBLE
//        } else {
//            recyclerView.visibility = View.VISIBLE
//            emptyView.visibility = View.VISIBLE
//
//            recyclerViewAdapter.notifyDataSetChanged()
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mapslist_activity_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.sort_maps_menu -> {
                return true
            }
            R.id.delete_maps_menu -> {
                deleteAllMaps()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllMaps() {
        val markerCreateBuilder = AlertDialog.Builder(this)

//        val dialogView = layoutInflater.inflate(R.layout.dialog_create_marker, null)
        markerCreateBuilder.setTitle("Are you sure want to delete all maps?")
        markerCreateBuilder.setPositiveButton("Delete") { dialogInterface, _ ->
            dialogInterface.cancel()
        }
        markerCreateBuilder.setNegativeButton("Cancel", null)
        val dialog = markerCreateBuilder.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener { v ->
            mUserViewModel.deleteAllFromMaps()
            dialog.cancel()
        }
    }

    private fun openDialog() {
        val createMapDialog = CreateMapDialog()
        createMapDialog.show(supportFragmentManager, "exx")
    }

    class CreateMapDialog : AppCompatDialogFragment() {
        private lateinit var mapNameEt: TextInputEditText
        private lateinit var mapDescriptionEt: TextInputEditText

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val builder = AlertDialog.Builder(activity)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            val dialogView = inflater.inflate(R.layout.dialog_create_map, null)
            builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton(
                    R.string.create_str
                ) { dialog, id ->
                    val mapName = mapNameEt.text.toString()
                    val mapDesc = mapDescriptionEt.text.toString()

                    if (mapName.isNotEmpty()) {
                        val intent = Intent(context, MapActivity::class.java)
                        intent.putExtra(ACTION_KEY, CREATE)
                        intent.putExtra(MAP_NAME_KEY, mapName)
                        intent.putExtra(MAP_DESCRIPTION_KEY, mapDesc)
                        startActivity(intent)
                    }
                }
                .setNegativeButton(
                    R.string.cancel_str
                ) { dialog, id ->
                    dialog.cancel()
                }
                .setTitle(R.string.create_map_str)
            mapNameEt = dialogView!!.findViewById(R.id.map_name_ev)
            mapDescriptionEt = dialogView.findViewById(R.id.description_ev)

            return builder.create()
        }
    }
}

