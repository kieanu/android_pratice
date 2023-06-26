package com.ssafy.youtubecopy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.ssafy.youtubecopy.adapter.VideoAdapter
import com.ssafy.youtubecopy.databinding.ActivityMainBinding
import com.ssafy.youtubecopy.databinding.FragmentMainBinding
import com.ssafy.youtubecopy.dto.VideoDto
import com.ssafy.youtubecopy.service.VideoService
import com.ssafy.youtubecopy.ui.main.MainFragment
import com.ssafy.youtubecopy.ui.main.MainViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    /**
     * 먼저, 리사이클러뷰를 어뎁터와 리니어레이아웃 연결
     * 프레임 레이아웃에 프레그먼트를 어테치
     */
    private lateinit var videoAdapter: VideoAdapter
    private lateinit var binding: ActivityMainBinding
    private val fragment: MainFragment by lazy {
        MainFragment(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initRecyclerView()
        getVideoList()
    }

    private fun initRecyclerView() {
        Log.d("싸피", "initRecyclerView: $fragment")
        videoAdapter = VideoAdapter(callback = {url, title->
            // 모션 레이아웃 end(전체화면) 상태로 바꾸고..
            //supportFragmentManager 로 모든 프레그 먼트중 PlayerFragment 를 찾아서 가져옴
            initFragmentContainer()
            supportFragmentManager.fragments.find{ it is MainFragment}?.let {
                (it as MainFragment).play(url,title)
            }
        })

        binding.mainRecyclerView.adapter = videoAdapter
    }

    private fun initFragmentContainer() {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commitNow() // commit은 비동기 이므로 이후 코드가 먼저 실행됨, commitNow로 동기성보장
    }

    private fun getVideoList(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(VideoService::class.java).also{
            it.listVideos()
                .enqueue(object: Callback<VideoDto> {
                    override fun onResponse(call: Call<VideoDto>, response: Response<VideoDto>) {
                        if(response.isSuccessful.not()){
                            Log.e("싸피","response fail")
                            return
                        }

                        response.body()?.let{ videoDto ->
                            videoAdapter.submitList(videoDto.videos)
                        }

                    }

                    override fun onFailure(call: Call<VideoDto>, t: Throwable) {
                        // 예외처리
                    }
                })
        }
    }
}