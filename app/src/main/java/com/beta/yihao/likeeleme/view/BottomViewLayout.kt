package com.beta.yihao.likeeleme.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import com.beta.yihao.likeeleme.R

/**
 * @Author yihao
 * @Date 2019/1/5-21:20
 * @Email yihaobeta@163.com
 */
class BottomViewLayout(context: Context, attributeSet: AttributeSet) : ConstraintLayout(context, attributeSet) {
    init {
        LayoutInflater.from(context).inflate(R.layout.bottom_layout,this)
    }
}