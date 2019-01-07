package com.beta.yihao.likemeituan.adapter

import android.support.v7.widget.LinearLayoutManager
import com.beta.yihao.likeeleme.CategoryBean
import com.beta.yihao.likeeleme.R
import com.beta.yihao.likeeleme.basic.BaseApp
import com.beta.yihao.likeeleme.util.dip2px
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * @Author yihao
 * @Date 2018/12/30-19:52
 * @Email yihaobeta@163.com
 */
class CategoryAdapter(data: List<CategoryBean>) :
    BaseQuickAdapter<CategoryBean, BaseViewHolder>(R.layout.category_item, data) {
    private var curCategory = 0
    override fun convert(helper: BaseViewHolder?, item: CategoryBean) {
        helper?.apply {
            setText(R.id.categoryName, item.name)
            setTag(R.id.itemParent, item.id)
            if (item.id == curCategory) {
                setBackgroundColor(R.id.itemParent, BaseApp.getContext().getColor(R.color.colorWhite))
            } else {
                setBackgroundColor(R.id.itemParent, BaseApp.getContext().getColor(R.color.colorCategoryItemNormal))
            }
        }
    }

    fun changeCategory(categoryBean: CategoryBean) {
        curCategory = categoryBean.id
        notifyDataSetChanged()
        moveToCenter(categoryBean.id)
    }

    private fun moveToCenter(position:Int){
        //将点击的position转换为当前屏幕上可见的item的位置以便于计算距离顶部的高度，从而进行移动居中
        val llManager = recyclerView.layoutManager as LinearLayoutManager?
        val childAt = recyclerView.getChildAt(position - llManager!!.findFirstVisibleItemPosition())
        if (childAt != null) {
            val y = childAt.top - recyclerView.height / 2 - dip2px(25.0)
            recyclerView.smoothScrollBy(0, y)
        }
    }
}