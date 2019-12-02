package com.zhf.auxiliaryjar.verification_code

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputConnectionWrapper


class WiseEditText : AppCompatEditText {


	private var keyListener: OnKeyListener? = null

	constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

	constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

	constructor(context: Context) : super(context) {}

	override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection {
		return MyInputConnection(super.onCreateInputConnection(outAttrs),
				true)
	}

	private inner class MyInputConnection(target: InputConnection, mutable: Boolean) : InputConnectionWrapper(target, mutable) {

		override fun sendKeyEvent(event: KeyEvent): Boolean {
			if (keyListener != null) {
				keyListener!!.onKey(this@WiseEditText, event.keyCode, event)
			}
			return super.sendKeyEvent(event)
		}

		override fun deleteSurroundingText(beforeLength: Int, afterLength: Int): Boolean {
			// magic: in latest Android, deleteSurroundingText(1, 0) will be called for backspace
			return if (beforeLength == 1 && afterLength == 0) {
				// backspace
				sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL)) && sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL))
			} else super.deleteSurroundingText(beforeLength, afterLength)

		}

	}

	//设置监听回调
	fun setSoftKeyListener(listener: OnKeyListener) {
		keyListener = listener
	}

}