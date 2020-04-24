package com.zhf.auxiliaryjar.indicator

import android.content.Context
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.viewpager.widget.ViewPager
import com.zhf.auxiliaryjar.weiget.MagicIndicator
import com.zhf.auxiliaryjar.indicator.magicindicator.ViewPagerHelper
import com.zhf.auxiliaryjar.indicator.magicindicator.buildins.commonnavigator.CommonNavigator
import com.zhf.auxiliaryjar.indicator.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import com.zhf.auxiliaryjar.indicator.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import com.zhf.auxiliaryjar.indicator.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import com.zhf.auxiliaryjar.indicator.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import java.util.*

/**
 * 配合viewpage的指示器
 */
object ViewPageTabLayoutUtil {

    /**
     * 设置 Tab 均分布局
     * @param context : Context
     * @param viewpager : ViewPager
     * @param mDataList : 菜单List
     * @param normalColor : 未选中字体颜色
     * @param selectedColor : 选中字体颜色
     * @param textSize : 设置字体大小
     * @param lineHeight : 横线的高度  UIUtil.dip2px(context, 2.0).toFloat()
     * @param lineWidth : 横线的宽度  UIUtil.dip2px(context, 50.0).toFloat()
     * @param roundRadius : 横线的圆角  UIUtil.dip2px(context, 1.0).toFloat()
     * @param lineColor : 横线的颜色
     * @param backgroundColor : 设置切换 tab 的背景色
     * @param rollCount : 判断是否可以左右滑动,<=rollCount :不可滚动  >rollCount：可以左右滚动
     */
    fun initMagicIndicator(
        context: Context,
        magicIndicator: MagicIndicator,
        viewpager: ViewPager,
        mDataList: ArrayList<String>,
        normalColor: Int,
        selectedColor: Int,
        textSize: Float,
        lineHeight: Float,
        lineWidth: Float,
        roundRadius: Float,
        lineColor: Int,
        backgroundColor: Int?,
        rollCount: Int
    ) {
        magicIndicator.setBackgroundColor(backgroundColor!!);
        val commonNavigator = CommonNavigator(context)
        // 判断是否可以左右滑动
        commonNavigator.isAdjustMode = mDataList!!.size <= rollCount
        setCommonNavigator(
            magicIndicator,
            viewpager,
            mDataList,
            normalColor,
            selectedColor,
            textSize,
            lineHeight,
            lineWidth,
            roundRadius,
            lineColor,
            commonNavigator
        )
    }


    /**
     * 设置 Tab 均分布局
     * @param context : Context
     * @param viewpager : ViewPager
     * @param mDataList : 菜单List
     * @param normalColor : 未选中字体颜色
     * @param selectedColor : 选中字体颜色
     * @param textSize : 设置字体大小
     * @param lineHeight : 横线的高度  UIUtil.dip2px(context, 2.0).toFloat()
     * @param lineWidth : 横线的宽度  UIUtil.dip2px(context, 50.0).toFloat()
     * @param roundRadius : 横线的圆角  UIUtil.dip2px(context, 1.0).toFloat()
     * @param lineColor : 横线的颜色
     * @param backgroundColor : 设置切换 tab 的背景色
     * @param isAdjustMode : 是否可以横向滚动
     */
    fun initMagicIndicator(
        context: Context,
        magicIndicator: MagicIndicator,
        viewpager: ViewPager,
        mDataList: ArrayList<String>,
        normalColor: Int,
        selectedColor: Int,
        textSize: Float,
        lineHeight: Float,
        lineWidth: Float,
        roundRadius: Float,
        lineColor: Int,
        backgroundColor: Int?,
        isAdjustMode: Boolean
    ) {
        // 设置切换 tab 的背景色
        magicIndicator.setBackgroundColor(backgroundColor!!);
        val commonNavigator = CommonNavigator(context)
        commonNavigator.isAdjustMode = isAdjustMode
        setCommonNavigator(
            magicIndicator,
            viewpager,
            mDataList,
            normalColor,
            selectedColor,
            textSize,
            lineHeight,
            lineWidth,
            roundRadius,
            lineColor, commonNavigator
        )
    }

    private fun setCommonNavigator(
        magicIndicator: MagicIndicator,
        viewpager: ViewPager,
        mDataList: ArrayList<String>,
        normalColor: Int,
        selectedColor: Int,
        textSize: Float,
        lineHeight: Float,
        lineWidth: Float,
        roundRadius: Float,
        lineColor: Int,
        commonNavigator: CommonNavigator
    ) {
        commonNavigator.scrollPivotX = 0.65f
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mDataList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView = ColorFlipPagerTitleView(context)
                simplePagerTitleView.text = mDataList[index]
                simplePagerTitleView.textSize = textSize
                simplePagerTitleView.normalColor = normalColor
                simplePagerTitleView.selectedColor = selectedColor
                simplePagerTitleView.setOnClickListener { viewpager.currentItem = index }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                indicator.mode = LinePagerIndicator.MODE_EXACTLY
                indicator.lineHeight = lineHeight
                indicator.lineWidth = lineWidth

                // 设置线的圆角
                indicator.roundRadius = roundRadius
                indicator.startInterpolator = AccelerateInterpolator()
                indicator.endInterpolator = DecelerateInterpolator(2.0f)
                // 设置线的颜色
                indicator.setColors(lineColor)
                return indicator
            }
        }
        magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator, viewpager)
    }

//    /**
//     * 背景全铺
//     */
//    fun initCourseInfoMagicIndicator(
//        context: Context,
//        magicIndicator: MagicIndicator,
//        mViewPager: ViewPager,
//        mDataList: List<String>?
//    ) {
//
//        val commonNavigator = CommonNavigator(context)
//        // 自适应模式
//        commonNavigator.isAdjustMode = true
//        commonNavigator.adapter = object : CommonNavigatorAdapter() {
//            override fun getCount(): Int {
//                return mDataList?.size ?: 0
//            }
//
//            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
//                val clipPagerTitleView = ClipPagerTitleView(context)
//                clipPagerTitleView.text = mDataList!!.get(index)
//                clipPagerTitleView.textColor = Color.WHITE
//                clipPagerTitleView.clipColor = Color.BLACK
//                clipPagerTitleView.setOnClickListener { mViewPager.currentItem = index }
//                return clipPagerTitleView
//            }
//
//            @RequiresApi(Build.VERSION_CODES.M)
//            override fun getIndicator(context: Context): IPagerIndicator {
//                val indicator = LinePagerIndicator(context)
//                // 与 magicIndicator 高度一致
//                val navigatorHeight = context.resources.getDimension(R.dimen.margin_43)
//                var borderWidth = UIUtil.dip2px(context, 0.0).toFloat()
//                val lineHeight = navigatorHeight - 2 * borderWidth
//                indicator.lineHeight = lineHeight
//                indicator.roundRadius = 0f
//                indicator.setColors(context.resources.getColor(R.color.color_FFCC00, null))
//                return indicator
//            }
//        }
//        magicIndicator.navigator = commonNavigator
//        ViewPagerHelper.bind(magicIndicator, mViewPager)
//    }
//
//
//    /**
//     * 带颜色渐变和缩放的指示器标题
//     * @param context
//     * @param magicIndicator
//     * @param mainPager
//     * @param mDataList
//     * @param type
//     */
//    fun mainInitMagicIndicator(
//        context: Context,
//        magicIndicator: MagicIndicator,
//        mainPager: ViewPager,
//        mDataList: List<String>?,
//        type: Int
//    ) {
//        magicIndicator.setBackgroundColor(context.resources.getColor(R.color.transparent, null))
//        val commonNavigator = CommonNavigator(context)
//        commonNavigator.isAdjustMode = true
//        commonNavigator.adapter = object : CommonNavigatorAdapter() {
//            override fun getCount(): Int {
//                return mDataList?.size ?: 0
//            }
//
//            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
//                val simplePagerTitleView = ScaleTransitionPagerTitleView(context)
//                simplePagerTitleView.text = mDataList!![index]
//                simplePagerTitleView.textSize = 17f
//                // 未选中字体颜色
//                simplePagerTitleView.normalColor =
//                    context.resources.getColor(R.color.color_404040, null)
//                // 选中字体颜色
//                simplePagerTitleView.selectedColor = context.resources.getColor(R.color.black, null)
//                simplePagerTitleView.setOnClickListener { mainPager.currentItem = index }
//                return simplePagerTitleView
//            }
//
//            override fun getIndicator(context: Context): IPagerIndicator {
//                val indicator = LinePagerIndicator(context)
//                indicator.visibility = View.GONE
//                return indicator
//            }
//        }
//        magicIndicator.navigator = commonNavigator
//        ViewPagerHelper.bind(magicIndicator, mainPager)
//    }
//
//    fun initWhiteMagicIndicator(
//        context: Context,
//        magicIndicator: MagicIndicator,
//        mainPager: ViewPager,
//        mDataList: List<String>?,
//        drawableId: Int,
//        textColorId: Int,
//        lineColorId: Int
//    ) {
//        //        magicIndicator.setBackgroundColor(context.getResources().getColor(R.color.transparent));
//        magicIndicator.background = context.resources.getDrawable(drawableId, null)
//
//        val commonNavigator = CommonNavigator(context)
//        commonNavigator.isAdjustMode = true
//        commonNavigator.scrollPivotX = 0.65f
//        commonNavigator.adapter = object : CommonNavigatorAdapter() {
//            override fun getCount(): Int {
//                return mDataList?.size ?: 0
//            }
//
//            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
//                val simplePagerTitleView = ColorFlipPagerTitleView(context)
//                simplePagerTitleView.text = mDataList!![index]
//                simplePagerTitleView.textSize = 13f
//                // 未选中字体颜色
//                simplePagerTitleView.normalColor = Color.parseColor("#FFD4C0")
//                // 选中字体颜色
//                simplePagerTitleView.selectedColor = context.resources.getColor(lineColorId)
//                simplePagerTitleView.setOnClickListener { mainPager.currentItem = index }
//                return simplePagerTitleView
//            }
//
//            override fun getIndicator(context: Context): IPagerIndicator {
//                val indicator = LinePagerIndicator(context)
//                indicator.mode = LinePagerIndicator.MODE_EXACTLY
//                indicator.lineHeight = UIUtil.dip2px(context, 2.0).toFloat()
//                indicator.lineWidth = UIUtil.dip2px(context, 30.0).toFloat()
//                indicator.roundRadius = UIUtil.dip2px(context, 1.0).toFloat()
//                indicator.startInterpolator = AccelerateInterpolator()
//                indicator.endInterpolator = DecelerateInterpolator(2.0f)
//                indicator.setColors(context.resources.getColor(R.color.white))
//                return indicator
//            }
//        }
//        magicIndicator.navigator = commonNavigator
//        ViewPagerHelper.bind(magicIndicator, mainPager)
//    }


}
