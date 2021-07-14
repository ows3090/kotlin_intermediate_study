package ows.kotlinstudy.youtube.service

import ows.kotlinstudy.youtube.dto.VideoDto
import retrofit2.Call
import retrofit2.http.GET

interface VideoService {
    @GET("v3/8c3a7ca4-0296-47c6-abc0-3280250cb578")
    fun listVideos(): Call<VideoDto>
}