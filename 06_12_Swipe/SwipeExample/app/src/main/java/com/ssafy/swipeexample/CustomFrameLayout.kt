package com.ssafy.swipeexample

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.ssafy.swipeexample.databinding.FragmentItemBinding
import com.ssafy.swipeexample.databinding.FragmentMainBinding
import com.ssafy.swipeexample.ui.main.MainViewModel

class CustomFrameLayout(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    private var initialX: Float = 0f
    private var initialY: Float = 0f
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentMainBinding

    init{
        setupTouchListener()
    }

    fun setViewModel(viewModel: MainViewModel) {
        this.viewModel = viewModel
    }

    fun setBinding(binding: FragmentMainBinding) {
        this.binding = binding
    }

    private fun setupTouchListener() {
        setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    performClick() // 클릭 이벤트 처리를 위해 performClick 호출
                    initialX = event.x
                    initialY = event.y
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val deltaX = event.x - initialX
                    val deltaY = event.y - initialY

                    // 이동 및 회전 애니메이션 적용
                    animate()
                        .translationX(deltaX)
                        .translationY(deltaY)
                        .rotation(deltaX / width * 45) // 회전 각도 설정
                        .setDuration(0)
                        .start()

                    true
                }
                MotionEvent.ACTION_UP -> {
                    // 애니메이션 완료 후 카드 위치 업데이트
                    updateCardPosition()
                    true
                }
                else -> false
            }
        }
    }

    private fun updateCardPosition() {
        val threshold = width * 0.3f // 스와이프로 카드를 제거할 기준 값
        val animeListener =  object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                Log.d("싸피", "onAnimationStart: 애니메이션 시작")
                // 현재 선택된 레이아웃제거
                (parent as? ViewGroup)?.removeView(this@CustomFrameLayout)
            }

            // 얘는 왜 계속 2번 호출되냐
            override fun onAnimationEnd(animation: Animator) {
                if(viewModel.cur < viewModel.size) {
                    // 바인딩 클래스를 사용하여 XML 레이아웃 파일 인플레이션
                    val itemBinding: FragmentItemBinding =
                        FragmentItemBinding.inflate(LayoutInflater.from(context), binding.root, false)

                    // 인플레이션된 바인딩 객체에서 카드뷰 참조
                    itemBinding.cardText.text = viewModel.cardList[viewModel.cur++].desc
                    val newCardView = itemBinding.card

                    newCardView.setViewModel(viewModel)
                    newCardView.setBinding(binding)

                    binding.cardContainer.addView(newCardView, 0)
                }
                Log.d("싸피", "onAnimationEnd: 애니메이션 끝 ${viewModel.cur}")
            }

            override fun onAnimationCancel(animation: Animator) {
                Log.d("싸피", "onAnimationStart: 애니메이션 취소")
            }

            override fun onAnimationRepeat(animation: Animator) {
                Log.d("싸피", "onAnimationStart: 애니메이션 반복")
            }
        }

        if (translationX > threshold) {
            // 오른쪽으로 스와이프하여 제거
            animate()
                .translationX(width.toFloat())
                .setDuration(300)
                .setListener(animeListener)
                .start()
        } else if (translationX < -threshold) {
            // 왼쪽으로 스와이프하여 제거
            animate()
                .translationX(-width.toFloat())
                .setDuration(300)
                .setListener(animeListener)
                .start()
        } else {
            // 스와이프가 기준 값을 넘지 않아 원래 위치로 복귀
            animate()
                .translationX(0f)
                .translationY(0f)
                .rotation(0f)
                .setDuration(300)
                .start()
        }
    }
}
