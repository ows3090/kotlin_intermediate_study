package ows.kotlinstudy.dustapp.data

import android.os.Build
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ows.kotlinstudy.dustapp.BuildConfig
import ows.kotlinstudy.dustapp.data.models.airquality.MeasuredValue
import ows.kotlinstudy.dustapp.data.models.monitoringstation.MonitoringStation
import ows.kotlinstudy.dustapp.data.models.monitoringstation.MonitoringStationsResponse
import ows.kotlinstudy.dustapp.data.services.AirKoreaApiService
import ows.kotlinstudy.dustapp.data.services.KaKaoLocalApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object Repository {

    suspend fun getNearbyMonitoringStation(latitude: Double, longitude: Double): MonitoringStation?{
        val tmCoordiates = kakaoLocalApiService
            .getTmCoordinates(longitude, latitude)
            .body()
            ?.documents
            ?.firstOrNull()

        val tmX = tmCoordiates?.x
        val tmY = tmCoordiates?.y

        return airKoreaApiService
            .getNearbyMonitoringStation(tmX!!, tmY!!)
            .body()
            ?.response
            ?.body
            ?.monitoringStations
            ?.minByOrNull { it.tm ?: Double.MAX_VALUE }
    }

    suspend fun getLatestAirQualityData(stationName: String): MeasuredValue? {
        return airKoreaApiService
            .getRealtimeAirQualites(stationName)
            .body()
            ?.response
            ?.body
            ?.measuredValues
            ?.firstOrNull()
    }

    private val kakaoLocalApiService: KaKaoLocalApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Url.KAKAO_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(buildHttpClient())
            .build()
            .create()
    }

    private val airKoreaApiService: AirKoreaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Url.AIR_KOREA_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(buildHttpClient())
            .build()
            .create()
    }

    private fun buildHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if(BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else{
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            ).build()
}