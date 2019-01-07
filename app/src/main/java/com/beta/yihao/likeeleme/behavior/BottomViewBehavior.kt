package com.beta.yihao.likeeleme.behavior

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View
import com.beta.yihao.likeeleme.R
import com.beta.yihao.likeeleme.util.getDimen
import com.beta.yihao.likeeleme.view.BottomViewLayout
import com.beta.yihao.likeeleme.view.FoodMainLayout

/**
 * @Author yihao
 * @Description 底部视图行为控制类，跟随FoodMainLayout变化
 * @Date 2019/1/5-21:31
 * @Email yihaobeta@163.com
 */
class BottomViewBehavior(context: Context, attributeSet: AttributeSet) :
    CoordinatorLayout.Behavior<BottomViewLayout>(context, attributeSet) {
    override fun layoutDependsOn(parent: CoordinatorLayout, child: BottomViewLayout, dependency: View): Boolean {
        return dependency is FoodMainLayout
    }

    override fun onLayoutChild(parent: CoordinatorLayout, child: BottomViewLayout, layoutDirection: Int): Boolean {
        //对BottomViewLayout进行布局
        child.layout(0, parent.height - getDimen(R.dimen.bottom_view_height), parent.width, parent.height)
        return true
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: BottomViewLayout, dependency: View): Boolean {
        //跟随FoodMainLayout对BottomViewLayout进行位置变换
        if (child.visibility == View.VISIBLE) {
            child.translationY = when {
                dependency.translationY > child.height -> child.height.toFloat()
                dependency.translationY < 0 -> 0F
                else -> dependency.translationY
            }
            return true
        }
        return super.onDependentViewChanged(parent, child, dependency)
    }
}