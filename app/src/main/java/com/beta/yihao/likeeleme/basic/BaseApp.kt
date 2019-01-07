package com.beta.yihao.likeeleme.basic

import android.app.Application

/**
 * @Author yihao
 * @Date 2019/1/1-17:28
 * @Email yihaobeta@163.com
 */
class BaseApp : Application() {

    companion object {
        private lateinit var INSTANCE: Application
        @Synchronized
        fun getContext() = INSTANCE
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}