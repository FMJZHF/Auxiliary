package com.zhf.auxiliaryjar.date

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Desc: 时间工具类
 *
 */
object DateTime {
    private val FORMAT_H_M_S = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
    private val FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

    /**
     * 获取当前时间
     * 格式： yyyy-MM-dd HH:mm:ss
     * @return
     */
    val nowDateHourMinuteSecond: String
        get() = FORMAT_H_M_S.format(Date(System.currentTimeMillis()))
    /**
     * 获取当前时间
     * 格式： yyyy-MM-dd
     * @return
     */
    val nowDate: String
        get() = FORMAT.format(Date(System.currentTimeMillis()))

    /**
     * 将时间转为字符串
     * 格式： yyyy-MM-dd HH:mm:ss
     * @param date Date
     * @return 时间字符串
     */
    fun getSampleDateHourMinuteSecond(date: Date): String {
        return FORMAT_H_M_S.format(date)
    }

    /**
     * 将时间转为字符串
     * 格式： yyyy-MM-dd
     * @param date Date
     * @return 时间字符串
     */
    fun getSampleDate(date: Date): String {
        return FORMAT.format(date)
    }

    /**
     * 将时间戳转为字符串
     * 格式： yyyy-MM-dd HH:mm:ss
     * @param time 时间戳
     * @return 时间字符串
     */
    fun getSampleDateHourMinuteSecond(time: Long): String {
        return FORMAT_H_M_S.format(Date(time))
    }

    /**
     * 将时间戳转为字符串
     * 格式： yyyy-MM-dd
     * @param time 时间戳
     * @return 时间字符串
     */
    fun getSampleDate(time: Long): String {
        return FORMAT.format(Date(time))
    }
}
