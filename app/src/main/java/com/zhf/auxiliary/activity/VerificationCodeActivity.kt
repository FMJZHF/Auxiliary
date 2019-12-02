package com.zhf.auxiliary.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.zhf.auxiliary.R
import com.zhf.auxiliaryjar.verification_code.VerificationCodeView

class VerificationCodeActivity : AppCompatActivity(), View.OnClickListener {
	private var viewVerification: VerificationCodeView? = null
	private var btnSubmit: Button? = null
	private var btnClear: Button? = null
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_verification_code)
		viewVerification = findViewById(R.id.view_verification)
		btnSubmit = findViewById(R.id.btn_submit)
		btnClear = findViewById(R.id.btn_clear)

		btnSubmit!!.setOnClickListener(this)
		btnClear!!.setOnClickListener(this)
	}


	override fun onClick(v: View) {
		when (v.id) {
			R.id.btn_submit -> if (viewVerification!!.isFinish) {
				Toast.makeText(this, "输入验证码是:" + viewVerification!!.content, Toast.LENGTH_SHORT).show()
			} else {
				Toast.makeText(this, "请输入完整验证码", Toast.LENGTH_SHORT).show()
			}
			R.id.btn_clear -> viewVerification!!.clear()
		}
	}
}
