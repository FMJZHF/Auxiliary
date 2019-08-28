package com.zhf.auxiliary

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.zhf.auxiliary.demo.NoUnderLineTextActivity
import com.zhf.auxiliary.util.DialogUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 含有 下划线 或 不含有下划线的 TextView
        noUnderLineTextBtn.setOnClickListener {
            startActivity(Intent(this, NoUnderLineTextActivity::class.java))
        }

        // 时间选择框
        wheelPickerBtn.setOnClickListener {

            DialogUtil.showDialog(this@MainActivity)

        }






    }
}
