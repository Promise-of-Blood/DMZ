package com.example.dmz.ui.browse

import android.view.GestureDetector
import android.view.MotionEvent
import androidx.constraintlayout.motion.widget.MotionLayout
import kotlin.math.abs

class MotionLayoutGestureListener(
    val motionLayout: MotionLayout,
    yBuffer: Int = 10
) : GestureDetector.SimpleOnGestureListener() {
    private val Y_BUFFER = yBuffer
    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        if (abs(distanceX) > abs(distanceY)) {
            // 수평 스크롤 감지, MotionLayout에 이벤트를 전달하지 않도록 설정
            motionLayout.parent.requestDisallowInterceptTouchEvent(true)
        } else if (abs(distanceY) > Y_BUFFER) {
            // 수직 스크롤 감지, MotionLayout이 이벤트를 처리하도록 설정
            motionLayout.parent.requestDisallowInterceptTouchEvent(false)
        }
        return super.onScroll(e1, e2, distanceX, distanceY)
    }
    override fun onDown(e: MotionEvent): Boolean {
        // 터치가 시작될 때, MotionLayout에 이벤트를 전달하지 않도록 설정
        motionLayout.parent.requestDisallowInterceptTouchEvent(true)
        return super.onDown(e)
    }
}
fun MotionLayout.setScrollSensitivity(yBuffer: Int = 10) {
    val gestureDetector = GestureDetector(this.context, MotionLayoutGestureListener(this, yBuffer))
    this.setOnTouchListener { _, event ->
        gestureDetector.onTouchEvent(event)
        false // MotionLayout의 기본 터치 동작을 그대로 유지
    }
}