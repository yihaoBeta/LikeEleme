package com.beta.yihao.likeeleme.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextPaint
import android.view.View
import com.beta.yihao.likeeleme.CategoryBean
import com.beta.yihao.likeeleme.util.dip2px
import com.beta.yihao.likeeleme.util.getPaintHeight
import com.beta.yihao.likeeleme.util.sp2px

/**
 * @Author yihao
 * @Description
 * 使用RecyclerView添加ItemDecoration的方式，
 * 绘制list中的分类信息，以及添加头部固定的分类信息
 * @Date 2018/12/30-21:43
 * @Email yihaobeta@163.com
 */

class FoodHeaderItemDecoration(context: Context, var categoryList: List<CategoryBean>) :
    RecyclerView.ItemDecoration() {

    private var categoryBarHeight = dip2px(25.0)
    private var categoryBarLeftPadding = 25
    private var bShowCategoryFloatingBar = true
    private val paint: Paint = Paint()
    private val textPaint = TextPaint()
    private var curCategory: String = ""
    private var clickStatus = false


    init {
        paint.apply {
            isAntiAlias = true
            color = Color.parseColor("#FFEEEEEE")
        }

        textPaint.apply {
            isAntiAlias = true
            color = Color.parseColor("#FF999999")
            textSize = sp2px(15.0).toFloat()
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (categoryList.isEmpty()) return

        val manager = parent.layoutManager
        if ((manager is LinearLayoutManager) && LinearLayoutManager.VERTICAL != manager.orientation) {
            return
        }

        val position = parent.getChildAdapterPosition(view)
        if (position == 0 || categoryList[position] != categoryList[position - 1]) {
            outRect.set(0, categoryBarHeight, 0, 0)
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (categoryList.isEmpty()) return
        for (i in 0 until parent.childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)
            val category = categoryList[position]
            if (position == 0 || category != categoryList[position - 1]) {
                drawCategoryBar(c, parent, view, category.name)
            }
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        if (categoryList.isEmpty() || !bShowCategoryFloatingBar) return

        val position = (parent.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        val category = categoryList[position]
        val view = parent.findViewHolderForAdapterPosition(position)!!.itemView
        var flag = false
        if ((position + 1) < categoryList.size && category != categoryList[position + 1]) {
            if (view.bottom <= categoryBarHeight) {
                c.save()
                flag = true
                c.translate(0f, (view.height + view.top - categoryBarHeight).toFloat())
            }
        }
        drawCategoryFloatingBar(c, parent, category)
        if (flag) {
            c.restore()
        }
        //分类更改时，添加回调
        if (curCategory != category.name && !this.clickStatus) {
            curCategory = category.name
            onCategoryChangeListener.invoke(category)
        }
    }

    fun setClickStatus(isClick: Boolean) {
        clickStatus = isClick
    }


    /**
     * 绘制list头部固定的分类条
     */
    @Synchronized
    private fun drawCategoryFloatingBar(c: Canvas, parent: RecyclerView, category: CategoryBean) {
        val rect = getCategoryFloatingBarRectLayout(parent)
        c.drawRect(rect, paint)
        val x = rect.left + categoryBarLeftPadding
        val y = rect.top + (categoryBarHeight + category.name.getPaintHeight(textPaint)) / 2
        c.drawText(category.name, x.toFloat(), y.toFloat(), textPaint)
    }

    /**
     * 绘制List列表中的分类条
     */
    private fun drawCategoryBar(c: Canvas, parent: RecyclerView, view: View?, category: String) {
        val rect = getCategoryBarRectLayout(parent, view!!)
        c.drawRect(rect, paint)
        val x = rect.left + categoryBarLeftPadding
        val y = rect.top + (categoryBarHeight + category.getPaintHeight(textPaint)) / 2
        c.drawText(category, x.toFloat(), y.toFloat(), textPaint)
    }

    private fun getCategoryBarRectLayout(parent: RecyclerView, view: View): Rect {
        val params = view.layoutParams as RecyclerView.LayoutParams
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val bottom = view.top - params.topMargin
        val top = bottom - categoryBarHeight
        return Rect(left, top, right, bottom)
    }

    private fun getCategoryFloatingBarRectLayout(parent: RecyclerView): Rect {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val bottom = categoryBarHeight
        val top = 0
        return Rect(left, top, right, bottom)
    }

    private lateinit var onCategoryChangeListener: (CategoryBean) -> Unit

    fun setOnCategoryChangeListener(listener: (CategoryBean) -> Unit) {
        this.onCategoryChangeListener = listener
    }
}