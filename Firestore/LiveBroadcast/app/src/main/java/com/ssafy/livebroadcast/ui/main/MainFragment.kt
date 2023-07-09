package com.ssafy.livebroadcast.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.ssafy.livebroadcast.R
import com.ssafy.livebroadcast.adapter.RoomAdapter
import com.ssafy.livebroadcast.databinding.FragmentEditUserBinding
import com.ssafy.livebroadcast.databinding.FragmentMainBinding
import kotlinx.coroutines.launch

class MainFragment : Fragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = RoomAdapter()
        binding.recyclerRoom.adapter = adapter

        lifecycleScope.launch {
            viewModel.rooms.collect {
                adapter.submitList(it)
                Log.d("리사이클러 싸피", "onViewCreated: $it")
            }
        }
    }
}