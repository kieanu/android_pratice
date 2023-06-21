package com.ssafy.waveexample.ui.main

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ssafy.waveexample.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.io.IOException
import java.lang.Math.sqrt
import java.net.URL

class MainFragment : Fragment(), SensorEventListener {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val mainViewModel: MainViewModel by viewModels() // var -> setvalue에러뜸 , val이면 안뜸 -> val은 set이 불가능하기 때문 즉 val을 사용하라
    lateinit var binding: ActivityMainBinding

    // 가속도 센서, 매니저 등록
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor

    // 초기 가속도 값 설정 - primitive는 lateinit 불가능
    private var accel = 0f
    private var currentAccel = 0f
    private var lastAccel = 0f

    // 메일 앱
    val smsIntent = Intent(Intent.ACTION_VIEW)

    // 음악 재생
    private lateinit var mediaPlayer: MediaPlayer
    private val url = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
    private var playing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUi()
    }

    private fun setUi() {
        // SensorManager 인스턴스 생성
        sensorManager = getSystemService(requireContext(),SensorManager::class.java) as SensorManager
        // 가속도계 센서 가져오기
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        // 초기 가속도 값 설정
        accel = 10f
        currentAccel = SensorManager.GRAVITY_EARTH
        lastAccel = SensorManager.GRAVITY_EARTH

        // 메일 앱 타입 정하기
        // smsIntent.type = "vnd.android-dir/mms-sms" - 보낼 사람 선택하기
        smsIntent.data = Uri.parse( "smsto:${mainViewModel.phoneNumber}" ) // - 바로 보낼 번호 지정
        smsIntent.putExtra("sms_body", mainViewModel.message)

        // 음악 플레이어 셋팅
        mediaPlayer = MediaPlayer().apply {
            this.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            this.setDataSource(URL(url).toString())
            // 종료되면 실행할 함수
            this.setOnCompletionListener {
                playing = false
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val x:Float = event?.values?.get(0) as Float
        val y:Float = event?.values?.get(0) as Float
        val z:Float = event?.values?.get(0) as Float

        lastAccel = currentAccel
        currentAccel = kotlin.math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()

        val delta: Float = currentAccel - lastAccel

        accel = accel * 0.9f + delta

        //액셀 치수가 30이 넘어가면 흔들었다고 휴대폰이 판단한다.
        if(accel > 30 && !playing){
            Log.d("싸피", "onSensorChanged: 흔들흔들")

            // 메시지 앱 화면으로 이동
            // startActivity(smsIntent)

            // 음악 재생
            playMusic(mediaPlayer)
        }
    }

//    센서의 하드웨어 상태: 센서의 하드웨어 상태가 변경되면 정확도도 변경될 수 있습니다. 예를 들어, 센서가 손상되었거나 불안정한 전원 공급을 받는 경우 정확도가 낮아질 수 있습니다.
//    환경 조건: 주변 환경 조건이 변할 때 센서의 정확도도 영향을 받을 수 있습니다. 예를 들어, 가속도계 센서가 사용자가 움직이는 동안 진동에 노출되는 경우 정확도가 낮아질 수 있습니다.
//    센서의 위치: 센서가 장치의 다른 부분에 위치할 때 정확도가 다를 수 있습니다. 예를 들어, 가속도계 센서가 장치의 상단 또는 하단에 위치한 경우에는 정확도에 차이가 있을 수 있습니다.
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    private fun playMusic(m:MediaPlayer){
        playing = true
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    m.prepare()
                    m.start()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

}