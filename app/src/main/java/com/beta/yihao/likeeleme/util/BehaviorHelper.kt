package com.beta.yihao.likeeleme.util

/**
 * @Author yihao
 * @Description 用来记录一些坐标移动过程中的状态
 * @Date 2019/1/5-15:21
 * @Email yihaobeta@163.com
 */
class BehaviorHelper private constructor() {
    private var currentState = 0L

    companion object {
        private const val INITIAL: Long = 1

        const val STATE_TAB_LAYOUT_REACH_TOP = INITIAL shl 1
        const val STATE_RECOMMEND_REACH_TOP = INITIAL shl 2
        const val STATE_TAB_LAYOUT_REACH_BOTTOM = INITIAL shl 3
        const val STATE_RECOMMEND_REACH_BOTTOM = INITIAL shl 4
        //Tab页处于OrderLayout
        const val STATE_VIEW_PAGER_ORDER = INITIAL shl 5


        //FoodMainLayout自动下滑或归位的垂直方向移动界限
        val SCROLL_LIMIT = dip2px(120.0)
        val SIMPLE_INFO_LAYOUT_LIMIT = dip2px(100.0)
        val INSTANCE: BehaviorHelper by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            BehaviorHelper()
        }
    }

    init {
        //设置初始状态
        setState(BehaviorHelper.STATE_TAB_LAYOUT_REACH_BOTTOM)
        setState(BehaviorHelper.STATE_RECOMMEND_REACH_BOTTOM)
        setState(BehaviorHelper.STATE_VIEW_PAGER_ORDER)
    }

    fun hasState(state: Long): Boolean {
        return currentState and state != 0L
    }

    fun setState(state: Long): Long {
        if (hasState(state)) return currentState
        currentState = currentState or state
        return getAllState()
    }

    fun removeState(state: Long): Long {
        if (!hasState(state)) return currentState
        currentState = currentState xor state
        return getAllState()
    }

    /**
     * 移除所有关于RecommendLayout的状态
     */
    fun removeRecommendLayoutState(){
        removeState(BehaviorHelper.STATE_RECOMMEND_REACH_BOTTOM)
        removeState(BehaviorHelper.STATE_RECOMMEND_REACH_TOP)
    }

    /**
     * 移除所有关于TabLayout的状态
     */
    fun removeTabLayoutState(){
        removeState(BehaviorHelper.STATE_TAB_LAYOUT_REACH_BOTTOM)
        removeState(BehaviorHelper.STATE_TAB_LAYOUT_REACH_TOP)
    }

    private fun getAllState(): Long = currentState

}