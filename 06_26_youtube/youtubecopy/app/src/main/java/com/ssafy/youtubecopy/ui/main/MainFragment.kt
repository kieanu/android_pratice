package com.ssafy.youtubecopy.ui.main

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.ssafy.youtubecopy.MainActivity
import com.ssafy.youtubecopy.R
import com.ssafy.youtubecopy.adapter.VideoAdapter
import com.ssafy.youtubecopy.databinding.ActivityMainBinding
import com.ssafy.youtubecopy.databinding.FragmentMainBinding
import com.ssafy.youtubecopy.dto.VideoDto
import com.ssafy.youtubecopy.service.VideoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainFragment(private val fragmentContext:Context) : Fragment(R.layout.fragment_main) {
    private lateinit var videoAdapter: VideoAdapter
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var binding: FragmentMainBinding
    private val player: ExoPlayer = ExoPlayer.Builder(fragmentContext).build()

    // 객체 생성이 아니라 뷰가 그려질 때 (replace) 호출된다.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)

        initPlayer()
        initMotionLayout()
        initRecyclerView()
        initControlButton()

        getVideoList()
    }


    private fun initMotionLayout() {
        binding.playerMotionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}

            override fun onTransitionChange( // 재정의를 통해 메인 엑티비티(모션 레이아웃)과 연동한다.
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                /**
                 * 메인 엑티비티 모션 레이아웃에 값을 전달
                 */
                /**
                 * Fragment 는 자기 단독으로 존재할 수 없기 떄문에 activity 가 존재 할수밖에 없고
                 * activity 를 가져오면 해당 Fragment 가 attach 되어있는 액티비티를 가져온다.
                 */
                (activity as MainActivity).also {
                    it.findViewById<MotionLayout>(mainBinding.mainMotionLayout.id).progress = kotlin.math.abs(progress)
                }
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {}

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}

        })
    }

    private fun initRecyclerView() {
        videoAdapter = VideoAdapter(callback = { url, title ->
            play(url, title)
        })

        binding.fragmentRecyclerView.adapter = videoAdapter
    }

    private fun initPlayer() = with(binding) {
        playerView.player = player

        player.addListener(object : Player.Listener {
            // play 여부가 바뀔 때 마다 실행 (아이콘을 알맞게 변경)
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)

                if (isPlaying) bottomPlayerControlButton.setImageResource(R.drawable.ic_baseline_pause_24)
                else bottomPlayerControlButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            }
        })
    }

    private fun initControlButton() = with(binding) {
        bottomPlayerControlButton.setOnClickListener {
            // player 의 재생 여부 확인해서 처리
            if (player.isPlaying) {
                player.pause()
            } else {
                player.play()
            }
        }
    }

    private fun getVideoList() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(VideoService::class.java).also {
            it.listVideos()
                .enqueue(object : Callback<VideoDto> {
                    override fun onResponse(call: Call<VideoDto>, response: Response<VideoDto>) {
                        if (response.isSuccessful.not()) {
                            Log.e("싸피", "response fail")
                            return
                        }

                        response.body()?.let { videoDto ->
                            videoAdapter.submitList(videoDto.videos)
                        }

                    }

                    override fun onFailure(call: Call<VideoDto>, t: Throwable) {
                        // 예외처리
                    }

                })
        }
    }

    // 동영상 아이템 눌렀을 때 처리
    fun play(url: String, title: String) {
        val dataSourceFactory = DefaultDataSource.Factory(fragmentContext)
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.parse(url)))
        player.setMediaSource(mediaSource)
        player.prepare()
        player.play()

        Log.d("싸피", "play: 호출됨, $binding")

        binding.playerMotionLayout.transitionToEnd() // 열기
        binding.bottomTitleTextView.text = title
    }

    override fun onStop() {
        super.onStop()
        player.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}