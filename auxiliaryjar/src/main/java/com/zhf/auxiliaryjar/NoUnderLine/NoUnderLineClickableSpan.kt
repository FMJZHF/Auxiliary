package com.zhf.auxiliaryjar.NoUnderLine

import android.content.Context
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

class NoUnderLineClickableSpan(
    context: Context,
    // 文本的颜色
    private val color: Int,
    // false 表示不显示下划线，true表示显示下划线
    private val underlineText: Boolean,
    // 根据不同的类型设置不同的事件跳转类型
    private val pairEntity: PairEntity
) : ClickableSpan() {

    private var i_ClickableSpan = context as I_ClickableSpan

    override fun onClick(widget: View) {
        if (pairEntity == null || pairEntity.clickType == "" || pairEntity.clickType == null) return
        i_ClickableSpan.OnSpanClick(pairEntity)
    }


    override fun updateDrawState(textPaint: TextPaint) {
        //设置文本的颜色
        textPaint.color = color
        //超链接形式的下划线，false 表示不显示下划线，true表示显示下划线
        textPaint.isUnderlineText = underlineText
    }
}
