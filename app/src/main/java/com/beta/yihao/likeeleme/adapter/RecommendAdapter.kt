package com.beta.yihao.likeeleme.adapter

import com.beta.yihao.likeeleme.FoodBean
import com.beta.yihao.likeeleme.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * @Author yihao
 * @Date 2019/1/1-20:27
 * @Email yihaobeta@163.com
 */
class RecommendAdapter(data:List<FoodBean>) : BaseQuickAdapter<FoodBean, BaseViewHolder>(R.layout.recommend_item,data) {
    override fun convert(helper: BaseViewHolder?, item: FoodBean?) {
        helper?.apply {
            setText(R.id.recommendName,item!!.name)
            setImageResource(R.id.recommendIv,item.image)
            setText(R.id.salesTv, item.sales)
            setText(R.id.priceTv, item.price)
        }
    }

}