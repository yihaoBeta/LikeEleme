package com.beta.yihao.likeeleme.util

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.support.v4.graphics.drawable.DrawableCompat
import android.text.TextPaint
import android.widget.ImageView
import com.beta.yihao.likeeleme.basic.BaseApp

/**
 * @Author yihao
 * @Date 2018/12/30-16:52
 * @Email yihaobeta@163.com
 */

fun dip2px(dip: Double): Int {
    val density = BaseApp.getContext().resources.displayMetrics.density
    return (dip * density + 0.5f).toInt()
}

fun sp2px(sp: Double): Int {
    val scaledDensity = BaseApp.getContext().resources.displayMetrics.scaledDensity
    return (sp * scaledDensity + 0.5f).toInt()
}

fun px2sp(px: Int): Float {
    val scaledDensity = BaseApp.getContext().resources.displayMetrics.scaledDensity
    return (px.toFloat() / scaledDensity + 0.5f)
}

fun px2dip(px: Int): Int {
    val scale = BaseApp.getContext().resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}


fun String.getPaintHeight(paint: TextPaint): Int {
    val bounds = Rect()
    paint.getTextBounds(this, 0, this.length, bounds)
    return bounds.height()
}

fun getDimen(resId: Int): Int {
    return BaseApp.getContext().resources.getDimension(resId).toInt()
}

fun getBitmapFromRes(resId: Int): Bitmap {
    return BitmapFactory.decodeResource(BaseApp.getContext().resources, resId)
}

fun Bitmap.compress(scaleRatio: Int = 10): Bitmap {
    return Bitmap.createScaledBitmap(this, this.width / scaleRatio, this.height / scaleRatio, false)
}

/**
 * 原生API实现blur，但是只Supported Radius range 0 < radius <= 25
 */
fun Bitmap.blur(radius: Float): Bitmap {
    val rs = RenderScript.create(BaseApp.getContext())
    val allocFromBitmap = Allocation.createFromBitmap(rs, this)
    val blur = ScriptIntrinsicBlur.create(rs, allocFromBitmap.element)
    blur.setInput(allocFromBitmap)
    blur.setRadius(radius)
    blur.forEach(allocFromBitmap)
    allocFromBitmap.copyTo(this)
    rs.destroy()
    return this
}

/**
 * 使用FastBlur算法
 * 算法作者：Mario Klingemann
 */
fun Bitmap.fastBlur(radius: Float): Bitmap? {
    return FastBlur.doBlur(this, radius.toInt(), false)
}

fun Bitmap.toDrawable(): Drawable {
    return BitmapDrawable(BaseApp.getContext().resources, this)
}

/**
 * 获取状态栏高度
 */
fun getStatusBarHeight(): Int {
    var result = 0
    val resourceId = BaseApp.getContext().resources.getIdentifier(
        "status_bar_height",
        "dimen", "android"
    )

    if (resourceId > 0) {
        result = BaseApp.getContext().resources.getDimensionPixelSize(resourceId)
    }
    return result
}

fun drawableOf(resId: Int):Drawable{
    return BaseApp.getContext().resources.getDrawable(resId)
}

fun ImageView.setWrapperDrawable(drawable: Drawable, color: Int) {
    this.setImageDrawable(getDrawableOfWrapper(drawable, ColorStateList.valueOf(color)))
}

/**
 * 处理图片颜色
 */
fun getDrawableOfWrapper(drawableOrigin: Drawable, colorStateList: ColorStateList): Drawable {
    val wrap = DrawableCompat.wrap(drawableOrigin)
    DrawableCompat.setTintList(wrap, colorStateList)
    return wrap
}