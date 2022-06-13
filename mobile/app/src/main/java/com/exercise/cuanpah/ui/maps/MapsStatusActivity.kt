package com.exercise.cuanpah.ui.maps

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.exercise.cuanpah.data.*
import com.exercise.cuanpah.databinding.ActivityMapsBinding
import com.exercise.cuanpah.databinding.ActivityMapsStatusBinding
import com.exercise.cuanpah.ui.ViewModelFactory
import com.exercise.cuanpah.ui.home.HomeFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MapsStatusActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding : ActivityMapsStatusBinding
    private lateinit var mMap: GoogleMap
    private lateinit var mapsViewModel: MapsViewModel
    private var userId=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()

        val mapFragment = supportFragmentManager
            .findFragmentById(com.exercise.cuanpah.R.id.mapCheck) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mapsViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), "")
        )[MapsViewModel::class.java]

        mapsViewModel.getUser().observe(this) {
            userId= it.id
        }

        val service=ApiConfig().getApiService().getOrder(userId)
        service.enqueue(object: Callback<OrderGetResponse> {
            override fun onResponse(
                call: Call<OrderGetResponse>,
                response: Response<OrderGetResponse>
            ) {
                if(response.isSuccessful){
                    val responseBody=response.body()
                    if(responseBody!=null){
                        for (item in responseBody.data){
                            if(item.driverId== DRIVERID){
                                STATUS=item.status
//                                Toast.makeText(this@MapsStatusActivity, item.status,Toast.LENGTH_SHORT).show()
                                if((item.status=="Completed") || (item.status=="completed")){
                                    HomeFragment.ORDERED=false
                                }
                                break
                            }
                        }

                    }
                }
            }

            override fun onFailure(call: Call<OrderGetResponse>, t: Throwable) {
                Toast.makeText(this@MapsStatusActivity,t.message,Toast.LENGTH_SHORT).show()
            }

        })

        binding.namaDriverPlaceholder.text= DRIVERNAME
        binding.pickUpTimePlaceholder.text= PICKUPTIME
        binding.statusPlaceholder.text= STATUS
    }

    override fun onResume() {
        super.onResume()
        mapsViewModel.getUser().observe(this) {
            userId= it.id
        }

        val service=ApiConfig().getApiService().getOrder(userId)
        service.enqueue(object: Callback<OrderGetResponse> {
            override fun onResponse(
                call: Call<OrderGetResponse>,
                response: Response<OrderGetResponse>
            ) {
                if(response.isSuccessful){
                    val responseBody=response.body()
                    if(responseBody!=null){
                        for (item in responseBody.data){
                            if(item.driverId== DRIVERID){
                                STATUS=item.status
                                if((item.status=="Completed") || (item.status=="completed")){
                                    HomeFragment.ORDERED=false
                                }
                                break
                            }
                        }

                    }
                }
            }

            override fun onFailure(call: Call<OrderGetResponse>, t: Throwable) {
                Toast.makeText(this@MapsStatusActivity,t.message,Toast.LENGTH_SHORT).show()
            }
        })
        binding.statusPlaceholder.text= STATUS

    }
    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val latLng = LatLng(LAT, LONG)
        mMap.addMarker(MarkerOptions().position(latLng))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
    }

    companion object{
        var DRIVERNAME="NAME"
        var DRIVERID=0
        var PICKUPTIME="TIME"
        var STATUS="STATUS"
        var LAT=0.0
        var LONG=0.0
    }
}