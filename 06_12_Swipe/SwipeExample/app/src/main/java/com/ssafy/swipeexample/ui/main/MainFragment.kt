package com.ssafy.swipeexample.ui.main

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.ssafy.swipeexample.CustomFrameLayout
import com.ssafy.swipeexample.R
import com.ssafy.swipeexample.databinding.FragmentItemBinding
import com.ssafy.swipeexample.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding:FragmentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 바인딩 클래스를 사용하여 XML 레이아웃 파일 인플레이션
        val itemBinding: FragmentItemBinding = FragmentItemBinding.inflate(LayoutInflater.from(context),binding.root,false)

        // 인플레이션된 바인딩 객체에서 카드뷰 참조
        itemBinding.cardText.text = viewModel.cardList[viewModel.cur++].desc
        val newCardView = itemBinding.card

        newCardView.setViewModel(viewModel)
        newCardView.setBinding(binding)

        // 새로운 카드뷰의 부모 확인 및 제거
        val previousParent = newCardView.parent as? ViewGroup
        previousParent?.removeView(newCardView)

        binding.cardContainer.addView(newCardView)
    }


}