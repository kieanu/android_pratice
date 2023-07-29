package com.ssafy.imagecomb.ui.dashboard

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ssafy.imagecomb.databinding.FragmentDashboardBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
private const val PATH = "/data/data/com.ssafy.imagecomb/files/"
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    val images = listOf("https://cdn.aitimes.kr/news/photo/202303/27617_41603_044.jpg"
        ,"https://static4.depositphotos.com/1003326/319/i/450/depositphotos_3191160-stock-photo-blurry-bright-background.jpg")

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            createCombinedImageFromUrls(images, PATH + "combine2.png", 100)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 이미지 주소 리스트로부터 이미지 파일 생성
    suspend fun createCombinedImageFromUrls(imageUrls: List<String>, filePath: String, spacing: Int) {
        val bitmapList = imageUrls.mapNotNull { getBitmapFromUrl(it) }

        // 비트맵들을 수직으로 합치기 (간격 추가)
        val combinedBitmap = combineBitmapsVertically(bitmapList, spacing)

        // 합쳐진 비트맵을 이미지 파일로 저장
        combinedBitmap?.let {
            saveBitmapToFile(it, filePath)
        }
    }

    suspend fun getBitmapFromUrl(imageUrl: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(imageUrl)
                BitmapFactory.decodeStream(url.openConnection().getInputStream())
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

    // 비트맵들을 수직으로 합치기
    fun combineBitmapsVertically(bitmapList: List<Bitmap>, spacing: Int): Bitmap? {
        if (bitmapList.isEmpty()) return null

        // 비트맵들을 수직으로 합친 새로운 비트맵 생성
        val totalHeight = bitmapList.sumOf { it.height } + (spacing * (bitmapList.size - 1))
        val combinedBitmap = Bitmap.createBitmap(bitmapList[0].width, totalHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(combinedBitmap)

        // 비트맵들을 캔버스에 수직으로 그리기
        var top = 0
        for (bitmap in bitmapList) {
            canvas.drawBitmap(bitmap, 0f, top.toFloat(), null)
            top += bitmap.height + spacing // 간격 추가
        }

        return combinedBitmap
    }

    // 비트맵을 이미지 파일로 저장
    fun saveBitmapToFile(bitmap: Bitmap, filePath: String) {
        try {
            val file = File(filePath)
            val bos = BufferedOutputStream(FileOutputStream(file))
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
            bos.flush()
            bos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}