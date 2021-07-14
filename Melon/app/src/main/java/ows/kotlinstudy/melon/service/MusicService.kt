package ows.kotlinstudy.melon.service

import retrofit2.Call
import retrofit2.http.GET

interface MusicService {
    @GET("/v3/72ed8575-38b5-4276-ad5c-8c4b2105effd")
    fun listMusics() : Call<MusicDto>
}