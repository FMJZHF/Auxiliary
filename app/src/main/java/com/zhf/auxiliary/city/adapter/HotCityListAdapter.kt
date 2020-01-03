package com.zhf.auxiliary.city.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.zhf.auxiliary.R
import com.zhf.auxiliary.city.model.CityEntity

/**
 * 热门城市适配器
 */
class HotCityListAdapter(var mContext: Context,var cityEntities: List<CityEntity>): BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var convertView = convertView

        val holder: ViewHolder
        if (null == convertView) {
            holder = ViewHolder()
            convertView =  LayoutInflater.from(mContext).inflate(R.layout.city_list_grid_item_layout, null)
            holder.cityNameTv = convertView.findViewById(R.id.city_list_grid_item_name_tv)
            convertView!!.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        val cityEntity = cityEntities[position]
        holder.cityNameTv.setText(cityEntity.getName())
        return convertView
    }

    override fun getItem(position: Int): Any {
        return cityEntities[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return cityEntities.size
    }

    class ViewHolder {

        lateinit var cityNameTv: TextView

    }
}