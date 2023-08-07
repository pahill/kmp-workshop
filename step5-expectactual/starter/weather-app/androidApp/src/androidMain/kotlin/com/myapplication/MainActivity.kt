package com.myapplication

import MainView
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            MainView()
        }
    }
}
//    }
//
//    private fun getLastKnownLocation(callback: (location.Location?) -> Unit) {
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            val locationPermissionRequest = registerForActivityResult(
//                ActivityResultContracts.RequestMultiplePermissions()
//            ) { permissions ->
//                fun getLocation(callback: (location.Location?) -> Unit) {
//                    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
//                        // Got last known location. In some rare situations this can be null.
//                        var convertedLocation: location.Location? = null
//                        location?.let {
//                            convertedLocation = location.Location(
//                                lat = location.latitude,
//                                long = location.longitude
//                            )
//                        }
//                        callback(convertedLocation)
//                    }
//                }
//                when {
//                    permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
//                        getLocation(callback)
//                    }
//                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
//                        getLocation(callback)
//                    }
//                    else -> {
//                        callback(null)
//                    }
//                }
//            }
//            locationPermissionRequest.launch(
//                arrayOf(
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                )
//            )
//        }
//    }
//}