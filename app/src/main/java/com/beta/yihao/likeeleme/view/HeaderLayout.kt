package com.beta.yihao.likeeleme.view

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AccelerateDecelerateInterpolator
import com.beta.yihao.likeeleme.R
import com.beta.yihao.likeeleme.behavior.HeaderControlBehavior
import com.beta.yihao.likeeleme.util.drawableOf
import com.beta.yihao.likeeleme.util.setWrapperDrawable
import kotlinx.android.synthetic.main.header_bar_layout.view.*

/**
 * @Author yihao
 * @Date 2019/1/2-16:32
 * @Email yihaobeta@163.com
 */
class HeaderLayout(context: Context, attributeSet: AttributeSet) : ConstraintLayout(context, attributeSet) {

    private val argbEvaluator: ArgbEvaluator = ArgbEvaluator()
    private val interpolator = AccelerateDecelerateInterpolator()
    private val backDrawable = drawableOf(R.mipmap.back)
    private val searchDrawable = drawableOf(R.mipmap.search)
    private val shareDrawable = drawableOf(R.mipmap.share)
    private val pinDrawable = drawableOf(R.mipmap.pin)


    init {
        LayoutInflater.from(context).inflate(R.layout.header_bar_layout, this)
        this.viewTreeObserver.addOnGlobalLayoutListener {
            val lp = this.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = lp.behavior as HeaderControlBehavior
            //跟随变换
            behavior.setCallBack {
                val interpolation = interpolator.getInterpolation(it)
                setBackgroundColor(argbEvaluator.evaluate(interpolation, Color.TRANSPARENT, Color.WHITE) as Int)
                val w2b = argbEvaluator.evaluate(interpolation, Color.WHITE, Color.BLACK) as Int
                with(w2b) {
                    backIv.setWrapperDrawable(backDrawable, this)
                    searchIv.setWrapperDrawable(searchDrawable, this)
                    shareIv.setWrapperDrawable(shareDrawable, this)
                    pinIv.setWrapperDrawable(pinDrawable, this)
                }

                with(it) {
                    search_frame.alpha = this
                    searchHint.alpha = this
                    searchIv.alpha = 1 - this
                    shareIv.alpha = 1 - this
                }
            }
        }
    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        //设置初始状态
        setBackgroundColor(Color.TRANSPARENT)
        with(Color.WHITE) {
            backIv.setWrapperDrawable(backDrawable, this)
            searchIv.setWrapperDrawable(searchDrawable, this)
            shareIv.setWrapperDrawable(shareDrawable, this)
            pinIv.setWrapperDrawable(pinDrawable, this)
        }
    }
}