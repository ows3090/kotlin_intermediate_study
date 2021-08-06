package ows.kotlinstudy.data

import ows.kotlinstudy.copyrightapp.BuildConfig
import ows.kotlinstudy.data.models.PhotoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApiService {

    @GET(
        "photos/random?" +
                "client_id=${BuildConfig.UNSPLASH_ACCESS_KEY}" +
                "&count=30"
    )
    suspend fun getRandomPhtos(
        @Query("query") query: String?
    ): Response<List<PhotoResponse>>
}