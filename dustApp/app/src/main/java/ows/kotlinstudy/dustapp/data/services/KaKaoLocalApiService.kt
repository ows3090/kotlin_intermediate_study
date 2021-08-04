package ows.kotlinstudy.dustapp.data.services

import ows.kotlinstudy.dustapp.BuildConfig
import ows.kotlinstudy.dustapp.data.models.tmcoordinates.TmCoordinatesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface KaKaoLocalApiService {

    @Headers("Authorization: KakaoAK ${BuildConfig.KAKAO_API_KEY}")
    @GET("/v2/local/geo/transcoord.json?output_coord=TM")
    suspend fun getTmCoordinates(
        @Query("x") longitude: Double,
        @Query("y") latitude: Double
    ): Response<TmCoordinatesResponse>

}