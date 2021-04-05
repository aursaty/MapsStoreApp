package com.example.mapstore

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MapsListActivity : AppCompatActivity() {
    companion object {
        val MAP_NAME_KEY = "MAP_NAME_KEY"
        val MAP_DESCRIPTION_KEY = "MAP_DESCRIPTION_KEY"
    }

    private lateinit var mapsListView: ListView
    private lateinit var emptyView: View
    private lateinit var listViewAdapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps_list)

        // Setup FAB to open EditorActivity

        // Setup FAB to open EditorActivity
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            openDialog()
        }

        listViewAdapter = ArrayAdapter(this, R.layout.maps_list_item, Array(0) {})

        mapsListView = findViewById(R.id.lvPets)
        emptyView = findViewById(R.id.empty_view)
    }

    private fun openDialog() {
        val createMapDialog = CreateMapDialog()
        createMapDialog.show(supportFragmentManager, "exx")
    }

    class CreateMapDialog : AppCompatDialogFragment() {
        private lateinit var mapNameEt: EditText
        private lateinit var mapDescriptionEt: EditText

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val builder = AlertDialog.Builder(activity)
            // Get the layout inflater
            val inflater = activity!!.layoutInflater

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            val dialogView = inflater.inflate(R.layout.dialog_create_map, null)
            builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton(R.string.create_str
                ) { dialog, id ->
                    val mapName = mapNameEt.text.toString()
                    val mapDesc = mapDescriptionEt.text.toString()

                    if (mapName.isNotEmpty()) {
                        val intent = Intent(context, MapActivity::class.java)
                        intent.putExtra(MAP_NAME_KEY, mapName)
                        intent.putExtra(MAP_DESCRIPTION_KEY, mapDesc)
                        startActivity(intent)
                    }
                }
                .setNegativeButton(R.string.cancel_str
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