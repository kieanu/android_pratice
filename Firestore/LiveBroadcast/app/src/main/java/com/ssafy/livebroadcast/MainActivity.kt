package com.ssafy.livebroadcast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.ssafy.livebroadcast.databinding.ActivityMainBinding
import com.ssafy.livebroadcast.ui.main.AddRoomFragment
import com.ssafy.livebroadcast.ui.main.EditUserFragment
import com.ssafy.livebroadcast.ui.main.MainFragment
import com.ssafy.livebroadcast.ui.main.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding :ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.myPage.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(binding.container.id, EditUserFragment.newInstance())
                .commitNow()
        }

        binding.addRoom.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(binding.container.id, AddRoomFragment.newInstance())
                .commitNow()
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.container.id, MainFragment.newInstance())
                .commitNow()
        }
    }
}