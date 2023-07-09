package com.ssafy.livebroadcast.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssafy.livebroadcast.ApplicationClass
import com.ssafy.livebroadcast.R
import com.ssafy.livebroadcast.databinding.FragmentAddRoomBinding
import com.ssafy.livebroadcast.databinding.FragmentEditUserBinding
import com.ssafy.livebroadcast.dto.Room

class AddRoomFragment : Fragment() {
    companion object {
        fun newInstance() = AddRoomFragment()
    }

    private lateinit var binding: FragmentAddRoomBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSave.setOnClickListener {
            val room = Room(
                binding.editTextThumbnail.text.toString(),
                binding.editTextTitle.text.toString(),
                binding.editTextTopic.text.toString(),
                binding.editTextRemainTime.text.toString().toLong()
            )
            ApplicationClass.fireStore.addRoom(room)
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}