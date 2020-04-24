package com.zhf.auxiliary.suctiontop

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.zhf.auxiliary.R
import com.zhf.auxiliary.suctiontop.adapter.BaseFragmentPagerAdapter
import com.zhf.auxiliary.suctiontop.fragment.SuctionTopFragment
import com.zhf.auxiliaryjar.indicator.ViewPageTabLayoutUtil
import com.zhf.auxiliaryjar.indicator.magicindicator.buildins.UIUtil
import kotlinx.android.synthetic.main.activity_suction_top.*
import java.util.*

/**
 * 吸顶效果
 */
class SuctionTopActivity : AppCompatActivity() {

    private var mTitles: Array<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suction_top)

        mTitles = arrayOf("新闻", "财经")
        var mFragments = ArrayList<Fragment>()


        val sucationTopFragment1 = SuctionTopFragment()
        mFragments.add(sucationTopFragment1)
        val sucationTopFragment2 = SuctionTopFragment()
        mFragments.add(sucationTopFragment2)


//        val adapter = BaseFragmentAdapter(supportFragmentManager, mFragments, mTitles)
        val adapter = BaseFragmentPagerAdapter(supportFragmentManager, mFragments, mTitles)
        viewPager.adapter = adapter


        // 未选中字体颜色
        var normalColor = Color.parseColor("#ffffff")
        // 选中字体颜色
        var selectedColor = Color.parseColor("#FCFF26")
        //  横线的颜色
        var lineColor = Color.parseColor("#FCFF26")
        // 设置切换 tab 的背景色
        var backgroundColor = Color.parseColor("#000000")
        // 设置字体大小
        var textSize = 12f

        ViewPageTabLayoutUtil.initMagicIndicator(
            this,
            magicIndicator,
            viewPager,
            mTitles!!.toList() as ArrayList<String>,
            normalColor,
            selectedColor,
            textSize,
            UIUtil.dip2px(this, 2.0).toFloat(),
            UIUtil.dip2px(this, 50.0).toFloat(),
            UIUtil.dip2px(this, 1.0).toFloat(),
            lineColor,
            backgroundColor,
            true
        )

    }

}
