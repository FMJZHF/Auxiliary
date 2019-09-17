package com.zhf.auxiliaryjar.recyclerview_adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList

/**
 * RecyclerView 的 Adapter
 * Created by zhf on 2019/8/19 14:32
 *
 * use:
 * class NewsAdapter(context: Context, datas: MutableList<TestBean>) :RecyclerBaseAdapter<TestBean>(context, R.layout.item_layout, datas) {
 *         override fun convert(holder: RecyclerBaseHolder, item: TestBean, position: Int) {
 *                val tv = holder.getView<TextView>(R.id.tv)
 *                     tv.text = item.name
 *                val image = holder.getView<ImageView>(R.id.image)
 *                     image.setOnClickListener{
 *                          // 点击事件
 *                      }
 *          }
 * }
 *
*/
abstract class RecyclerBaseAdapter<T> : RecyclerRootAdapter<T> {

    private var mContext: Context
    private val mItemLayoutId: Int

    constructor(context: Context, itemLayoutId: Int) {
        mContext = context
        mItemLayoutId = itemLayoutId
        mDatas = ArrayList()
    }

    constructor(context: Context, itemLayoutId: Int, datas: MutableList<T>) {
        mContext = context
        mItemLayoutId = itemLayoutId
        mDatas = datas
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holder = RecyclerBaseHolder(LayoutInflater.from(mContext).inflate(mItemLayoutId, parent, false))
//        setListener(parent, holder, viewType)
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val baseHolder = holder as RecyclerBaseHolder

        convert(baseHolder, mDatas!![position] as T, position)
    }

    /**
     * @param holder   自定义的ViewHolder对象，可以getView获取控件
     * @param item     对应的数据
     * @param position
     */
    abstract fun convert(holder: RecyclerBaseHolder, item: T, position: Int)

    override fun getItemCount(): Int {
        return if (isEmpty) 0 else mDatas!!.size
    }

//    protected fun isEnabled(viewType: Int): Boolean {
//        return true
//    }

    fun setClickListener(holder: RecyclerBaseHolder, id: Int, onClickListener: View.OnClickListener) {
        holder.getView<View>(id).setOnClickListener(onClickListener)
    }

//    private fun setListener(parent: ViewGroup, viewHolder: RecyclerBaseHolder, viewType: Int) {
//        if (!isEnabled(viewType)) return
//        viewHolder.convertView.setOnClickListener { v ->
//            if (mOnItemClickListener != null) {
//                //这个方法是获取在holder里面真正的位置，而不是对应list的位置
//                val position = viewHolder.adapterPosition - 1
//                val t = mDatas!![position]
////                mOnItemClickListener.onClick(v, viewHolder, t, position)
//            }
//        }
//
//        viewHolder.convertView.setOnLongClickListener(View.OnLongClickListener { v ->
//            if (mOnLongItemClickListener != null) {
//                val position = viewHolder.adapterPosition
////                return@OnLongClickListener mOnLongItemClickListener.onItemLongClick(
////                    v,
////                    viewHolder,
////                    mDatas!![position],
////                    position
////                )
//            }
//            false
//        })
//
//    }


}

