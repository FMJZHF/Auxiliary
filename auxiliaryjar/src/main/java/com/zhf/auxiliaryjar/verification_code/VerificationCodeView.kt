package com.zhf.auxiliaryjar.verification_code

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.zhf.auxiliaryjar.R


/**
 * 验证码输入框
 *
 *  使用方法：
 *  xmlns:app="http://schemas.android.com/apk/res-auto"
 *
 *  <!--输入框文字大小-->
 *  app:vcv_code_size="12sp"
 *  <!--输入框获取焦点时边框-->
 *  app:vcv_code_bg_focus="@drawable/bg_text_focused"
 *  <!--输入框没有焦点时边框-->
 *  app:vcv_code_bg_normal="@drawable/bg_text_normal"
 *  <!--输入框文字颜色-->
 *  app:vcv_code_color="@color/text_border_focused"
 *  <!--输入框的数量 默认为6-->
 *  app:vcv_code_number="4"
 *  <!--单个验证码的宽度-->
 *  app:vcv_code_width="50dp"
 *  <!--是否展示密码样式，默认false-->
 *  app:vcv_code_input_style="true"
 */
class VerificationCodeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {
	
	//当前验证码View用来展示验证码的TextView数组，数组个数由codeNum决定
	private var textViews: Array<TextView?>? = null
	//用来输入验证码的EditText输入框，输入框会跟随输入个数移动，显示或者隐藏光标等
	private var editText: WiseEditText? = null
	//当前验证码View展示验证码个数
	private var codeNum: Int = 0
	//每个TextView的宽度
	private var codeWidth: Float = 0.toFloat()
	//字体颜色
	private var textColor: Int = 0
	//字体大小
	private var textSize: Int = 0
	//每个单独验证码的背景
	private var textDrawable: Drawable? = null
	//验证码选中时的背景
	private var textFocusedDrawable: Drawable? = null
	//是否展示密码样式
	private var showPassword = false
	//验证码之间间隔
	private var dividerWidth = 0f
	//对EditText输入进行监听
	private var watcher: TextWatcher? = null
	//监听删除键和enter键
	private var onKeyDownListener: OnKeyListener? = null
	
	//当前选中的TextView位置，即光标所在位置
	private var currentFocusPosition = 0

	private lateinit var inputOver: I_inputOver

	/**
	 * 获取输入的验证码
	 *
	 * @return
	 */
	val getContent: String
		get() {
			val builder = StringBuilder()
			for (tv in textViews!!) {
				builder.append(tv!!.text)
			}
			return builder.toString()
		}
	
	/**
	 * 判断是否验证码输入完毕
	 *
	 * @return
	 */
	val isFinish: Boolean
		get() {
			for (tv in textViews!!) {
				if (TextUtils.isEmpty(tv!!.text)) {
					return false
				}
			}
			return true
		}


	fun setInputOver(inputOver: I_inputOver) {
		this.inputOver = inputOver
	}

	init {
		init(context, attrs, defStyleAttr)
	}
	
	/**
	 * 初始化View
	 *
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 */
	@SuppressLint("NewApi")
	private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
		val array = context.obtainStyledAttributes(attrs, R.styleable.VerificationCodeView, defStyleAttr, 0)
		codeNum = array.getInteger(R.styleable.VerificationCodeView_vcv_code_number, 6)
		codeWidth = array.getDimensionPixelSize(R.styleable.VerificationCodeView_vcv_code_width, dip2px(40f, context).toInt()).toFloat()
		textColor = array.getColor(R.styleable.VerificationCodeView_vcv_code_color, resources.getColor(R.color.text_border_focused))
		textSize = array.getDimensionPixelSize(R.styleable.VerificationCodeView_vcv_code_size, resources.getDimensionPixelOffset(R.dimen.text_size))
		textDrawable = array.getDrawable(R.styleable.VerificationCodeView_vcv_code_bg_normal)
		textFocusedDrawable = array.getDrawable(R.styleable.VerificationCodeView_vcv_code_bg_focus)
		showPassword = array.getBoolean(R.styleable.VerificationCodeView_vcv_code_input_style, false)
		array.recycle()
		//若未设置选中和未选中状态，设置默认
		if (textDrawable == null) {
			textDrawable = resources.getDrawable(R.drawable.bg_text_normal)
		}
		if (textFocusedDrawable == null) {
			textFocusedDrawable = resources.getDrawable(R.drawable.bg_text_focused)
		}
		
		initView(context)
		initListener()
		resetCursorPosition()
	}
	
	/**
	 * 初始化各View并加入当前View中
	 */
	private fun initView(context: Context) {
		//初始化TextView数组
		textViews = arrayOfNulls(size = codeNum)
		for (i in 0 until codeNum) {
			//循环加入数组中，设置TextView字体大小和颜色，并将TextView依次加入LinearLayout
			val textView = TextView(context)
			textView.gravity = Gravity.CENTER
			textView.setTextColor(textColor)
			textView.textSize = textSize.toFloat()
			textView.includeFontPadding = false
			if (showPassword) {
				//数字密码样式，可更改
				textView.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
			}
			textViews!![i] = textView
			this.addView(textView)
			val params = textView.layoutParams as RelativeLayout.LayoutParams
			params.addRule(RelativeLayout.CENTER_VERTICAL)
			params.width = codeWidth.toInt()
			params.height = codeWidth.toInt()
		}
		//初始化EditText，设置背景色为透明，获取焦点，设置光标颜色，设置输入类型等
		editText = WiseEditText(context)
		editText!!.setBackgroundColor(Color.TRANSPARENT)
		editText!!.requestFocus()
		editText!!.inputType = InputType.TYPE_CLASS_NUMBER
		setCursorRes(R.drawable.cursor)
		addView(editText, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
	}
	
	/**
	 * 监听EditText输入字符，监听键盘删除键
	 */
	private fun initListener(){
		watcher = object : TextWatcher {
			override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
			
			}
			
			override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
			
			}
			
			override fun afterTextChanged(s: Editable) {
				val content = s.toString()
				if (!TextUtils.isEmpty(content)) {
					if (content.length == 1) {
						setText(content)
					}
					if (currentFocusPosition == textViews!!.size) {
						// 输入完成
						inputOver.inputCodeOver(getContent)
					}
					editText!!.setText("")
				}
			}
		}
		editText!!.addTextChangedListener(watcher)
		
		onKeyDownListener = OnKeyListener { v, keyCode, event ->
			if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
				deleteCode()
				return@OnKeyListener true
			}
			return@OnKeyListener false
		}
		editText!!.setSoftKeyListener(onKeyDownListener!!)
	}
	
	/**
	 * 重设EditText的光标位置，以及选中TextView的边框颜色
	 */
	
	@SuppressLint("NewApi")
	private fun resetCursorPosition() {
		for (i in 0 until codeNum) {
			val textView = textViews!![i]
			if (i == currentFocusPosition) {
				textView!!.background = textFocusedDrawable
			} else {
				textView!!.background = textDrawable
			}
		}
		if (codeNum > 1) {
			if (currentFocusPosition < codeNum) {
				//字数小于总数，设置EditText的leftPadding，造成光标移动的错觉
				editText!!.isCursorVisible = true
				val leftPadding = codeWidth / 2 + currentFocusPosition * codeWidth + currentFocusPosition * dividerWidth
				editText!!.setPadding(leftPadding.toInt(), 0, 0, 0)
			} else {
				//字数大于总数，隐藏光标
				editText!!.isCursorVisible = false
			}
		}
	}
	
	/**
	 * 删除键按下
	 */
	private fun deleteCode() {
		if (currentFocusPosition == 0) {
			//当前光标位置在0，直接返回
			return
		} else {
			//当前光标不为0，当前光标位置-1，将当前光标位置TextView置为""，重设光标位置
			currentFocusPosition--
			textViews!![currentFocusPosition]!!.text = ""
			resetCursorPosition()
		}
	}
	
	/**
	 * onMeasure后获取到测量的控件宽度，计算出每个Code之间的间隔
	 */
	private fun layoutTextView() {
		//获取控件剩余宽度
		val availableWidth = (measuredWidth - paddingLeft - paddingRight).toFloat()
		if (codeNum > 1) {
			//计算每个Code之间的间距
			dividerWidth = (availableWidth - codeWidth * codeNum) / (codeNum - 1)
			for (i in 1 until codeNum) {
				val leftMargin = codeWidth * i + dividerWidth * i
				val params1 = textViews!![i]!!.layoutParams as RelativeLayout.LayoutParams
				params1.leftMargin = leftMargin.toInt()
			}
		}
		
		//设置EditText宽度从第一个Code左侧到最后一个Code右侧，设置高度为Code高度
		//设置EditText为纵向居中
		editText!!.width = availableWidth.toInt()
		editText!!.height = codeWidth.toInt()
		val params = editText!!.layoutParams as RelativeLayout.LayoutParams
		params.addRule(RelativeLayout.CENTER_VERTICAL)
	}
	
	/**
	 * 拦截到EditText输入字符，发送给该方法进行处理
	 *
	 * @param s
	 */
	private fun setText(s: String) {
		if (currentFocusPosition >= codeNum) {
			//光标已经隐藏，直接返回
			return
		}
		//设置字符给当前光标位置的TextView，光标位置后移，重设光标状态
		textViews!![currentFocusPosition]!!.text = s
		currentFocusPosition++
		resetCursorPosition()
	}
	
	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		var heightMeasureSpec = heightMeasureSpec
		val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
		if (heightMode == View.MeasureSpec.AT_MOST) {
			//若高度是wrap_content，则设置为50dp
			heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(dip2px(80f, context).toInt(), View.MeasureSpec.EXACTLY)
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec)
	}
	
	override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
		super.onLayout(changed, l, t, r, b)
		//获取到宽高，可以对TextView进行摆放
		if (dividerWidth == 0f) {
			layoutTextView()
		}
	}
	
	private fun dip2px(dp: Float, context: Context): Float {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dp, context.resources.displayMetrics)
	}
	
	/**
	 * 暴露公共方法，设置光标颜色
	 *
	 * @param drawableRes
	 */
	fun setCursorRes(@DrawableRes drawableRes: Int) {
		try {
			val f = TextView::class.java.getDeclaredField("mCursorDrawableRes")
			f.isAccessible = true
			f.set(editText, drawableRes)
		} catch (e: Exception) {
		}
		
	}
	
	/**
	 * 清除已输入验证码
	 */
	fun clear() {
		for (tv in textViews!!) {
			tv!!.text = ""
		}
		currentFocusPosition = 0
		resetCursorPosition()
	}

	// 输入结束后的监听
	interface I_inputOver {
		fun inputCodeOver(code: String)
	}
}
