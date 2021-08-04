package ows.kotlinstudy.dustapp.data.services

import ows.kotlinstudy.dustapp.BuildConfig
import ows.kotlinstudy.dustapp.data.models.airquality.AirQualityResponse
import ows.kotlinstudy.dustapp.data.models.monitoringstation.MonitoringStationsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AirKoreaApiService {

    @GET(
        "B552584/MsrstnInfoInqireSvc/getNearbyMsrstnList" +
                "?serviceKey=${BuildConfig.AIR_KOREA_SERVICE_KEY}" +
                "&returnType=json"
    )
    suspend fun getNearbyMonitoringStation(
        @Query("tmX") tmX: Double,
        @Query("tmY") tmY: Double
    ): Response<MonitoringStationsResponse>

    @GET(
        "B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty" +
                "?serviceKey=${BuildConfig.AIR_KOREA_SERVICE_KEY}" +
                "&returnType=json" +
                "&dataTerm=DAILY" +
                "&ver"
    )
    suspend fun getRealtimeAirQualites(
        @Query("stationName") stationName: String
    ): Response<AirQualityResponse>
}