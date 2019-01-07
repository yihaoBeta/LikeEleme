package com.beta.yihao.likeeleme.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import com.beta.yihao.likeeleme.R
import com.beta.yihao.likeeleme.util.compress
import com.beta.yihao.likeeleme.util.fastBlur
import com.beta.yihao.likeeleme.util.getBitmapFromRes
import com.beta.yihao.likeeleme.util.toDrawable
import kotlinx.android.synthetic.main.shop_info_layout.view.*

/**
 * @Author yihao
 * @Date 2019/1/2-19:00
 * @Email yihaobeta@163.com
 */
class ShopInfoLayout(context: Context, attributeSet: AttributeSet) : ConstraintLayout(context, attributeSet) {
    private var mListener: ((viewId: Int) -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.shop_info_layout, this)
        menuBackIv.setOnClickListener {
            mListener?.invoke(it.id)
        }
        moreBtn.setOnClickListener {
            mListener?.invoke(it.id)
        }

    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        //对背景图进行blur虚化
        shopBg.setImageDrawable(getBitmapFromRes(R.drawable.shop_bg).compress(2).fastBlur(50f)?.toDrawable())
    }

    //添加Button按键的回调
    fun addClickBackListener(listener: (viewId: Int) -> Unit) {
        this.mListener = listener
    }
}