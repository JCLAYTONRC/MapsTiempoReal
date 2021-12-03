package com.example.maps

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.maps.databinding.ActivityMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener{
    private lateinit var map:GoogleMap
    private lateinit var binding: ActivityMainBinding
    companion object{
        const val REQUEST_CODE_LOCATION = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createFragment()
    }

    private fun createFragment() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        createMarker()
        map.setOnMyLocationButtonClickListener (this)
        map.setOnMyLocationClickListener { this }
        enableLocation()
    }

    private fun createMarker() {
        val coordinates = LatLng(23.989693774873984, -104.65660934049053)
        val marker = MarkerOptions().position(coordinates).title("Mi Casa")
        map.addMarker(marker)
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates,18f),
            4000,null
        )
    }

    private fun isLocationPermissionsGranted() =
        ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED


    private fun enableLocation(){
        if(!::map.isInitialized) return
        if(isLocationPermissionsGranted()){
            //si
            map.isMyLocationEnabled = true
        }else{
            //no
            requestLocationPermission()

        }
    }
    private fun requestLocationPermission(){

        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){

            Toast.makeText(this, "Ve a ajustes y acepta los permisos", Toast.LENGTH_LONG).show()

        }else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when(requestCode){
            REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty()  && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                map.isMyLocationEnabled = true
            }else{
                Toast.makeText(this, "Para activar la localización ve a ajustes y acepta los permisos", Toast.LENGTH_LONG).show()

            }
            else
            ->{ }

        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if(!::map.isInitialized) return
        if(!isLocationPermissionsGranted()){
            map.isMyLocationEnabled = false
            Toast.makeText(this, "Para activar la localización ve a ajustes y acepta los permisos", Toast.LENGTH_LONG).show()

        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "Esta es tu ubicación", Toast.LENGTH_LONG).show()

        return false
    }

    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(this, "Esta es doende estoy ahora ${p0.latitude}, ${p0.longitude}", Toast.LENGTH_LONG).show()
    }
}