package com.exercise.cuanpah.ui.maps

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.exercise.cuanpah.data.ApiConfig
import com.exercise.cuanpah.data.OrderData
import com.exercise.cuanpah.data.OrderResponse
import com.exercise.cuanpah.data.UserPreference
import com.exercise.cuanpah.databinding.ActivityMapsBinding
import com.exercise.cuanpah.databinding.ActivityMapsStatusBinding
import com.exercise.cuanpah.ui.ViewModelFactory
import com.exercise.cuanpah.ui.home.HomeFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var binding_status :ActivityMapsStatusBinding
    private lateinit var searchView:SearchView
    private lateinit var mapsViewModel: MapsViewModel
    private var latGlobal :Double=0.0
    private var lonGlobal :Double=0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        var marker:Marker?=null
        super.onCreate(savedInstanceState)

        mapsViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), "")
        )[MapsViewModel::class.java]

        binding_status=ActivityMapsStatusBinding.inflate(layoutInflater)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()


        searchView = binding.searchLokasi

        val mapFragment = supportFragmentManager
            .findFragmentById(com.exercise.cuanpah.R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val location: String = searchView.query.toString()
                var addressList: List<Address>? = null

                val geocoder = Geocoder(this@MapsActivity)
                try {
                    addressList = geocoder.getFromLocationName(location, 1)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                if (addressList != null) {
                    if(addressList.isNotEmpty()){
                        val address = addressList[0]

                        marker?.remove()
                        val latLng = LatLng(address.latitude, address.longitude)
                        latGlobal=address.latitude
                        lonGlobal=address.longitude
                        marker=mMap.addMarker(MarkerOptions().position(latLng).title(location))
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                    }else{
                        Toast.makeText(this@MapsActivity, "Lokasi tidak ditemukan",Toast.LENGTH_SHORT).show()
                    }
                }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        binding.pesanKurirButton.setOnClickListener { order() }
    }

    private fun order(){
        var userId=0
        mapsViewModel.getUser().observe(this) {
            userId= it.id
        }
        val selectedTrash=binding.jenisSampahDropdown.selectedItem.toString()
        if (binding.beratInputText.text.toString().isEmpty()){
            Toast.makeText(this,"Masukkan berat sampah!",Toast.LENGTH_SHORT).show()
        }else{
            val inputWeight=binding.beratInputText.text.toString().toDouble()
            Toast.makeText(this@MapsActivity,"Mohon Menunggu",Toast.LENGTH_SHORT).show()

            if((latGlobal==0.0)||(lonGlobal==0.0)){
                Toast.makeText(this,"Masukkan Lokasi",Toast.LENGTH_SHORT).show()
            }else{
                val orderService=ApiConfig().getApiService().requestOrder(OrderData(userId,2,latGlobal,lonGlobal,"completed",inputWeight,selectedTrash))
                orderService.enqueue(object : Callback<OrderResponse>{
                    override fun onResponse(
                        call: Call<OrderResponse>,
                        response: Response<OrderResponse>
                    ) {
                        if(response.isSuccessful){
                            val responseBody=response.body()
                            if(responseBody!=null){
                                HomeFragment.ORDERED=true
                                MapsStatusActivity.LAT=latGlobal
                                MapsStatusActivity.LONG=lonGlobal
                                startActivity(Intent(this@MapsActivity,MapsStatusActivity::class.java))
                                MapsStatusActivity.DRIVERNAME=responseBody.data.driverName
                                MapsStatusActivity.DRIVERID=responseBody.data.driverId
                                MapsStatusActivity.STATUS="Ongoing"
                                MapsStatusActivity.PICKUPTIME=responseBody.data.pickup_time
                                finish()
                            }
                        }else{
                            Toast.makeText(this@MapsActivity,"responsenya gagal",Toast.LENGTH_SHORT).show()

                        }

                    }

                    override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                        Toast.makeText(this@MapsActivity,"Gagal",Toast.LENGTH_SHORT).show()
                    }

                })
            }

        }

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

        //Setting for dropdown menu
        val spinner: Spinner = findViewById(com.exercise.cuanpah.R.id.jenisSampahDropdown)
        ArrayAdapter.createFromResource(
            this,
            com.exercise.cuanpah.R.array.trash_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        //Setting hint for edit text
        binding.lokasiAndaPlaceholder.hint = "Cari lokasi Anda"
        binding.beratInputText.hint="Berat Sampah"


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true

    }

}