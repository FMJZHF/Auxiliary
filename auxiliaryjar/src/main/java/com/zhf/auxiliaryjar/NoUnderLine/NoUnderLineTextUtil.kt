package com.zhf.auxiliaryjar.NoUnderLine

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.widget.TextView

/**
 * 含有下划线 或者 无下划线的操作类
 * Created by zhf on 2019/8/25 19:25
 */
object NoUnderLineTextUtil {

    /**
     * 设置点击文字含有下划线
     * @param context
     * @param textView TextView 控件
     * @param text
     * @param color 点击文字的字体色值
     * @param entities PairEntity
     */
    fun setTextUnderLine(
        context: Context,
        textView: TextView,
        text: String,
        color: Int,
        vararg entities: PairEntity
    ) {
        setText(context, textView, text, color, true, *entities)
    }

    /**
     * 设置点击文字没有下划线
     * @param context
     * @param textView TextView 控件
     * @param text
     * @param color 点击文字的字体色值
     * @param entities PairEntity
     */
    fun setTextNoUnderLine(
        context: Context,
        textView: TextView,
        text: String,
        color: Int,
        vararg entities: PairEntity
    ) {
        setText(context, textView, text, color, false, *entities)
    }

    /**
     * 设置点击文字样式
     * @param context
     * @param textView TextView 控件
     * @param text 显示的文字
     * @param color 点击文字的字体色值
     * @param underlineText  false 表示不显示下划线，true表示显示下划线
     * @param entities PairEntity
     */
   private  fun setText(
        context: Context,
        textView: TextView,
        text: String,
        color: Int,
        underlineText: Boolean,
        vararg entities: PairEntity
    ) {
        var textVaule = SpannableString(text)
        for (entity in entities) {
            var clickText = entity.clickText
            textVaule.setSpan(
                NoUnderLineClickableSpan(context, color, underlineText, entity),
                textVaule.indexOf(clickText),
                textVaule.indexOf(clickText) + clickText.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }
        //这里也可以通过setSpan来设置哪些位置的文本哪些颜色
        textView.text = textVaule
        //不设置 没有点击事件
        textView.movementMethod = LinkMovementMethod.getInstance();
        //设置点击后的颜色为透明
        textView.highlightColor = Color.TRANSPARENT;
    }

}