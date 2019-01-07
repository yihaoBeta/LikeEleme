package com.beta.yihao.likemeituan.adapter

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.beta.yihao.likeeleme.FoodBeanWrapper
import com.beta.yihao.likeeleme.R
import com.beta.yihao.likeeleme.util.dip2px
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * @Author yihao
 * @Date 2018/12/30-20:32
 * @Email yihaobeta@163.com
 */
class DetailAdapter(data: List<FoodBeanWrapper>) :
    BaseQuickAdapter<FoodBeanWrapper, BaseViewHolder>(R.layout.food_list_item, data) {
    private var scrolling = false
    private var curIndex = 0
    override fun convert(helper: BaseViewHolder?, item: FoodBeanWrapper) {
        helper?.apply {
            setText(R.id.foodNameTv, item.name)
            setText(R.id.salesTv, item.sales)
            setText(R.id.priceTv, item.price)
            setImageResource(R.id.foodImageIv, item.image)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val llManager = recyclerView.layoutManager as LinearLayoutManager?
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (scrolling && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    scrolling = false
                    val n = curIndex - llManager!!.findFirstVisibleItemPosition()
                    if (0 <= n && n < recyclerView.childCount) {
                        val top = recyclerView.getChildAt(n).top
                        recyclerView.smoothScrollBy(0, top)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (scrolling) {
                    scrolling = false
                    val n = curIndex - llManager!!.findFirstVisibleItemPosition()
                    if (0 <= n && n < recyclerView.childCount) {
                        val top = recyclerView.getChildAt(n).top - dip2px(25.0)
                        recyclerView.scrollBy(0, top)
                    }
                }
            }
        })
    }

    fun smoothMoveToPosition(position: Int) {
        curIndex = position
        recyclerView.stopScroll()
        val llManager = recyclerView.layoutManager as LinearLayoutManager?
        val firstItem = llManager!!.findFirstVisibleItemPosition()
        val lastItem = llManager.findLastVisibleItemPosition()
        when {
            curIndex <= firstItem -> recyclerView.scrollToPosition(curIndex)
            curIndex <= lastItem -> {
                val top = recyclerView.getChildAt(curIndex - firstItem).top - dip2px(25.0)
                recyclerView.scrollBy(0, top)
            }
            else -> {
                recyclerView.scrollToPosition(curIndex)
                scrolling = true
            }
        }
    }
}