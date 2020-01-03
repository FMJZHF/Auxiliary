package com.zhf.auxiliaryjar.recyclerview_adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
* RecyclerView adapter父类 继承 RecyclerView.Adapter
* Created by zhf on 2019/8/19 14:34
*/
abstract class RecyclerRootAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected var mDatas: MutableList<T>? = null
    protected lateinit var mOnItemClickListener: OnItemClickListener<*>
    protected lateinit var mOnLongItemClickListener: OnLongItemClickListener<*>

    val isEmpty: Boolean
        get() = mDatas == null || mDatas!!.size == 0

    /**
     * 设置列表中的数据
     */
    var datas: MutableList<T>?
        get() = mDatas
        set(datas) {
            if (datas == null) {
                return
            }
            this.mDatas = datas
            notifyDataSetChanged()
        }

    /**
     * 将单个数据添加到列表中
     */
    fun addSingleData(t: T?) {
        if (t == null) {
            return
        }
        this.mDatas!!.add(t)
        notifyItemInserted(mDatas!!.size - 1)
    }

    fun addDatas(datas: List<T>?, position: Int) {
        if (datas == null || datas.size == 0)
            return
        mDatas!!.addAll(position, datas)
        notifyItemRangeInserted(position, datas.size)
    }

    fun removeDatas(datas: List<T>?, position: Int) {
        if (datas == null || datas.size == 0)
            return
        mDatas!!.removeAll(datas)
        notifyItemRangeRemoved(position, datas.size)
    }

    fun addSingleDate(t: T, position: Int) {
        mDatas!!.add(position, t)
        notifyItemInserted(position)
        // notifyItemRangeChanged(position, mDatas.size());
    }

    fun removeData(position: Int) {
        mDatas!!.removeAt(position)
        notifyItemRemoved(position)
        // notifyItemRangeChanged(position, mDatas.size());
    }

    fun removeData(t: T) {
        val index = mDatas!!.indexOf(t)
        notifyItemRemoved(index)
        // notifyItemRangeChanged(index, mDatas.size());
    }

    /**
     * 将一个List添加到列表中
     */
    fun addDatas(datas: List<T>?) {
        if (datas == null || datas.size == 0) {
            return
        }
        //        int oldSize = this.mDatas.size();
        //        int newSize = datas.size();
        //        this.mDatas.addAll(datas);
        //        notifyItemRangeInserted(oldSize, newSize);
        this.mDatas!!.addAll(datas)
        if (true) {
            notifyDataSetChanged()
        }
    }


    fun clearDatas() {
        if (!isEmpty) {
            this.mDatas!!.clear()
        }

    }

    interface OnItemClickListener<T> {
        fun onClick(view: View, holder: RecyclerView.ViewHolder, o: T, position: Int)

    }

    interface OnLongItemClickListener<T> {
        fun onItemLongClick(view: View, holder: RecyclerView.ViewHolder, o: T, position: Int): Boolean
    }

    /**
     * 设置点击事件
     *
     * @param onItemClickListener
     */
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<*>) {
        this.mOnItemClickListener = onItemClickListener
    }

    /**
     * 设置长按点击事件
     *
     * @param onLongItemClickListener
     */
    fun setonLongItemClickListener(onLongItemClickListener: OnLongItemClickListener<*>) {
        this.mOnLongItemClickListener = onLongItemClickListener
    }
}
