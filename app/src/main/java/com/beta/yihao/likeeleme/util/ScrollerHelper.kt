package com.beta.yihao.likeeleme.util

import android.content.Context
import android.os.Handler
import android.view.View
import android.widget.Scroller
import com.beta.yihao.likeeleme.behavior.ShopInfoScrollBehavior

/**
 * @Author yihao
 * @Description Scroller辅助类
 * @Date 2019/1/3-15:55
 * @Email yihaobeta@163.com
 */
class ScrollerHelper(
    context: Context,
    private val listener: (dy: Float, state: ShopInfoScrollBehavior.Companion.STATE) -> Unit
) :
    Scroller(context), Runnable {
    private val handler = Handler(context.mainLooper)
    private var viewToTranslate: View? = null
    private var isScrolling = false
    override fun run() {
        if (this.computeScrollOffset()) {
            viewToTranslate?.let {
                val dy = this.currY.toFloat()
                it.translationY = dy
                listener.invoke(dy, ShopInfoScrollBehavior.Companion.STATE.SCROLLING)
            }
            handler.post(this)
        } else {
            isScrolling = false
            viewToTranslate?.let {
                when (it.translationY) {
                    0F -> listener.invoke(it.translationY, ShopInfoScrollBehavior.Companion.STATE.NORMAL)
                    else -> listener.invoke(it.translationY, ShopInfoScrollBehavior.Companion.STATE.HIDE)
                }
            }
        }
    }

    fun startScrollVertical(view: View, startY: Int = view.translationY.toInt(), dy: Int, duration: Int = 800) {
        if (isScrolling) return
        viewToTranslate = view
        startScroll(0, startY, 0, dy, duration)
        isScrolling = true
        handler.post(this)
    }
}