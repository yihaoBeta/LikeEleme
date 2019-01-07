package com.beta.yihao.likeeleme.basic

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.beta.yihao.likeeleme.util.StatusBarUtil

/**
 * @Author yihao
 * @Date 2019/1/1-17:32
 * @Email yihaobeta@163.com
 */
abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(attachLayout())
        StatusBarUtil.setStatusBarTransparent(this,false)
        StatusBarUtil.setStatusBarTheme(this,true)
        initViews()
    }

    abstract fun attachLayout(): Int
    abstract fun initViews()
}