package com.beta.yihao.likeeleme.behavior

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.beta.yihao.likeeleme.R
import com.beta.yihao.likeeleme.util.BehaviorHelper
import com.beta.yihao.likeeleme.util.ScrollerHelper
import com.beta.yihao.likeeleme.util.getDimen
import com.beta.yihao.likeeleme.view.BottomViewLayout
import com.beta.yihao.likeeleme.view.FoodMainLayout
import com.beta.yihao.likeeleme.view.HeaderLayout
import com.beta.yihao.likeeleme.view.ShopInfoLayout

class ShopInfoScrollBehavior(val context: Context, attributeSet: AttributeSet) :
    CoordinatorLayout.Behavior<FoodMainLayout>(context, attributeSet) {

    private lateinit var shopInfoLayout: ShopInfoLayout
    private lateinit var avatarIv: ImageView
    private lateinit var headerLayout: HeaderLayout
    //判断是否已经进行过子view的layout操作
    private var hasLayout = false
    //ShopInfoLayout能够移动的最大距离
    private var translationYLimit = 0
    //HeaderLayout的高度
    private var headerHeight = 0
    //Header下面的商铺背景图
    private lateinit var shopBgLayout: ImageView
    private lateinit var simpleInfoLayout: ConstraintLayout
    private val scroller: ScrollerHelper
    //推荐列表最大的移动距离
    private var recommendMaxDy = 0
    private val helper = BehaviorHelper.INSTANCE
    private lateinit var recommendRv: RecyclerView
    private lateinit var foodListRootLayout: LinearLayout
    private lateinit var recommendTv: TextView
    private lateinit var bottomViewLayout: BottomViewLayout
    //初始状态
    private var state = STATE.NORMAL

    companion object {
        //FoodMainLayout的状态枚举类
        enum class STATE {
            NORMAL,
            HIDE,
            SCROLLING
        }
    }


    init {
        //scroller回调
        scroller = ScrollerHelper(context) { dy, state ->
            this.state = state
            when (this.state) {
                STATE.NORMAL -> {
                    //shopinfoLayout上面简短的信息
                    simpleInfoLayout.alpha = 1F
                    simpleInfoLayout.isClickable = true
                }
                STATE.HIDE -> {
                    simpleInfoLayout.alpha = 0F
                    simpleInfoLayout.isClickable = false
                }
                STATE.SCROLLING -> {
                    var temp = dy
                    if (temp < 0) temp = 0F
                    if (temp > BehaviorHelper.SIMPLE_INFO_LAYOUT_LIMIT) temp =
                            BehaviorHelper.SIMPLE_INFO_LAYOUT_LIMIT.toFloat()
                    simpleInfoLayout.alpha = 1 - (temp / BehaviorHelper.SIMPLE_INFO_LAYOUT_LIMIT)
                }
            }
        }
    }

    override fun layoutDependsOn(parent: CoordinatorLayout, child: FoodMainLayout, dependency: View): Boolean {
        child.addViewPagerChangeCallback {
            when (it) {
                0 -> {
                    bottomViewLayout.visibility = View.VISIBLE
                    helper.setState(BehaviorHelper.STATE_VIEW_PAGER_ORDER)
                }
                else -> {
                    bottomViewLayout.visibility = View.GONE
                    helper.removeState(BehaviorHelper.STATE_VIEW_PAGER_ORDER)
                }
            }
        }
        when (dependency) {
            is ShopInfoLayout -> {
                shopInfoLayout = dependency
                //button按键回调
                shopInfoLayout.addClickBackListener {
                    when (it) {
                        R.id.menuBackIv -> {
                            if (state == STATE.HIDE) {
                                scroller.startScrollVertical(child, dy = -child.translationY.toInt())
                            }
                        }

                        R.id.moreBtn -> {
                            if (state == STATE.NORMAL) {
                                scroller.startScrollVertical(child, dy = child.height)
                            }
                        }
                    }

                }
                avatarIv = shopInfoLayout.findViewById(R.id.shopAvatar)
            }

            is HeaderLayout -> {
                headerLayout = dependency
                return false
            }
            is BottomViewLayout -> {
                bottomViewLayout = dependency
                return false
            }
            else -> return false
        }
        return true
    }

    override fun onLayoutChild(parent: CoordinatorLayout, child: FoodMainLayout, layoutDirection: Int): Boolean {
        if (headerHeight == 0) {
            headerHeight = headerLayout.height
        }
        if (!hasLayout) {
            hasLayout = true
            //对商铺头像进行定位
            avatarIv.layout(
                avatarIv.left,
                (avatarIv.top - getDimen(R.dimen.avatar_height) * 0.8).toInt(),
                avatarIv.left + avatarIv.width,
                (avatarIv.bottom - getDimen(R.dimen.avatar_height) * 0.8).toInt()
            )
        }
        val infoContainer = shopInfoLayout.findViewById<ConstraintLayout>(R.id.infoContainer)
        simpleInfoLayout = shopInfoLayout.findViewById(R.id.simpleInfoLayout)
        shopBgLayout = shopInfoLayout.findViewById(R.id.shopBg)
        val top = infoContainer.top + simpleInfoLayout.bottom
        //对FoodMainLayout进行初始定位
        child.layout(0, top, parent.width, parent.height + top)
        recommendRv = child.findViewById(R.id.recommendRv)
        foodListRootLayout = child.findViewById(R.id.foodListRootLayout)
        recommendTv = foodListRootLayout.findViewById(R.id.recommendTv)
        recommendMaxDy = recommendTv.height + recommendRv.height

        return true
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: FoodMainLayout, dependency: View): Boolean {
        //动态改变商铺头像的缩放大小
        //换算缩放范围时以移动总距离的半程为上限
        val totalScaleHeight: Float = if (translationYLimit == 0) 1F else (translationYLimit / 2).toFloat()
        val curTransYAbs = Math.abs(dependency.translationY)
        //计算当前缩放范围的Y方向的移动值
        val curScaleHeight: Float = if (curTransYAbs > totalScaleHeight) totalScaleHeight else curTransYAbs
        //计算缩放比例
        val scale = 1 - (curScaleHeight / totalScaleHeight)
        //设置缩放
        avatarIv.apply {
            //设置缩放中心点为view下边缘中心点
            pivotX = (avatarIv.width / 2).toFloat()
            pivotY = (avatarIv.height).toFloat()
            scaleX = scale
            scaleY = scale
        }
        return true
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FoodMainLayout,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        if (target.id == R.id.recommendRv) {
            //接受推荐列表的横向RecyclerView滑动事件
            return true
        }
        return axes and ViewCompat.SCROLL_AXIS_VERTICAL != -1
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FoodMainLayout,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        if (translationYLimit <= 0) {
            translationYLimit = child.top - headerHeight
        }
        //自动移动中，消耗所有的dx,dy
        if (state == STATE.SCROLLING) {
            consumed[0] = dx
            consumed[1] = dy
            return
        }

        if (dy > 0) {//向上滑动
            if (!helper.hasState(BehaviorHelper.STATE_TAB_LAYOUT_REACH_TOP)) {//TabLayout没有到达最高点
                //移动TabLayout到最高点
                handMoveUpBoth(child, dy)
                consumed[1] = dy
                return
            } else if (!helper.hasState(BehaviorHelper.STATE_RECOMMEND_REACH_TOP) && helper.hasState(BehaviorHelper.STATE_VIEW_PAGER_ORDER)) {
                //移动recommendLayout到最高点
                foodListRootLayout.translationY =
                        calcRealDyWithRecommendLayoutOnly(foodListRootLayout.translationY - dy, true)
                consumed[1] = dy
                return
            }
        } else {//向下滑动
            if (target == recommendRv) {//触摸焦点在recommendLayout上面
                if (!helper.hasState(BehaviorHelper.STATE_RECOMMEND_REACH_BOTTOM) && helper.hasState(BehaviorHelper.STATE_VIEW_PAGER_ORDER)) {//recommendLayout没有到达最低点
                    //移动recommend到最低点
                    foodListRootLayout.translationY =
                            calcRealDyWithRecommendLayoutOnly(foodListRootLayout.translationY - dy, false)
                    consumed[1] = dy
                    return
                } else {//if (!helper.hasState(BehaviorHelper.STATE_TAB_LAYOUT_REACH_BOTTOM)) {//tabLayout没有到达最低点
                    //移动tabLayout到最低点
                    handMoveDownBoth(child, dy)
                    consumed[1] = dy
                    return
                }
            }
        }
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FoodMainLayout,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        //列表已经滑动到第一个Item,且继续滑动
        if (dyUnconsumed < 0 && type == 0) {
            if (!helper.hasState(BehaviorHelper.STATE_RECOMMEND_REACH_BOTTOM) && helper.hasState(BehaviorHelper.STATE_VIEW_PAGER_ORDER)) {
                //移动foodListRootLayout到初始位
                foodListRootLayout.translationY =
                        calcRealDyWithRecommendLayoutOnly(foodListRootLayout.translationY - dyUnconsumed, false)
            } else {
                //整体下移
                handMoveDownBoth(child, dyUnconsumed)
            }
        }
    }


    private fun handMoveDownBoth(viewToMove: View, dy: Int) {
        //此时FoodMainLayout处于向上展开的状态,需要FoodMainLayout和ShopInfoLayout归位
        viewToMove.translationY.let {
            if (it < 0) {
                val realDy = calcRealDyBothFoodMainAndShopInfoLayout(shopInfoLayout.translationY - dy, false)
                shopInfoLayout.translationY = realDy
                //背景图保持不动
                viewToMove.translationY = realDy
                shopBgLayout.translationY = -realDy
            } else {
                //FoodMainLayout继续下滑
                val realY = calcRealDyWithFoodMainLayoutOnly(it - dy, false)
                viewToMove.translationY = realY
                if (realY < BehaviorHelper.SIMPLE_INFO_LAYOUT_LIMIT) {
                    simpleInfoLayout.alpha = 1 - (realY / BehaviorHelper.SIMPLE_INFO_LAYOUT_LIMIT)
                }
            }
        }

    }

    private fun handMoveUpBoth(viewToMove: View, dy: Int) {
        viewToMove.translationY.let {
            if (it <= 0) {
                val realDy = calcRealDyBothFoodMainAndShopInfoLayout(shopInfoLayout.translationY - dy, true)
                shopInfoLayout.translationY = realDy
                viewToMove.translationY = realDy
                //背景图保持原位不动
                shopBgLayout.translationY = -realDy
            } else if (it > 0) {//FoodMainLayout处于原始位置之下，此时上滑需要将FoodMainLayout首先归位
                val realY = calcRealDyWithFoodMainLayoutOnly(it - dy, true)
                viewToMove.translationY = realY
                if (realY < BehaviorHelper.SIMPLE_INFO_LAYOUT_LIMIT) {
                    simpleInfoLayout.alpha = 1 - (realY / BehaviorHelper.SIMPLE_INFO_LAYOUT_LIMIT)
                }
            }
        }
    }

    /**
     * 列表和shopInfo界面一起移动时，用来计算真正需要移动的y方向的数值，已避免滑动越界
     * @param isUp true:向上滑动,false:向下滑动
     * @param intentToTranslateDy 将要进行的translateY值
     */
    private fun calcRealDyBothFoodMainAndShopInfoLayout(intentToTranslateDy: Float, isUp: Boolean): Float {
        return intentToTranslateDy.let {
            if (isUp) {
                if (it < -translationYLimit) {
                    helper.setState(BehaviorHelper.STATE_TAB_LAYOUT_REACH_TOP)
                    -translationYLimit.toFloat()
                } else {
                    helper.removeTabLayoutState()
                    it
                }
            } else {
                if (it > 0) {
                    helper.setState(BehaviorHelper.STATE_TAB_LAYOUT_REACH_BOTTOM)
                    0F
                } else {
                    helper.removeTabLayoutState()
                    it
                }
            }
        }
    }

    /**
     * 只计算推荐列表，以避免越界
     */
    private fun calcRealDyWithRecommendLayoutOnly(intentToTranslateDy: Float, isUp: Boolean): Float {
        return intentToTranslateDy.let {
            if (isUp) {
                if (it < -recommendMaxDy) {
                    helper.setState(BehaviorHelper.STATE_RECOMMEND_REACH_TOP)
                    -recommendMaxDy.toFloat()
                } else {
                    helper.removeRecommendLayoutState()
                    it
                }
            } else {
                if (it > 0) {
                    helper.setState(BehaviorHelper.STATE_RECOMMEND_REACH_BOTTOM)
                    0F
                } else {
                    helper.removeRecommendLayoutState()
                    it
                }
            }
        }
    }

    /**
     * 列表单独移动时，用来计算真正需要移动的y方向的数值，以避免滑动越界
     * @param isUp true:向上滑动,false:向下滑动
     * @param intentToTranslateDy 将要进行的translateY值
     */
    private fun calcRealDyWithFoodMainLayoutOnly(intentToTranslateDy: Float, isUp: Boolean): Float {
        return intentToTranslateDy.let {
            if (isUp) {
                if (it < 0) {
                    0F
                } else {
                    it
                }
            } else {
                it
            }
        }
    }


    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FoodMainLayout,
        target: View,
        type: Int
    ) {
        when (type) {
            0 -> {
                if (state == STATE.NORMAL) {
                    val transY = child.translationY
                    if (transY > 0 && transY < BehaviorHelper.SCROLL_LIMIT) {//自动滑动到初始位置
                        state = STATE.SCROLLING
                        scroller.startScrollVertical(child, dy = -transY.toInt())
                    } else if (transY > BehaviorHelper.SCROLL_LIMIT) {//自动滑出屏幕
                        state = STATE.SCROLLING
                        scroller.startScrollVertical(child, dy = (coordinatorLayout.height - transY).toInt())
                    }
                }
            }
        }
        super.onStopNestedScroll(coordinatorLayout, child, target, type)
    }
}