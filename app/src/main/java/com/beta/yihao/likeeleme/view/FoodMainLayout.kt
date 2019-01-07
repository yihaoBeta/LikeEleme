package com.beta.yihao.likeeleme.view

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.beta.yihao.likeeleme.R
import com.beta.yihao.likeeleme.adapter.FoodViewPagerAdapter
import com.beta.yihao.likeeleme.behavior.ShopInfoScrollBehavior
import kotlinx.android.synthetic.main.food_tab.view.*

/**
 * @Author yihao
 * @Date 2019/1/1-17:40
 * @Email yihaobeta@163.com
 */
class FoodMainLayout(context: Context, attributeSet: AttributeSet) : LinearLayout(context, attributeSet) {
    private lateinit var behavior: ShopInfoScrollBehavior

    init {
        LayoutInflater.from(context).inflate(R.layout.food_tab, this)
    }

    private fun initViews() {
        val adapter = FoodViewPagerAdapter(context)
        foodViewPager.adapter = adapter
        foodViewPager.offscreenPageLimit = 2
        foodTabLayout.setupWithViewPager(foodViewPager)
        foodViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(position: Int) {
                mVpCallback?.invoke(position)
            }
        })
    }

    override fun onFinishInflate() {
        initViews()
        super.onFinishInflate()
    }

    private var mVpCallback: ((Int) -> Unit)? = null

    fun addViewPagerChangeCallback(callback: (Int) -> Unit) {
        if (this.mVpCallback == null)
            this.mVpCallback = callback
    }
}