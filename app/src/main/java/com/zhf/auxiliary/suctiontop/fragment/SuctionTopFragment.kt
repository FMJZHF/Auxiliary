package com.zhf.auxiliary.suctiontop.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhf.auxiliary.R
import com.zhf.auxiliary.suctiontop.adapter.RecyclerviewAdapter

class SuctionTopFragment : Fragment() {
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val topView = inflater.inflate(R.layout.fragment_suction_top, container, false)
		var recyclerView: RecyclerView = topView.findViewById(R.id.listView)
		
		var dataList = arrayListOf<String>("列表内容1", "列表内容2", "列表内容3", "列表内容4", "列表内容5",
				"列表内容6", "列表内容7", "列表内容8", "列表内容9", "列表内容10")
		
		
		var adapter = RecyclerviewAdapter(context!!, dataList)
		recyclerView.layoutManager = LinearLayoutManager(context!!);
		
		//使用适配器
		recyclerView.adapter = adapter
		
		return topView
	}
	
	
}
