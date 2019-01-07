package com.beta.yihao.likeeleme.behavior

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View
import com.beta.yihao.likeeleme.view.FoodMainLayout
import com.beta.yihao.likeeleme.view.HeaderLayout
import com.beta.yihao.likeeleme.view.ShopInfoLayout

/**
 * @Author yihao
 * @Description HeaderLayout行为控制类
 * @Date 2019/1/2-20:08
 * @Email yihaobeta@163.com
 */
class HeaderControlBehavior(context: Context, attributeSet: AttributeSet) :
    CoordinatorLayout.Behavior<HeaderLayout>(context, attributeSet) {
    private lateinit var shopInfoLayout: ShopInfoLayout
    private var maxTranslateY = 0
    private var mCallback: ((Float) -> Unit)? = null


    override fun layoutDependsOn(parent: CoordinatorLayout, child: HeaderLayout, dependency: View): Boolean {
        if (dependency is ShopInfoLayout) {
            shopInfoLayout = dependency
            return true
        }

        if (dependency is FoodMainLayout && maxTranslateY == 0) {
            maxTranslateY = dependency.top - child.height
        }
        return super.layoutDependsOn(parent, child, dependency)
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: HeaderLayout, dependency: View): Boolean {

        if (maxTranslateY == 0) return false
        val scale = Math.abs(dependency.translationY) / maxTranslateY
        //通知HeaderLayout
        mCallback?.invoke(scale)
        return super.onDependentViewChanged(parent, child, dependency)
    }

    /**
     * 设置回调
     */
    fun setCallBack(callback: (Float) -> Unit) {
        if (this.mCallback == null)
            this.mCallback = callback
    }
}