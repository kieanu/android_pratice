package com.ssafy.flowexample1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ssafy.flowexample1.databinding.ActivityMainBinding
import com.ssafy.flowexample1.ui.user.UserFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, UserFragment.newInstance("param1","param2"))
                .commitNow()
        }
    }
}