package com.example.mapstore

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity() : AppCompatActivity(), GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback, Parcelable {

    private lateinit var mMap: GoogleMap
    private lateinit var mMarkerHashMap: HashMap<String, Marker>

    private lateinit var mapNameTv: TextView
    private lateinit var mapDescriptionTv: TextView


    private val LOCATION_PERMISSION_REQUEST_CODE = 1234

    private var permissionDenied = false

    constructor(parcel: Parcel) : this() {
        permissionDenied = parcel.readByte() != 0.toByte()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapNameTv = findViewById(R.id.map_name_tv)
        mapDescriptionTv = findViewById(R.id.modified_datetime_tv)


        val mapName = intent.getStringExtra(MapsListActivity.MAP_NAME_KEY)
        val mapDescription = intent.getStringExtra(MapsListActivity.MAP_DESCRIPTION_KEY)

        mapNameTv.text = mapName
        mapDescriptionTv.text = mapDescription

        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("InflateParams")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        checkLocationEnabled()

        googleMap.setOnMyLocationButtonClickListener(this)
        googleMap.setOnMyLocationClickListener(this)

        googleMap.setOnMapClickListener() {
            lateinit var markerName: String

            val markerCreateBuilder = AlertDialog.Builder(this)

            val dialogView = layoutInflater.inflate(R.layout.dialog_create_marker, null)
            markerCreateBuilder.setView(dialogView)
            markerCreateBuilder.setTitle("Add a new point")
            markerCreateBuilder.setPositiveButton("Add") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            markerCreateBuilder.setNegativeButton("Cancel", null)
            val dialog = markerCreateBuilder.show()

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener { v ->

                markerName =
                    dialogView.findViewById<EditText>(R.id.marker_name_et).text.toString()

                if (markerName.isNotEmpty()) {
                    Toast.makeText(this, "Add a new Marker", Toast.LENGTH_SHORT).show()

                    val newMarker = mMap.addMarker(MarkerOptions().position(it).title(markerName))
                    mMarkerHashMap[markerName] = newMarker
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(it))

                    // TODO saveMarker()

                    dialog.cancel()
                } else {
                    Toast.makeText(this, "Enter marker name!", Toast.LENGTH_SHORT).show()
                }
            }

            // Add a marker in Sydney and move the camera
//            val sydney = LatLng(-34.0, 151.0)
//            val marker: Marker = mMap.addMarker(
//                MarkerOptions().position(sydney)
//                    .title("Marker in Sydney")
//            )
            mMarkerHashMap = HashMap(0)
//            mMarkerList.add(marker)
        }

        googleMap.setOnInfoWindowClickListener {
            it.isVisible = false
            mMarkerHashMap.remove(it.title)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return
        }
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Enable the my location layer if the permission has been granted.
            mMap.isMyLocationEnabled = true
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true
            Toast.makeText(this, "Location is disabled!", Toast.LENGTH_LONG).show()
            // Permission to access the location is missing. Show rationale and request permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE
            )
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */

    private fun checkLocationEnabled() {
        if (!::mMap.isInitialized) return
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        checkLocationEnabled()
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show()
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false
    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(this, "Current location:\n$location", Toast.LENGTH_LONG).show()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (permissionDenied) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MapActivity> {
        override fun createFromParcel(parcel: Parcel): MapActivity {
            return MapActivity(parcel)
        }

        override fun newArray(size: Int): Array<MapActivity?> {
            return arrayOfNulls(size)
        }
    }


}