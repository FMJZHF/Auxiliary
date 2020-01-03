package com.zhf.auxiliary.suctiontop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.zhf.auxiliary.R
import com.zhf.auxiliary.suctiontop.adapter.BaseFragmentPagerAdapter
import com.zhf.auxiliary.suctiontop.fragment.SuctionTopFragment
import com.zhf.auxiliary.suctiontop.indicator.TabLayouUtil
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
		
		TabLayouUtil.initMagicIndicator(this, magicIndicator, viewPager, mTitles!!.toList() as ArrayList<String>?)
	}
	
}
