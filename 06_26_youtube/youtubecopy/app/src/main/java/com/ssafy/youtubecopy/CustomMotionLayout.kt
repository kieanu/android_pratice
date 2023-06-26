package com.ssafy.youtubecopy

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout

class CustomMotionLayout(context: Context, attributes: AttributeSet? = null) : MotionLayout(context, attributes) {
    private var motionTouchStarted = false // 정확한 위치에서만 true
    private val mainContainerView by lazy {
        findViewById<View>(R.id.mainContainerLayout) // view 속성만 필요해서 view 로 받음
    }
    private val hitRect = Rect()

    init {
        setTransitionListener(object : TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                // 트렌지션 리스너에서 스텐지션이 완료됬다면 모션 터치도 끝난 것으로 볼 수 있기 때매
                // 완료시 모션 터치 스타츠를 false 로
                motionTouchStarted = false // transitions 가 완료되고 나서는 해제
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
        })
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            // 터치 뗄 때 또는 취소 한 경우에는 Default 처리 (터치 즉, 누르고 있을 떄만 처리하겠다는 거)
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                motionTouchStarted = false
                return super.onTouchEvent(event)
            }
        }

        if (!motionTouchStarted) {
            mainContainerView.getHitRect(hitRect)
            // 실제로 이벤트의 좌표가 mainContainerView 에 포함되는지 확인 (포함되면 true)
            motionTouchStarted = hitRect.contains(event.x.toInt(), event.y.toInt())
        }

        return super.onTouchEvent(event) && motionTouchStarted
    }

    private val gestureListener by lazy {
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(
                e1: MotionEvent,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                mainContainerView.getHitRect(hitRect) // 해당 렉트에만 터치가 먹히도록 할거
                // 현재 제스쳐가 메인 컨테이너 위치에 포함되는 위치인지에 따라 반환
                return hitRect.contains(e1.x.toInt(), e1.y.toInt())
            }
        }
    }

    private val gestureDetector by lazy {
        GestureDetector(context, gestureListener)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }


}