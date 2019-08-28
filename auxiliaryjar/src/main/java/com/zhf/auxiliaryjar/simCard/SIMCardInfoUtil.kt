package com.zhf.auxiliaryjar.simCard

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.TelephonyManager

/**
 * 获取本机手机号码以及运营商
 * Created by zhf on 2019/8/26 11:49
 */
class SIMCardInfoUtil(context: Context) {
    /**
     * TelephonyManager提供设备上获取通讯服务信息的入口。 应用程序可以使用这个类方法确定的电信服务商和国家 以及某些类型的用户访问信息。
     * 应用程序也可以注册一个监听器到电话收状态的变化。不需要直接实例化这个类
     * 使用Context.getSystemService(Context.TELEPHONY_SERVICE)来获取这个类的实例。
     */
    private val telephonyManager: TelephonyManager =
        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    /**
     * 国际移动用户识别码
     */
    private var IMSI: String? = null

    /**
     * Role:获取当前设置的电话号码 <BR></BR>
     */
    val nativePhoneNumber: String
        @SuppressLint("MissingPermission")
        get() {
            return telephonyManager.line1Number
        }

    /**
     * @param isEncry 是否加密 true: 133 **** 1234
     * @param phone 手机号码
     */
    fun getPhoneEncry(isEncry: Boolean, phone: String): String {
        if (phone == "") return ""
        var containStr = "+86"
        var phoneCode: String
        phoneCode = if (!phone.contains(containStr)) {
            phone
        } else {
            phone.substring(phone.indexOf(containStr) + containStr.length, phone.length)
        }
        phoneCode = phoneCode.substring(0, 3) + " **** " + phoneCode.substring(phoneCode.length - 4, phoneCode.length)
        return if (isEncry) phoneCode
        else phone
    }

    /**
     * 获取本机手机号码
     * @param isEncry 是否加密 true: 133 **** 1234
     */
    fun getPhoneLocalEncry(isEncry: Boolean): String {
        var containStr = "+86"
        var phone = nativePhoneNumber
        if (phone == "") return ""
        var phoneCode: String
        phoneCode = if (!phone.contains(containStr)) {
            phone
        } else {
            phone.substring(phone.indexOf(containStr) + containStr.length, phone.length)
        }
        phoneCode = phoneCode.substring(0, 3) + " **** " + phoneCode.substring(phoneCode.length - 4, phoneCode.length)
        return if (isEncry) phoneCode
        else phone
    }

    /**
     * Role:Telecom service providers获取手机服务商信息 <BR></BR>
     * 需要加入权限<uses-permission android:name="android.permission.READ_PHONE_STATE"></uses-permission> <BR></BR>
     *
     */
    // 返回唯一的用户ID;就是这张卡的编号的
    // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
    //        Log.i("tag",IMSI);
    val providersName: String?
        @SuppressLint("MissingPermission")
        get() {
            var ProvidersName: String? = null
            IMSI = telephonyManager.subscriberId
            if (IMSI == null || "" == IMSI) {
                ProvidersName = ""
            } else if (IMSI!!.startsWith("46000") || IMSI!!.startsWith("46002")) {
                ProvidersName = "中国移动"
            } else if (IMSI!!.startsWith("46001")) {
                ProvidersName = "中国联通"
            } else if (IMSI!!.startsWith("46003")) {
                ProvidersName = "中国电信"
            }
            return ProvidersName
        }


    /**
     * 检测Sim卡是否可用
     * @param context
     * @return
     */
    fun isExistSim(context: Context): Boolean {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.simState != TelephonyManager.SIM_STATE_ABSENT
    }
}
