package com.beta.yihao.likeeleme.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.beta.yihao.likeeleme.R
import com.beta.yihao.likeeleme.basic.BaseApp
import com.beta.yihao.likeeleme.view.CommentLayout
import com.beta.yihao.likeeleme.view.OrderLayout
import com.beta.yihao.likeeleme.view.ShopLayout

/**
 * @Author yihao
 * @Date 2019/1/1-18:45
 * @Email yihaobeta@163.com
 */
class FoodViewPagerAdapter(context: Context) : PagerAdapter() {
    private var tabNames = BaseApp.getContext().resources.getStringArray(R.array.tab_names)
    private val orderLayout = OrderLayout(context)
    private val commentLayout = CommentLayout(context)
    private val shopLayout = ShopLayout(context)
    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == p1
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount() = tabNames.size

    override fun getPageTitle(position: Int): CharSequence? {
        if (position <= tabNames.size) {
            return tabNames[position]
        }
        return super.getPageTitle(position)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val contentView: View = when (position) {
            0 -> orderLayout
            1 -> commentLayout
            2 -> shopLayout
            else -> throw IllegalStateException("Error")
        }
        container.addView(contentView)
        return contentView
    }
}