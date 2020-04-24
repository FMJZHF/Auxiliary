package com.zhf.auxiliaryjar.indicator

import android.content.Context
import com.zhf.auxiliaryjar.indicator.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView

/**
 * 配合viewpage的指示器帮助类
 */
class ColorFlipPagerTitleView(context: Context) : SimplePagerTitleView(context) {
	var changePercent = 0.5f

	override fun onLeave(index: Int, totalCount: Int, leavePercent: Float, leftToRight: Boolean) {
		if (leavePercent >= changePercent) {
			setTextColor(mNormalColor)
		} else {
			setTextColor(mSelectedColor)
		}
	}

	override fun onEnter(index: Int, totalCount: Int, enterPercent: Float, leftToRight: Boolean) {
		if (enterPercent >= changePercent) {
			setTextColor(mSelectedColor)
		} else {
			setTextColor(mNormalColor)
		}
	}

	override fun onSelected(index: Int, totalCount: Int) {}

	override fun onDeselected(index: Int, totalCount: Int) {}
}
