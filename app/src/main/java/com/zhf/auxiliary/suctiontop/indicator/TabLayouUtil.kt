package com.zhf.auxiliary.suctiontop.indicator

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.annotation.RequiresApi
import androidx.viewpager.widget.ViewPager

import com.zhf.auxiliary.R

import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator

import java.util.ArrayList

/**
 * viewpage 上部的指示器
 */
object TabLayouUtil {
	
	fun initMagicIndicator(
		context: Context,
		magicIndicator: MagicIndicator,
		mainPager: ViewPager,
		mDataList: ArrayList<String>?
	) {
		// 设置切换 tab 的背景色
		//magicIndicator.setBackgroundColor(context.getResources().getColor(R.color.white));
		val commonNavigator = CommonNavigator(context)
		commonNavigator.isAdjustMode = true
		commonNavigator.scrollPivotX = 0.65f
		commonNavigator.adapter = object : CommonNavigatorAdapter() {
			override fun getCount(): Int {
				return mDataList?.size ?: 0
			}
			
			override fun getTitleView(context: Context, index: Int): IPagerTitleView {
				val simplePagerTitleView = ColorFlipPagerTitleView(context)
				simplePagerTitleView.text = mDataList!![index]
				// 设置字体大小
				simplePagerTitleView.textSize = 16f
				// 未选中字体颜色
				simplePagerTitleView.normalColor = context.resources.getColor(R.color.white)
				// 选中字体颜色
				simplePagerTitleView.selectedColor = context.resources.getColor(R.color.color_FCFF26)
				simplePagerTitleView.setOnClickListener { mainPager.currentItem = index }
				return simplePagerTitleView
			}
			
			override fun getIndicator(context: Context): IPagerIndicator {
				val indicator = LinePagerIndicator(context)
				indicator.mode = LinePagerIndicator.MODE_EXACTLY
				// 设置线高
				indicator.lineHeight = UIUtil.dip2px(context, 2.0).toFloat()
				// 设置线宽
				indicator.lineWidth = UIUtil.dip2px(context, 50.0).toFloat()
				
				// 设置线的圆角
				indicator.roundRadius = UIUtil.dip2px(context, 1.0).toFloat()
				indicator.startInterpolator = AccelerateInterpolator()
				indicator.endInterpolator = DecelerateInterpolator(2.0f)
				// 设置线的颜色
				indicator.setColors(context.resources.getColor(R.color.color_FCFF26))
				return indicator
			}
		}
		magicIndicator.navigator = commonNavigator
		ViewPagerHelper.bind(magicIndicator, mainPager)
	}
	
	fun initListMagicIndicator(
			context: Context,
			magicIndicator: MagicIndicator,
			mainPager: ViewPager,
			mDataList: List<String>?
	) {
		magicIndicator.setBackgroundColor(context.resources.getColor(R.color.white))
		val commonNavigator = CommonNavigator(context)
		commonNavigator.isAdjustMode = true
		commonNavigator.scrollPivotX = 0.65f
		commonNavigator.adapter = object : CommonNavigatorAdapter() {
			override fun getCount(): Int {
				return mDataList?.size ?: 0
			}
			
			override fun getTitleView(context: Context, index: Int): IPagerTitleView {
				val simplePagerTitleView = ColorFlipPagerTitleView(context)
				simplePagerTitleView.text = mDataList!![index]
				simplePagerTitleView.textSize = 17f
				// 未选中字体颜色
				simplePagerTitleView.normalColor = context.resources.getColor(R.color.color_404040)
				// 选中字体颜色
				simplePagerTitleView.selectedColor = context.resources.getColor(R.color.color_FC6524)
				simplePagerTitleView.setOnClickListener { mainPager.currentItem = index }
				return simplePagerTitleView
			}
			
			override fun getIndicator(context: Context): IPagerIndicator {
				val indicator = LinePagerIndicator(context)
				indicator.mode = LinePagerIndicator.MODE_EXACTLY
				indicator.lineHeight = UIUtil.dip2px(context, 2.0).toFloat()
				indicator.lineWidth = UIUtil.dip2px(context, 24.0).toFloat()
				indicator.roundRadius = UIUtil.dip2px(context, 1.0).toFloat()
				indicator.startInterpolator = AccelerateInterpolator()
				indicator.endInterpolator = DecelerateInterpolator(2.0f)
				indicator.setColors(context.resources.getColor(R.color.color_FC6524))
				return indicator
			}
		}
		magicIndicator.navigator = commonNavigator
		ViewPagerHelper.bind(magicIndicator, mainPager)
	}
	
	/**
	 * 带颜色渐变和缩放的指示器标题
	 * @param context
	 * @param magicIndicator
	 * @param mainPager
	 * @param mDataList
	 * @param type
	 */
	fun mainInitMagicIndicator(
			context: Context,
			magicIndicator: MagicIndicator,
			mainPager: ViewPager,
			mDataList: List<String>?,
			type: Int
	) {
		magicIndicator.setBackgroundColor(context.resources.getColor(R.color.transparent))
		val commonNavigator = CommonNavigator(context)
		commonNavigator.isAdjustMode = true
		commonNavigator.adapter = object : CommonNavigatorAdapter() {
			override fun getCount(): Int {
				return mDataList?.size ?: 0
			}
			
			override fun getTitleView(context: Context, index: Int): IPagerTitleView {
				val simplePagerTitleView = ScaleTransitionPagerTitleView(context)
				simplePagerTitleView.text = mDataList!![index]
				simplePagerTitleView.textSize = 17f
				// 未选中字体颜色
				simplePagerTitleView.normalColor = context.resources.getColor(R.color.color_404040)
				// 选中字体颜色
				simplePagerTitleView.selectedColor = context.resources.getColor(R.color.color_404040)
				simplePagerTitleView.setOnClickListener { mainPager.currentItem = index }
				return simplePagerTitleView
			}
			
			override fun getIndicator(context: Context): IPagerIndicator {
				val indicator = LinePagerIndicator(context)
				indicator.visibility = View.GONE
				return indicator
			}
		}
		magicIndicator.navigator = commonNavigator
		ViewPagerHelper.bind(magicIndicator, mainPager)
	}
	
	@RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
	fun initWhiteMagicIndicator(
			context: Context,
			magicIndicator: MagicIndicator,
			mainPager: ViewPager,
			mDataList: List<String>?,
			drawableId: Int,
			textColorId: Int,
			lineColorId: Int
	) {
		//        magicIndicator.setBackgroundColor(context.getResources().getColor(R.color.transparent));
		magicIndicator.background = context.resources.getDrawable(drawableId)
		
		val commonNavigator = CommonNavigator(context)
		commonNavigator.isAdjustMode = true
		commonNavigator.scrollPivotX = 0.65f
		commonNavigator.adapter = object : CommonNavigatorAdapter() {
			override fun getCount(): Int {
				return mDataList?.size ?: 0
			}
			
			override fun getTitleView(context: Context, index: Int): IPagerTitleView {
				val simplePagerTitleView = ColorFlipPagerTitleView(context)
				simplePagerTitleView.text = mDataList!![index]
				simplePagerTitleView.textSize = 13f
				// 未选中字体颜色
				simplePagerTitleView.normalColor = Color.parseColor("#FFD4C0")
				// 选中字体颜色
				simplePagerTitleView.selectedColor = context.resources.getColor(lineColorId)
				simplePagerTitleView.setOnClickListener { mainPager.currentItem = index }
				return simplePagerTitleView
			}
			
			override fun getIndicator(context: Context): IPagerIndicator {
				val indicator = LinePagerIndicator(context)
				indicator.mode = LinePagerIndicator.MODE_EXACTLY
				indicator.lineHeight = UIUtil.dip2px(context, 2.0).toFloat()
				indicator.lineWidth = UIUtil.dip2px(context, 30.0).toFloat()
				indicator.roundRadius = UIUtil.dip2px(context, 1.0).toFloat()
				indicator.startInterpolator = AccelerateInterpolator()
				indicator.endInterpolator = DecelerateInterpolator(2.0f)
				indicator.setColors(context.resources.getColor(R.color.white))
				return indicator
			}
		}
		magicIndicator.navigator = commonNavigator
		ViewPagerHelper.bind(magicIndicator, mainPager)
	}
	
	fun initMagicIndicatorRechargeRule(
			context: Context,
			magicIndicator: MagicIndicator,
			mainPager: ViewPager,
			mDataList: List<String>?
	) {
		magicIndicator.setBackgroundColor(context.resources.getColor(R.color.white))
		val commonNavigator = CommonNavigator(context)
		commonNavigator.isAdjustMode = true
		commonNavigator.scrollPivotX = 0.65f
		commonNavigator.adapter = object : CommonNavigatorAdapter() {
			override fun getCount(): Int {
				return mDataList?.size ?: 0
			}
			
			override fun getTitleView(context: Context, index: Int): IPagerTitleView {
				val simplePagerTitleView = ColorFlipPagerTitleView(context)
				simplePagerTitleView.text = mDataList!![index]
				simplePagerTitleView.textSize = 13f
				// 未选中字体颜色
				simplePagerTitleView.normalColor = context.resources.getColor(R.color.color_404040)
				// 选中字体颜色
				simplePagerTitleView.selectedColor = context.resources.getColor(R.color.color_FC6524)
				simplePagerTitleView.setOnClickListener { mainPager.currentItem = index }
				return simplePagerTitleView
			}
			
			override fun getIndicator(context: Context): IPagerIndicator {
				val indicator = LinePagerIndicator(context)
				indicator.mode = LinePagerIndicator.MODE_EXACTLY
				indicator.lineHeight = UIUtil.dip2px(context, 2.0).toFloat()
				indicator.lineWidth = UIUtil.dip2px(context, 24.0).toFloat()
				indicator.lineWidth = UIUtil.dip2px(context, 24.0).toFloat()
				indicator.roundRadius = UIUtil.dip2px(context, 1.0).toFloat()
				indicator.startInterpolator = AccelerateInterpolator()
				indicator.endInterpolator = DecelerateInterpolator(1.0f)
				indicator.setColors(context.resources.getColor(R.color.color_FC6524))
				return indicator
			}
		}
		magicIndicator.navigator = commonNavigator
		ViewPagerHelper.bind(magicIndicator, mainPager)
	}
	
}
