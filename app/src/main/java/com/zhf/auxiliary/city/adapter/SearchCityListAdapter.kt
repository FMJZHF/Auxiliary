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
 * 搜索城市列表适配器
 */
class SearchCityListAdapter (var mContext: Context, var cityEntities: List<CityEntity>): BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        val holder: ViewHolder
        if (null == convertView) {
            holder = ViewHolder()
            convertView =  LayoutInflater.from(mContext).inflate(R.layout.city_list_item_layout, null)
            holder.cityNameTv = convertView.findViewById(R.id.city_name_tv)
            holder.cityKeyTv = convertView.findViewById(R.id.city_key_tv)
            convertView!!.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        val cityEntity = cityEntities!![position]
        holder.cityKeyTv!!.visibility = View.GONE
        holder.cityNameTv!!.setText(cityEntity.getName())

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
        lateinit var cityKeyTv: TextView
    }
}