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

    /**
     * 获取两个时间相差的天数
     * @param startDate 开始时间，Date类型
     * @param endDate   结束时间，Date类型
     * @return 天数
     */
    fun getTimeDifferenceDay(startDate: Date, endDate: Date): Int {
        return getTimeDifference(startDate, endDate, "day")
    }

    /**
     * 获取两个时间相差的小时数
     * @param startDate 开始时间，Date类型
     * @param endDate   结束时间，Date类型
     * @return
     */
    fun getTimeDifferenceHour(startDate: Date, endDate: Date): Int {
        return getTimeDifference(startDate, endDate, "hour")
    }

    /**
     * 获取两个时间相差的分钟数
     * @param startDate 开始时间，Date类型
     * @param endDate   结束时间，Date类型
     * @return
     */
    fun getTimeDifferenceMin(startDate: Date, endDate: Date): Int {
        return getTimeDifference(startDate, endDate, "min")
    }

    /**
     * 获取两个时间相差的秒数
     * @param startDate 开始时间，Date类型
     * @param endDate   结束时间，Date类型
     * @return
     */
    fun getTimeDifferenceSecond(startDate: Date, endDate: Date): Int {
        return getTimeDifference(startDate, endDate, "second")
    }


    /**
     * 获取与当前时间相差的分钟数
     * @param startDateStr 开始时间，String类型  格式：yyyy-MM-dd HH:mm:ss
     * @return
     */
    fun getTimeDifferenceMin(startDateStr: String): Int {
        var startDate = FORMAT_H_M_S.parse(startDateStr)
        var endDate = Date()
        return getTimeDifference(startDate, endDate, "min")
    }


    /**
     * 获取时间差
     * @param startDate 开始时间，Date类型
     * @param endDate   结束时间，Date类型
     * @param type   day:天，hour:小时，min:分钟，second:秒
     */
    private fun getTimeDifference(startDate: Date, endDate: Date, type: String): Int {
        val diff = endDate.time - startDate.time
        //以天数为单位取整   
        val day = diff / (1000 * 60 * 60 * 24)
        //以小时为单位取整   
        val hour = diff / (60 * 60 * 1000) - day * 24
        //以分钟为单位取整   
        val min = diff / (60 * 1000) - day * 24 * 60 - hour * 60
        //以秒为单位   
        val second = diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60
        return when (type) {
            "day" -> day.toInt()
            "hour" -> hour.toInt()
            "min" -> min.toInt()
            "second" -> second.toInt()
            else -> 0
        }
    }
}
