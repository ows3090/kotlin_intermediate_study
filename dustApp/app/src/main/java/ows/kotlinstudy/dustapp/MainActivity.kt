package ows.kotlinstudy.dustapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import ows.kotlinstudy.dustapp.data.Repository
import ows.kotlinstudy.dustapp.data.models.airquality.Grade
import ows.kotlinstudy.dustapp.data.models.airquality.MeasuredValue
import ows.kotlinstudy.dustapp.data.models.monitoringstation.MonitoringStation
import ows.kotlinstudy.dustapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var cancellationTokenSource: CancellationTokenSource? = null    // 위치 요청을 취소하기 위한 token

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        bindViews()
        initVariables()
        requestLocationPermissions()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancellationTokenSource?.cancel()
        scope.cancel()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val locationPermissionGranted =
            requestCode == REQUEST_ACCESS_LOCATION_PERMISSIONS &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED


        val backgroundLocationPermissionGranted =
            requestCode == REQUEST_BACKGOUND_ACCESS_LOCATION_PERMISSIONS &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED

        if(Build.VERSION.SDK_INT >=- Build.VERSION_CODES.R){
            if(!backgroundLocationPermissionGranted){
                requestBackgroundLocationPermissions()
            }else{
                fetchAirQualityData()
            }
        }else {
            if (!locationPermissionGranted) {
                finish()
            } else {
                fetchAirQualityData()
            }
        }
    }

    private fun bindViews() {
        binding.refresh.setOnRefreshListener {
            fetchAirQualityData()
        }
    }

    private fun initVariables() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            REQUEST_ACCESS_LOCATION_PERMISSIONS
        )
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestBackgroundLocationPermissions(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
            REQUEST_BACKGOUND_ACCESS_LOCATION_PERMISSIONS
        )
    }

    @SuppressLint("MissingPermission")
    private fun fetchAirQualityData() {
        // fetchData
        cancellationTokenSource = CancellationTokenSource()

        // getCurrentLocation nullable function
        fusedLocationProviderClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource!!.token
        ).addOnSuccessListener { location ->
            scope.launch {
                binding.errorDescriptionTextView.visibility = View.GONE
                try{
                    val monitoringStation =
                        Repository.getNearbyMonitoringStation(location.latitude, location.longitude)

                    val measuredValue =
                        Repository.getLatestAirQualityData(monitoringStation!!.stationName!!)

                    displayAirQualityData(monitoringStation, measuredValue!!)
                }catch (exception: Exception){
                    binding.errorDescriptionTextView.visibility = View.VISIBLE
                    binding.contentsLayout.alpha = 0F
                }finally {
                    binding.progressBar.visibility = View.GONE
                    binding.refresh.isRefreshing = false
                }

            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun displayAirQualityData(monitoringStation: MonitoringStation, measuredValue: MeasuredValue){
        binding.contentsLayout.animate()
            .alpha(1F)
            .start()

        binding.measuringStationName.text = monitoringStation.stationName
        binding.measuringStationAddressTextView.text = monitoringStation.addr

        (measuredValue.khaiGrade ?: Grade.UNKNOWN).let { grade ->  
            binding.root.setBackgroundResource(grade.colorResId)
            binding.totalGradeLabelTextView.text = grade.label
            binding.totalGradeEmojiTextView.text = grade.emoji
        }

        with(measuredValue){
            binding.fineDustInfomationTextView.text =
                "미세먼지: ${pm10Value} ㎍/㎥ ${(pm10Grade ?: Grade.UNKNOWN).emoji}"
            binding.ultraFineDustInformationTextView.text =
                "초미세먼지: ${pm25Value} ㎍/㎥ ${(pm25Grade ?: Grade.UNKNOWN).emoji}"

            with(binding.so2Item){
                labelTextView.text = "아황산가스"
                gradeTextView.text = (so2Grade ?: Grade.UNKNOWN).toString()
                valueTextView.text = "$so2Value ppm"
            }

            with(binding.coItem){
                labelTextView.text = "일산화탄소"
                gradeTextView.text = (coGrade?: Grade.UNKNOWN).toString()
                valueTextView.text = "$coValue ppm"
            }

            with(binding.o3Item){
                labelTextView.text = "오존"
                gradeTextView.text = (o3Grade ?: Grade.UNKNOWN).toString()
                valueTextView.text = "$o3Value ppm"
            }

            with(binding.no2Item){
                labelTextView.text = "이산화질소"
                gradeTextView.text = (no2Grade ?: Grade.UNKNOWN).toString()
                valueTextView.text = "$no2Value ppm"
            }
        }
    }

    companion object {
        private const val REQUEST_ACCESS_LOCATION_PERMISSIONS = 100
        private const val REQUEST_BACKGOUND_ACCESS_LOCATION_PERMISSIONS = 101
    }
}