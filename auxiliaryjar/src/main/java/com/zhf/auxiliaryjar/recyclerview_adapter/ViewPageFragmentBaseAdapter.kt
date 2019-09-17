package com.zhf.auxiliaryjar.recyclerview_adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter

import java.util.ArrayList

/**
 * viewpage 中添加fragment的 adapter
 * Created by zhf on 2019/8/19 14:32
 *
 * use :
 *  var mTitles = arrayOf("课程大纲", "相关作品")
 *  var mFragments = ArrayList<Fragment>()
 *  val adapter = ViewPageFragmentBaseAdapter(supportFragmentManager, mFragments, mTitles)
 *  viewPager.adapter = adapter
 *
 */
open class ViewPageFragmentBaseAdapter @JvmOverloads constructor(
    fm: FragmentManager,
    fragmentList: MutableList<Fragment>? = null,
    private var mTitles: Array<String>? = null
//) : FragmentPagerAdapter(fm) {
) : FragmentStatePagerAdapter(fm) {

    private var mFragmentList: MutableList<Fragment>? = null

    val isEmpty: Boolean
        get() = mFragmentList == null

    init {
        var fragmentList = fragmentList
        if (fragmentList == null) {
            fragmentList = ArrayList()
        }
        this.mFragmentList = fragmentList
    }

    fun add(fragment: Fragment) {
        if (isEmpty) {
            mFragmentList = ArrayList()

        }
        mFragmentList!!.add(fragment)
    }

    override fun getItem(position: Int): Fragment? {
        //        Logger.i("BaseFragmentAdapter position=" +position);
        return if (isEmpty) null else mFragmentList!![position]
    }

    override fun getCount(): Int {
        return if (isEmpty) 0 else mFragmentList!!.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitles!![position]
    }


}
