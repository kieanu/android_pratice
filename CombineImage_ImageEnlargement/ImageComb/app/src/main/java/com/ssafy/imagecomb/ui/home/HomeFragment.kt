package com.ssafy.imagecomb.ui.home

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.ssafy.imagecomb.CustomImageDialog
import com.ssafy.imagecomb.R
import com.ssafy.imagecomb.databinding.DialogSignImageBinding
import com.ssafy.imagecomb.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.image.setOnClickListener {
            val imageUrl = "https://cdn.aitimes.kr/news/photo/202303/27617_41603_044.jpg"
            val dialog = CustomImageDialog(requireContext(), imageUrl)
            dialog.show()
            // 중앙에 위치 시킬 수가없음 layout과 상관없는 더미 크기가 키워짐
            //dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    private fun showImageDialog() {
//        val dialogViewBinding: DialogSignImageBinding = DialogSignImageBinding.inflate(layoutInflater, _binding?.root, false)
//        val imageUrl = "https://cdn.aitimes.kr/news/photo/202303/27617_41603_044.jpg" // 이미지 URL을 가져와야 함
//
//        val builder = AlertDialog.Builder(requireContext())
//        builder.setView(dialogViewBinding.root)
//
//        val imageView = dialogViewBinding.dialogImageView
//        Glide.with(dialogViewBinding.root)
//            .load(imageUrl)
//            .into(imageView)
//
//        val dialog = builder.create()
//        dialog.show()
//        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
//    }
}