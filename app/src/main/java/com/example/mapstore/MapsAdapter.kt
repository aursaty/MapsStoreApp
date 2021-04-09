package com.example.mapstore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mapstore.entity.MapData

class CustomAdapter(private val mapDataList: MutableList<MapData>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

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
        viewHolder.mapNameView.text = mapDataList[position].name
        viewHolder.datetimeView.text = mapDataList[position].createdDatetime
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = mapDataList.size

}
