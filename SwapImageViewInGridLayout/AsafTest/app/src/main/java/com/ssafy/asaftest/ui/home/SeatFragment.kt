package com.ssafy.asaftest.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.ssafy.asaftest.R
import com.ssafy.asaftest.databinding.FragmentSeatBinding
import com.ssafy.asaftest.ui.BaseFragment

class SeatFragment : BaseFragment<FragmentSeatBinding>(FragmentSeatBinding::bind, R.layout.fragment_seat) {
    private lateinit var targetView: SeatView
    private var startX = 0
    private var startY = 0
    private var offsetX = 0
    private var offsetY = 0
    private var targetViewIndex = 20
    private var position : MutableList<Int> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 위치(index) - 그리드레이아웃(element) 리스트
        for(i in 0 .. 15) position.add(i)

        val gridLayout = binding.gridLayout
        targetView = binding.item1 // 아무 아이템이나 같은 크기이므로 넣어주면 됨 사이즈 계산에만 사용

        for (i in 0 until gridLayout.childCount) {
            val childView = gridLayout.getChildAt(i)
            if (childView is SeatView) {
                childView.seatText.text = i.toString()
                setTouchListener(childView)
            }
        }
    }

    private fun moveImageViewToPosition(cur: SeatView, newX: Int, newY: Int) {
        cur.animate()
            .x(newX.toFloat())
            .y(newY.toFloat())
            .setDuration(0)
            .start()
    }

    private fun calculateNewIndex(x: Int, y: Int): Int {
        val columnWidth = targetView.width
        val rowHeight = targetView.height
        val columnIndex = x / columnWidth
        val rowIndex = y / rowHeight
        Log.d("스왑계산", "계산 : ${rowIndex * 4 + columnIndex}")
        return rowIndex * 4 + columnIndex
    }

    private fun moveImageViewToGridPosition(cur: SeatView, newIndex: Int) {
        val columnWidth = targetView.width
        val rowHeight = targetView.height
        val columnIndex = newIndex % 4
        val rowIndex = newIndex / 4
        val newX = columnIndex * columnWidth
        val newY = rowIndex * rowHeight
        moveImageViewToPosition(cur, newX, newY)
    }

    private fun swapImageViewPosition(cur: SeatView, nextIndex: Int) {
        val parentView = cur.parent as ViewGroup
        val curIndex = targetViewIndex // 15 현재위치
        val nxtIndex = nextIndex // 0 나중위치

        val org = parentView.getChildAt(position[curIndex]) as SeatView // 0번 이미지뷰
        val next = parentView.getChildAt(position[nxtIndex]) as SeatView // 15번 이미지뷰

        moveImageViewToGridPosition(org, nxtIndex)
        moveImageViewToGridPosition(next, curIndex)
        // 위치정보 변경
        position.swap(curIndex, nextIndex)
    }

    private fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
        val temp = this[index1]
        this[index1] = this[index2]
        this[index2] = temp
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener(target: SeatView) {
        target.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = target.x.toInt()
                    startY = target.y.toInt()
                    offsetX = event.rawX.toInt() - startX
                    offsetY = event.rawY.toInt() - startY
                    targetViewIndex = calculateNewIndex(startX + target.width / 2, startY + target.height / 2)
                }
                MotionEvent.ACTION_MOVE -> {
                    val newX = event.rawX.toInt() - offsetX
                    val newY = event.rawY.toInt() - offsetY

                    // 바운더리 제한
                    val maxX = (target.parent as View).width - target.width / 2
                    val maxY = (target.parent as View).height - target.height / 2

                    // 새로운 위치를 바운더리 내에 유지하도록 조정
                    // -1을 안하면 합이 바운더리를 넘어가서 인덱스가 계산됨 (몫이 1이되는 걸 방지)
                    val constrainedX = newX.coerceIn(-(target.width / 2 - 1), maxX - 1)
                    val constrainedY = newY.coerceIn(-(target.height / 2 - 1), maxY - 1)

                    moveImageViewToPosition(target, constrainedX, constrainedY)

                    // Log.d("이동", "${newX}, ${newY}, ${maxX}, ${maxY} " )
                }
                // 시작위치가 좌측상단이므로 중간좌표에서 시작한 것처럼 width/2 , height/2 보정
                MotionEvent.ACTION_UP -> {
                    val newIndex = calculateNewIndex(target.x.toInt() + target.width / 2
                        , target.y.toInt() + target.height / 2)
                    Log.d("스왑전", "$targetViewIndex, $newIndex")
                    swapImageViewPosition(target, newIndex)
                }
            }
            true
        }
    }
}