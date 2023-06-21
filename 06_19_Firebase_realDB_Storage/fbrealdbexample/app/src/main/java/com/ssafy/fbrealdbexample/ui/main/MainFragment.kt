package com.ssafy.fbrealdbexample.ui.main

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.ssafy.fbrealdbexample.ApplicationClass
import com.ssafy.fbrealdbexample.UserListAdapter
import com.ssafy.fbrealdbexample.database.UserDto
import com.ssafy.fbrealdbexample.databinding.FragmentMainBinding
import com.ssafy.fbrealdbexample.repository.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentMainBinding
    private lateinit var requestPermissionLauncher : ActivityResultLauncher<String>
    private lateinit var pickImageLauncher : ActivityResultLauncher<Intent>
    private lateinit var fbRealDbRef : DatabaseReference
    private val userListAdapter: UserListAdapter by lazy {
        UserListAdapter(requireActivity())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setReq()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        fbRealDbRef = ApplicationClass.fbRealDb.realDbRef
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUi()
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun setReq() {
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                openImagePicker()
            } else {
                openImagePicker()
                Log.d("싸피", "리퀘스트 거부")
            }
        }

        pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                var selectedImageUri: Uri? = result.data?.data
                binding.imageView.setImageURI(selectedImageUri)

                // 갤러리에서 이미지 선택하면 실행
                selectedImageUri?.let { uri ->
                    // Firebase Storage에 업로드
                    CoroutineScope(Dispatchers.Default).launch {
                        val imageUrl = ApplicationClass.fbStorage.uploadImageToFirebaseStorage(uri)

                        // Firebase RealDB에 저장 할 유저 생성
                        val user = UserDto(binding.nameTxt.text.toString(),binding.ageTxt.text.toString().toInt(),
                            binding.introduceTxt.text.toString(), imageUrl)

                        // Firebase RealDB에 유저 정보 업로드
                        fbRealDbRef.push().setValue(user)
                    }
                }
            }
        }
    }

    private fun setUi() {
        binding.recyclerView.adapter = userListAdapter
        binding.selectImageButton.setOnClickListener {
            checkPermissionAndOpenImagePicker()
        }
        // firebase real db 감지
        viewLifecycleOwner.lifecycleScope.launch {
            ApplicationClass.fbRealDb.fetchData()
                .onStart { /* 로딩 상태 표시 */ }
                .onEach {}// 각 데이터 추가 작업(필터링 등)
                .collect {// Flow에서 방출된 데이터 처리
                    userListAdapter.submitList(it)
                    Log.d("싸피", "setUi: 플로우 리스트 감지 : $it")
                }
        }
    }

    private fun checkPermissionAndOpenImagePicker() {
        Log.d("싸피", "checkPermissionAndOpenImagePicker: 피커")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permission = Manifest.permission.READ_EXTERNAL_STORAGE
            val permissionGranted = PackageManager.PERMISSION_GRANTED

            if (ContextCompat.checkSelfPermission(requireContext(), permission) != permissionGranted) {
                Log.d("싸피", "checkPermissionAndOpenImagePicker: 권한없음")
                requestPermissionLauncher.launch(permission)
            } else {
                Log.d("싸피", "checkPermissionAndOpenImagePicker: 권한있음")
                openImagePicker()
            }
        } else {
            Log.d("싸피", "checkPermissionAndOpenImagePicker: 권한있음")
            openImagePicker()
        }
    }

    private fun openImagePicker() {
        Log.d("싸피", "openImagePicker: 갤러리 호출")
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }
}