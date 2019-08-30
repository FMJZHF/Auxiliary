package com.zhf.auxiliary.suctiontop.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.zhf.auxiliary.R

class RecyclerviewAdapter(private val context: Context, private val data: List<String>) : RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder>() {
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(context).inflate(R.layout.item_suction_top, parent, false)
		return ViewHolder(view)
	}
	
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.name.text = data[position]
		
		holder.itemView.setOnClickListener { Log.e("这里是点击每一行item的响应事件", "" + position) }
		
	}
	
	override fun getItemCount(): Int {
		return data.size
	}
	
	inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		
		internal val name: TextView = itemView.findViewById(R.id.name)
		
	}
}
