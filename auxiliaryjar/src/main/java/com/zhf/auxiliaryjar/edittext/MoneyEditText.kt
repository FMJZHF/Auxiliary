package com.zhf.auxiliaryjar.edittext

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet

/**
 *
 * 自定义可输入金额的EditText
 *
 * 【注意】在布局文件中设置 maxLength=12，是不能限制输入字符的最大长度，需要在代码中设置
 *  var etTradMoney:MoneyEditText
 *  //即限定最大输入字符数为12
 *  etTradMoney.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)})
 *
 */

class MoneyEditText @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatEditText(context, attrs, defStyleAttr) {
	private var textChange: Boolean = false

	/**
	 * 获取金额
	 */
	//如果最后一位是小数点
	val moneyText: String
		get() {
			val money = text!!.toString()
			return if (money.endsWith(".")) {
				money.substring(0, money.length - 1)
			} else money
		}

	init {
		//设置可以输入小数
		inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
		isFocusable = true
		isFocusableInTouchMode = true
		//监听文字变化
		addTextChangedListener(object : TextWatcher {

			override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

			}

			override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
										   after: Int) {

			}

			override fun afterTextChanged(s: Editable) {
				if (!textChange) {
					restrictText()
				}
				textChange = false
			}
		})
	}


	/**
	 * 将小数限制为2位
	 */
	private fun restrictText() {
		var input = text!!.toString()
		if (TextUtils.isEmpty(input)) {
			return
		}
		if (input.contains(".")) {
			val pointIndex = input.indexOf(".")
			val totalLenth = input.length
			val len = totalLenth - 1 - pointIndex
			if (len > 2) {
				input = input.substring(0, totalLenth - 1)
				textChange = true
				setText(input)
				setSelection(input.length)
			}
		}

		if (input.trim { it <= ' ' }.substring(0) == ".") {
			input = "0$input"
			setText(input)
			setSelection(2)
		}

	}

	companion object {
		private val TAG = "MoneyEditText"
	}
}