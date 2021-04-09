package com.example.mapstore

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mapstore.entity.MapData
import com.example.mapstore.entity.MarkerData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class MapActivity : AppCompatActivity(), GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback {

    private lateinit var mMapName: String
    private lateinit var mMapDescription: String

    private lateinit var mMap: GoogleMap
    var mMarkerHashMap: HashMap<String, Marker> = HashMap(0)
    private lateinit var mAddMarkerLocationBt: Button

    private lateinit var mMapViewModel: MapViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val LOCATION_PERMISSION_REQUEST_CODE = 1234
    private val FILE_NAME = "content.txt"

    private var permissionDenied = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mAddMarkerLocationBt = findViewById(R.id.marker_my_location_bt)

        mMapName = intent.getStringExtra(MapsListActivity.MAP_NAME_KEY).toString()
        mMapDescription = intent.getStringExtra(MapsListActivity.MAP_DESCRIPTION_KEY).toString()

        title = "$mMapName's markers"


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mMapViewModel = ViewModelProvider(this).get(MapViewModel::class.java)

        mapFragment.getMapAsync(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_activity_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.save_map_menu -> {
                saveMarkers()
                return true
            }
            R.id.open_settings_menu -> {
                return true
            }
            R.id.clear_map_menu -> {
                clearMarkers()
                return true
            }
        }
        //headerView.setText(item.getTitle());
        //headerView.setText(item.getTitle());
        return super.onOptionsItemSelected(item)
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

        googleMap.setOnMapClickListener {

            addMarker(it)
        }

        googleMap.setOnInfoWindowClickListener {
            it.isVisible = false
            mMarkerHashMap.remove(it.title)
        }
    }

    private fun addMarker(latlng: LatLng) {
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
                dialogView.findViewById<TextInputEditText>(R.id.marker_name_et).text.toString()

            if (markerName.isNotEmpty()) {
                Toast.makeText(this, "Add a new Marker", Toast.LENGTH_SHORT).show()

                val newMarker = mMap.addMarker(
                    MarkerOptions().position(latlng).title(markerName)
                        .snippet("For remove click on text")
                )
                mMarkerHashMap[markerName] = newMarker
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng))
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow)
                // TODO saveMarker()

                dialog.cancel()
            } else {
                Toast.makeText(this, "Enter marker name!", Toast.LENGTH_SHORT).show()
            }
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

            setLocation()
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true
            Toast.makeText(this, "Location is disabled!", Toast.LENGTH_LONG).show()
//            // Permission to access the location is missing. Show rationale and request permission
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE
//            )
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */

    private fun checkLocationPermission(): Boolean {
        if (!::mMap.isInitialized) return false
        return if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            setLocation()
            true
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE
            )
            false
        }
    }

    @SuppressLint("MissingPermission")
    private fun setLocation() {
        mMap.setOnMyLocationButtonClickListener(this)
        mMap.setOnMyLocationClickListener(this)

        mMap.isMyLocationEnabled = true
        mAddMarkerLocationBt.isEnabled = true
    }

    override fun onMyLocationButtonClick(): Boolean {
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false
    }

    override fun onMyLocationClick(location: Location) {

    }

    private fun checkMyGpsStatus() {
        val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (gpsStatus) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            val s = locationManager.getLastKnownLocation("")
        } else {
            val intent1 = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent1)
        }
    }

    fun markerMyLocationClick(view: View) {
        checkLocationPermission()
        if (!permissionDenied)
            checkMyGpsStatus()
    }

    @SuppressLint("SimpleDateFormat")
    private fun saveMarkers() {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        val markerHashMap = mMarkerHashMap.mapValues {
            MarkerData(
                it.key,
                it.value.position.longitude,
                it.value.position.latitude
            )
        }.values
        val mapData = MapData(
            name = mMapName,
            description = mMapDescription,
            createdDatetime = currentDate
        )//, markerHashMap.toList())

        mMapViewModel.addMap(mapData)
        finish()
    }

    private fun clearMarkers() {
        mMarkerHashMap.mapValues { it.value.remove() }
        mMarkerHashMap = HashMap(0)
    }

}