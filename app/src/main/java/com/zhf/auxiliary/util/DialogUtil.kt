package com.zhf.auxiliary.util

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.zhf.auxiliary.R
import com.zhf.auxiliaryjar.picker.WheelPickerScrollListener
import com.zhf.auxiliaryjar.picker.WheelPickerView

/**
 * 弹框显示 Android选择器
 * Created by zhf on 2019/8/28 14:11
 */
object DialogUtil {
	
	private var mYearList = arrayListOf<String>("2016", "2017", "2018", "2019", "2020", "2021", "2022")
	private lateinit var mMonthList: ArrayList<String>
	private lateinit var mDayList: ArrayList<String>
	
	fun showDialog(context: Context) {
		var yearPos = 3
		var monthPos = 6
		var dayPos = 16
		
		var title = ""
		val bottomDialog = Dialog(context, R.style.BottomDialog)
		val contentView = LayoutInflater.from(context).inflate(R.layout.dialog_content_normal, null)
		
		val titleView = contentView.findViewById<TextView>(R.id.title)
		
		
		val confrimView = contentView.findViewById<View>(R.id.button_confirm)
		val cancelView = contentView.findViewById<View>(R.id.button_cancel)
		val wheelPickerYear = contentView.findViewById<WheelPickerView>(R.id.wheelPickerYear)
		val wheelPickerMonth = contentView.findViewById<WheelPickerView>(R.id.wheelPickerMonth)
		val wheelPickerDay = contentView.findViewById<WheelPickerView>(R.id.wheelPickerDay)
		
		mDayList = getDayList()
		mMonthList = getMonthList()
		// 设置标题文字
		titleView.text = mYearList[yearPos] + "-" + mMonthList[monthPos] + "-" + mDayList[dayPos]
		wheelPickerYear.setDataList(mYearList)
		wheelPickerYear.setInitPosition(yearPos) // 默认选中的年份
		//年份滚动选择
		wheelPickerYear.setLoopListener(object : WheelPickerScrollListener {
			override fun onItemSelect(item: Int) {
				yearPos = item
				Log.e("选择的年：", mYearList[item])
				titleView.text = mYearList[yearPos] + "-" + mMonthList[monthPos] + "-" + mDayList[dayPos]
			}
		})
		
		wheelPickerMonth.setDataList(mMonthList)
		wheelPickerMonth.setInitPosition(monthPos) // 默认选中的月份
		//月份滚动选择
		wheelPickerMonth.setLoopListener(object : WheelPickerScrollListener {
			override fun onItemSelect(item: Int) {
				monthPos = item
				Log.e("选择的月： ", mMonthList[item])
				titleView.text = mYearList[yearPos] + "-" + mMonthList[monthPos] + "-" + mDayList[dayPos]
			}
		})
		
		wheelPickerDay.setDataList(mDayList)
		wheelPickerDay.setInitPosition(dayPos) // 默认选中的天
		//天数滚动选择
		wheelPickerDay.setLoopListener(object : WheelPickerScrollListener {
			override fun onItemSelect(item: Int) {
				dayPos = item
				Log.e("选择的天： ", mDayList[item])
				titleView.text = mYearList[yearPos] + "-" + mMonthList[monthPos] + "-" + mDayList[dayPos]
			}
		})
		
		confrimView.setOnClickListener {
			Toast.makeText(context,"选择的时间为："+titleView.text,Toast.LENGTH_SHORT).show()
			bottomDialog.dismiss()
		}
		cancelView.setOnClickListener { bottomDialog.dismiss() }
		bottomDialog.setContentView(contentView)
		val layoutParams = contentView.layoutParams
		layoutParams.width = context.resources.displayMetrics.widthPixels
		contentView.layoutParams = layoutParams
		bottomDialog.window!!.setGravity(Gravity.BOTTOM)
		bottomDialog.window!!.setWindowAnimations(R.style.BottomDialog_Animation)
		bottomDialog.setCanceledOnTouchOutside(true)
		bottomDialog.show()
	}
	
	// 获取天数，此处为模拟数据，请根据自己实际情况进行天数的判断
	private fun getDayList(): ArrayList<String> {
		var mDayList = arrayListOf<String>()
		for (i in 1..31) {
			if (i < 10) {
				mDayList.add("0$i")
			} else {
				mDayList.add(i.toString())
			}
		}
		return mDayList
	}
	
	// 获取月份
	private fun getMonthList(): ArrayList<String> {
		var mMonthList = arrayListOf<String>()
		for (i in 1..12) {
			if (i < 10) {
				mMonthList.add("0$i")
			} else {
				mMonthList.add(i.toString())
			}
		}
		return mMonthList
	}
	
}
