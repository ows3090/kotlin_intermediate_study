package ows.kotlinstudy.youtube

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ows.kotlinstudy.youtube.adapter.VideoAdapter
import ows.kotlinstudy.youtube.dto.VideoDto
import ows.kotlinstudy.youtube.service.VideoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var videoAdapter: VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, PlayerFragment())
            .commit()

        videoAdapter = VideoAdapter(callback = { url, title ->
            supportFragmentManager.fragments.find { it is PlayerFragment }?.let {
                (it as PlayerFragment).play(url, title)
            }
        })
        findViewById<RecyclerView>(R.id.mainRecyclerView).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = videoAdapter
        }

        getVideoList()
    }

    private fun getVideoList(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(VideoService::class.java).also {
            it.listVideos()
                .enqueue(object: Callback<VideoDto>{
                    override fun onResponse(call: Call<VideoDto>, response: Response<VideoDto>) {
                        if(response.isSuccessful.not()){
                            return
                        }

                        response.body()?.let {
                            videoAdapter.submitList(it.videos)
                        }
                    }

                    override fun onFailure(call: Call<VideoDto>, t: Throwable) {
                        // 예외처리
                    }
                })
        }
    }
}