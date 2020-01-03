package com.zhf.auxiliary.demo

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zhf.auxiliary.R
import com.zhf.auxiliaryjar.NoUnderLine.I_ClickableSpan
import com.zhf.auxiliaryjar.NoUnderLine.NoUnderLineTextUtil
import com.zhf.auxiliaryjar.NoUnderLine.PairEntity
import kotlinx.android.synthetic.main.activity_no_under_line_text.*

class NoUnderLineTextActivity : AppCompatActivity(), I_ClickableSpan {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_under_line_text)


        var text = "我已阅读并同意用户协议和隐私政策\n以及官网认证服务条款"

        val pairEntity1 = PairEntity("用户协议", "1")
        val pairEntity2 = PairEntity("隐私政策", "2")
        val pairEntity3 = PairEntity("官网认证服务条款", "3")


        // 没有下划线
        NoUnderLineTextUtil.setTextNoUnderLine(
            this,
            noUnderLineTextTv,
            text,
            Color.parseColor("#3BAC6A"),
            pairEntity1 , pairEntity2, pairEntity3
        )

        // 含有下划线
        NoUnderLineTextUtil.setTextUnderLine(
            this,
            underLineTextTv,
            text,
            Color.parseColor("#3BAC6A"),
            pairEntity1
        )


    }

    /**
     * 不同类型的点击事件
     */
    override fun OnSpanClick(pairEntity: PairEntity) {

        var type = pairEntity.clickType
        when (type) {
            "1" -> showHint("点击了："+pairEntity.clickText)
            "2" -> showHint("点击了："+pairEntity.clickText)
            "3" -> showHint("点击了："+pairEntity.clickText)
            else -> showHint("未设置点击事件")
        }

    }

    private fun showHint(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }


}
