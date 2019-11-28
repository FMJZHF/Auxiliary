package com.zhf.auxiliary.app

import android.app.Application

import com.zhf.auxiliary.util.ToastUtils


class MyApp : Application() {
	override fun onCreate() {
		super.onCreate()
		init()
	}

	private fun init() {
		ToastUtils.init(this)
	}
}
