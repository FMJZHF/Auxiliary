package com.zhf.auxiliary.util

import android.content.Context
import android.widget.Toast

object ToastUtils {
	private var sContext: Context? = null
	private var sToast: Toast? = null

	fun init(context: Context) {
		sContext = context.applicationContext
	}

	fun show(resId: Int) {
		show(sContext!!.getString(resId))
	}

	fun show(text: String) {
		if (sToast == null) {
			sToast = Toast.makeText(sContext, text, Toast.LENGTH_SHORT)
		} else {
			sToast!!.setText(text)
		}
		sToast!!.show()
	}
}
