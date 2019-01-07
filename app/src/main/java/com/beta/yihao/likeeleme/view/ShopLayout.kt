package com.beta.yihao.likeeleme.view

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.beta.yihao.likeeleme.R

/**
 * @Author yihao
 * @Date 2019/1/1-19:33
 * @Email yihaobeta@163.com
 */
class ShopLayout(context: Context):LinearLayout(context){
    init {
        LayoutInflater.from(context).inflate(R.layout.shop_layout,this)
    }
}