package com.example.foodie_moodie_20

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodie_moodie_20.adapters.RestaurantsAdapter
import com.example.foodie_moodie_20.databinding.FragmentRestaurantsBinding

import com.example.foodie_moodie_20.utils.Constants.Companion.API_KEYmaps
import com.example.foodie_moodie_20.viewModels.RestaurantsViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.tasks.OnCompleteListener
import kotlinx.coroutines.launch


class RestaurantsFragment : Fragment(), LocationListener , OnMapReadyCallback {
    private var _binding: FragmentRestaurantsBinding?=null
    private lateinit var viewModel: RestaurantsViewModel
    private lateinit var feedAdapter: RestaurantsAdapter
    private lateinit var locationManager: LocationManager

    private  var latitude : String? = null
    private  var longitude : String? = null

    private lateinit var mMap: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
          //  getLocation()
        when {
            isFineLocationGranted() -> {
                when {
                    isLocationEnabled() -> getLocation()
                    else -> showGPSNotEnableDialog()
                }
            }
            else -> requestAccessFineLocation()

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding= FragmentRestaurantsBinding.inflate(inflater, container, false)
        viewModel= ViewModelProvider(this).get(RestaurantsViewModel::class.java)


        feedAdapter = RestaurantsAdapter()

        _binding?.feedRecyclerView?.layoutManager= LinearLayoutManager(requireContext())
        _binding?.feedRecyclerView?.adapter=feedAdapter
        showShimmerEffect()

        _binding?.onGoogleMa?.setOnClickListener {
            if( latitude !=null && longitude!=null){

                val strUri = "https://www.google.com/maps/search/Restaurants/@" +latitude + ","+longitude+",15z/data=!3m1!4b1"
                Log.i("MAPS URI", strUri)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))

                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
                startActivity(intent)

            }

        }
        return _binding?.root
    }

    override fun onResume() {
        super.onResume()
        when {
            isFineLocationGranted() -> {
                showShimmerEffect()
                when {

                    isLocationEnabled() -> getLocation()
                   // else -> showGPSNotEnableDialog()
                }
            }
            //else -> requestAccessFineLocation()
        }
    }

    private fun getLocation() {

        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        val locationRequest = LocationRequest()
            .setInterval(20000000) //Milliseconds
            .setFastestInterval(20000000)
            .setSmallestDisplacement(1f)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        if (checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
            object : LocationCallback() {
                @SuppressLint("MissingPermission")
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {
                        val current = LatLng(location.latitude, location.longitude)
                        latitude = location.latitude.toString()
                        longitude = location.longitude.toString()

                        if (::mMap.isInitialized) {
                            mMap.addMarker(MarkerOptions().position(current).title("Marker in Current Are"))
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(current))
                        }
                    }
                    lifecycleScope.launch {

                        var locationstring : String = latitude + " , " + longitude
                        Log.i("Lati", locationstring)

                        viewModel.getRestrauByCurrentLocation(locationstring, 10000, "restaurant", API_KEYmaps)

                        viewModel.restaurantResponseCurrentLocation.observe({ lifecycle }) {
                            hideShimmerEffect()
                            feedAdapter.submitList(it)
                        }

                    }

                    println(latitude)
                    println(longitude)
                }

            },
            Looper.myLooper()
        )

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            999 -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                when {
                    isLocationEnabled() -> getLocation()
                    else -> showGPSNotEnableDialog()
                }
            } else {
                Toast.makeText(requireContext(),
                    "Permission Not Granted", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    fun isLocationEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun showGPSNotEnableDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Enable GPS")
            .setMessage("GPS is required for Google Maps")
            .setCancelable(false)
            .setPositiveButton("Enable Now") { dialogInterface: DialogInterface, i: Int ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                dialogInterface.dismiss()
            }
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            }).show()}

    fun isFineLocationGranted(): Boolean {
        return activity?.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

     fun requestAccessFineLocation() {
        this.requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            999
        )
    }

    override fun onLocationChanged(location: Location) {

    }


    private fun showShimmerEffect(){
        _binding?.feedRecyclerView?.showShimmer()
    }

    private fun hideShimmerEffect(){
       _binding?.feedRecyclerView?.hideShimmer()
    }

    override fun onMapReady(p0: GoogleMap) {

    }


}