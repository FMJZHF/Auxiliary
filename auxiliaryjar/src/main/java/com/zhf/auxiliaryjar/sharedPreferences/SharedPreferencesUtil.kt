package com.zhf.auxiliaryjar.sharedPreferences

import android.content.Context
import android.content.SharedPreferences

/**
 * 使用 SharedPreferences 保存数据
 */
object SharedPreferencesUtil {

    private var sharedPreferences: SharedPreferences? = null

    private fun init(context: Context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("ConfigData", Context.MODE_PRIVATE)
        }
    }

    /**
     * 保存 Boolean 数据
     * @param key
     * @param value
     */
    fun putBoolean(context: Context, key: String, value: Boolean?) {
        init(context)
        val editor = sharedPreferences!!.edit()
        editor.putBoolean(key, value!!)
        editor.apply()//生效
    }

    /**
     * 获取 Boolean 数据
     * @param key
     * @return
     */
    fun getBoolean(context: Context, key: String): Boolean {
        init(context)
        return sharedPreferences!!.getBoolean(key, false)
    }

    /**
     * 获取 String 数据
     * @param key
     * @return
     */
    fun getString(context: Context, key: String): String? {
        init(context)
        return sharedPreferences!!.getString(key, "")
    }

    /**
     * 保存 String 数据
     * @param key
     * @param value
     */
    fun putString(context: Context, key: String, value: String) {
        init(context)
        val editor = sharedPreferences!!.edit()
        editor.putString(key, value)
        editor.apply()//生效
    }

    /**
     * 获取 long 数据
     * @param key
     * @return
     */
    fun getLong(context: Context, key: String): Long {
        init(context)
        return sharedPreferences!!.getLong(key, 0L)
    }

    /**
     * 保存 long 数据
     * @param key
     * @param value
     */
    fun putLong(context: Context, key: String, value: Long?) {
        init(context)
        val editor = sharedPreferences!!.edit()
        editor.putLong(key, value!!)
        editor.apply()//生效
    }

    /**
     * 获取 int 数据
     * @param key
     * @return
     */
    fun getInt(context: Context, key: String): Int {
        init(context)
        return sharedPreferences!!.getInt(key, 0)
    }

    /**
     * 保存 int 数据
     * @param key
     * @param value
     */
    fun putInt(context: Context, key: String, value: Int) {
        init(context)
        val editor = sharedPreferences!!.edit()
        editor.putInt(key, value)
        editor.apply()//生效
    }

    /**
     * 清空数据
     */
    fun clearSpConfigData(context: Context) {
        init(context)
        val editor = sharedPreferences!!.edit()
        editor.clear()
        editor.commit()
    }

    /**
     * 清除单个数据
     */
    fun clearSpConfigData(context: Context, key: String) {
        init(context)
        val editor = sharedPreferences!!.edit()
        editor.remove(key);
        editor.commit();
    }

}