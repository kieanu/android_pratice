package com.ssafy.livebroadcast.ui.main

import android.Manifest
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.ssafy.livebroadcast.databinding.FragmentEditUserBinding

class EditUserFragment : Fragment() {
    companion object {
        fun newInstance() = EditUserFragment()
    }

    private lateinit var binding: FragmentEditUserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.getImage.setOnClickListener {
            openGallery()
        }
    }

    // 카메라 권한 얻어오기
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            // 이미지 선택 완료 후 처리할 작업 수행
            binding.profileImage.setImageURI(uri)
        } else {
            // 이미지 선택 취소 또는 실패 시 처리할 작업 수행
            Toast.makeText(requireContext(), "이미지 선택을 취소하였습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // 갤러리에서 사진 선택
    fun openGallery() {
        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                // 권한 허용된 경우 갤러리 열기
                getContent.launch("image/*")
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                // 권한 거부된 경우 권한 허용 다이얼로그 띄우기
                Toast.makeText(requireContext(), "갤러리 접근 권한을 허용해야 사진을 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setPermissions(Manifest.permission.READ_MEDIA_IMAGES)
            .setDeniedMessage("갤러리 접근 권한을 허용하지 않으면 사진을 선택할 수 없습니다.\n[설정] > [권한]에서 권한을 허용해주세요.")
            .check()
    }

}