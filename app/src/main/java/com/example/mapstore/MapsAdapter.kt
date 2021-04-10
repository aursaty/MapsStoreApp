package com.example.mapstore

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mapstore.model.MapData

class CustomAdapter(private val mapDataList: MutableList<MapData>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

//    private var mapList = emptyList<MapData>()

    fun add(mapData: MapData) {
        mapDataList.add(mapData)
        this.notifyDataSetChanged()
    }

    fun addAll(mapDataList: List<MapData>) {
        this.mapDataList.clear()
        this.mapDataList.addAll(mapDataList)
        this.notifyDataSetChanged()
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var mapNameView: TextView
        lateinit var datetimeView: TextView

        init {
            // Define click listener for the ViewHolder's View.
            mapNameView = view.findViewById(R.id.map_name_tv)
            datetimeView = view.findViewById(R.id.edit_datetime_tv)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.maps_list_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val currentMap = mapDataList[position]
        viewHolder.mapNameView.text = currentMap.name
        viewHolder.datetimeView.text = currentMap.createdDatetime

        viewHolder.itemView.setOnClickListener {
            val context = viewHolder.itemView.context
            val intent = Intent(context, MapActivity::class.java)

            intent.putExtra(MapsListActivity.ACTION_KEY, MapsListActivity.UPDATE)
            intent.putExtra(MapsListActivity.MAP_ID_KEY, currentMap.id)
            intent.putExtra(MapsListActivity.MAP_NAME_KEY, currentMap.name)
            intent.putExtra(MapsListActivity.MAP_DESCRIPTION_KEY, currentMap.description)
            intent.putExtra(MapsListActivity.MARKERS_KEY, MapData.DataConverter.fromCountryLangList(currentMap.pointsMap))
            startActivity(context, intent, null)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = mapDataList.size

}
