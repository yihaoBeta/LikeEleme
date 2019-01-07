package com.beta.yihao.likeeleme.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager

/**
 * @Author yihao
 * @Date 2019/1/2-13:40
 * @Email yihaobeta@163.com
 */

object StatusBarUtil {
    fun setStatusBarTransparent(context: Context, fitSystem: Boolean) {
        if (context !is Activity) {
            throw IllegalArgumentException("context must be activity")
        }
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            context.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            context.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            context.window.statusBarColor = Color.TRANSPARENT
        }
        //设置contentview为fitsSystemWindows
        val viewGroup = context.findViewById<ViewGroup>(android.R.id.content)
        val root = viewGroup.getChildAt(0)
        if (root != null) {
            root.fitsSystemWindows = fitSystem
        }
    }

    fun getStatusBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return context.resources.getDimensionPixelSize(resourceId)
    }

    fun setStatusBarTheme(context: Context, dark: Boolean) {
        if (context !is Activity) return
        val decor = context.window.decorView
        if (dark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decor.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        } else {
            decor.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }
}