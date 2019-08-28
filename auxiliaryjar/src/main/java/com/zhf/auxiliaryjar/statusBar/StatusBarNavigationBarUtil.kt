package com.zhf.auxiliaryjar.statusBar

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Environment
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.WindowManager

import java.io.File
import java.io.FileInputStream
import java.util.Properties

/**
 * 沉浸式
 * 修改状态栏文字颜色
 * Created by zhf on 2019/8/20 10:29
 */
object StatusBarNavigationBarUtil {

    /**
     * 设置状态栏颜色
     * 设置导航栏颜色
     * @param statusBar ： true 状态栏颜色 透明
     * @param navigationBar ： true 导航栏颜色 透明
     */
    fun immersiveStatusBarNavigationBar(activity: Activity, statusBar: Boolean, navigationBar: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.attributes.systemUiVisibility =
                activity.window.attributes.systemUiVisibility or (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            activity.window
                .clearFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                )
            try {
                val drawsSysBackgroundsField = WindowManager.LayoutParams::class.java
                    .getField("FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS")
                activity.window.addFlags(drawsSysBackgroundsField.getInt(null))
                if (statusBar) {
                    val setStatusBarColorMethod = Window::class.java
                        .getDeclaredMethod("setStatusBarColor", Int::class.javaPrimitiveType!!)
                    setStatusBarColorMethod.invoke(activity.window, Color.TRANSPARENT)
                }
                if (navigationBar) {
                    val setNavigationBarColorMethod = Window::class.java
                        .getDeclaredMethod(
                            "setNavigationBarColor",
                            Int::class.javaPrimitiveType!!
                        )
                    setNavigationBarColorMethod.invoke(activity.window, Color.TRANSPARENT)
                }
            } catch (e: Exception) {

            }

        }
    }


    /**
     * 修改状态栏文字颜色，这里小米，魅族区别对待。
     * @param activity
     * @param dark true:黑色; false:白色
     */
    fun setLightStatusBar(activity: Activity, dark: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            when (RomUtils.lightStatusBarAvailableRomType) {
                RomUtils.AvailableRomType.MIUI -> MIUISetStatusBarLightMode(activity, dark)

                RomUtils.AvailableRomType.FLYME -> setFlymeLightStatusBar(activity, dark)

                RomUtils.AvailableRomType.ANDROID_NATIVE -> setAndroidNativeLightStatusBar(activity, dark)
            }
        }
    }


    /**
     * 修改状态栏文字颜色
     * @param activity
     * @param dark true:黑色; false:白色
     */
    fun MIUISetStatusBarLightMode(activity: Activity, dark: Boolean): Boolean {
        var result = false
        val window = activity.window
        if (window != null) {
            val clazz = window.javaClass
            try {
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                var darkModeFlag = field.getInt(layoutParams)
                val extraFlagField =
                    clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag)//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag)//清除黑色字体
                }
                result = true

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && RomUtils.isMiUIV7OrAbove) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (dark) {
                        activity.window.decorView.systemUiVisibility =
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    } else {
                        activity.window.decorView.systemUiVisibility =
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    }
                }
            } catch (e: Exception) {

            }

        }
        return result
    }


    /**
     * 判断是否有虚拟按键
     * @param context
     * @return
     */
    fun checkDeviceHasNavigationBar(context: Context): Boolean {
        var hasNavigationBar = false
        val rs = context.getResources()
        val id = rs.getIdentifier("config_showNavigationBar", "bool", "android")
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id)
        }
        try {
            val systemPropertiesClass = Class.forName("android.os.SystemProperties")
            val m = systemPropertiesClass.getMethod("get", String::class.java)
            val navBarOverride = m.invoke(systemPropertiesClass, "qemu.hw.mainkeys") as String
            if ("1" == navBarOverride) {
                hasNavigationBar = false
            } else if ("0" == navBarOverride) {
                hasNavigationBar = true
            }
        } catch (e: Exception) {
        }

        return hasNavigationBar
    }

    /**
     * 隐藏虚拟按键的同时并隐藏了状状态栏
     * @param window
     */
    fun solveNavigationBar(window: Window) {
        //保持布局状态
        var uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                //布局位于状态栏下方
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                //全屏
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                //隐藏导航栏
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (Build.VERSION.SDK_INT >= 19) {
            uiOptions = uiOptions or 0x00001000
        } else {
            uiOptions = uiOptions or View.SYSTEM_UI_FLAG_LOW_PROFILE
        }
        window.decorView.systemUiVisibility = uiOptions
    }


    private fun setFlymeLightStatusBar(activity: Activity?, dark: Boolean): Boolean {
        var result = false
        if (activity != null) {
            try {
                val lp = activity.window.attributes
                val darkFlag = WindowManager.LayoutParams::class.java
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags = WindowManager.LayoutParams::class.java
                    .getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                meizuFlags.isAccessible = true
                val bit = darkFlag.getInt(null)
                var value = meizuFlags.getInt(lp)
                if (dark) {
                    value = value or bit
                } else {
                    value = value and bit.inv()
                }
                meizuFlags.setInt(lp, value)
                activity.window.attributes = lp
                result = true
            } catch (e: Exception) {
            }

        }
        return result
    }

    private fun setAndroidNativeLightStatusBar(activity: Activity, dark: Boolean) {
        val decor = activity.window.decorView
        if (dark) {
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    internal class RomUtils {

        object AvailableRomType {
            val MIUI = 1
            val FLYME = 2
            val ANDROID_NATIVE = 3
            val NA = 4
        }

        companion object {

            //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错
            val lightStatusBarAvailableRomType: Int
                get() {
                    if (isMiUIV7OrAbove) {
                        return AvailableRomType.ANDROID_NATIVE
                    }

                    if (isMiUIV6OrAbove) {
                        return AvailableRomType.MIUI
                    }

                    if (isFlymeV4OrAbove) {
                        return AvailableRomType.FLYME
                    }

                    return if (isAndroidMOrAbove) {
                        AvailableRomType.ANDROID_NATIVE
                    } else AvailableRomType.NA

                }

            //Flyme V4的displayId格式为 [Flyme OS 4.x.x.xA]
            //Flyme V5的displayId格式为 [Flyme 5.x.x.x beta]
            private//版本号4以上，形如4.x.
            val isFlymeV4OrAbove: Boolean
                get() {
                    val displayId = Build.DISPLAY
                    if (!TextUtils.isEmpty(displayId) && displayId.contains("Flyme")) {
                        val displayIdArray =
                            displayId.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        for (temp in displayIdArray) {
                            if (temp.matches("^[4-9]\\.(\\d+\\.)+\\S*".toRegex())) {
                                return true
                            }
                        }
                    }
                    return false
                }

            //Android Api 23以上
            private val isAndroidMOrAbove: Boolean
                get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

            private val KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code"

            private val isMiUIV6OrAbove: Boolean
                get() {
                    try {
                        val properties = Properties()
                        properties.load(FileInputStream(File(Environment.getRootDirectory(), "build.prop")))
                        val uiCode = properties.getProperty(KEY_MIUI_VERSION_CODE, null)
                        if (uiCode != null) {
                            val code = Integer.parseInt(uiCode)
                            return code >= 4
                        } else {
                            return false
                        }

                    } catch (e: Exception) {
                        return false
                    }
                }

            val isMiUIV7OrAbove: Boolean
                get() {
                    try {
                        val properties = Properties()
                        properties.load(FileInputStream(File(Environment.getRootDirectory(), "build.prop")))
                        val uiCode = properties.getProperty(KEY_MIUI_VERSION_CODE, null)
                        if (uiCode != null) {
                            val code = Integer.parseInt(uiCode)
                            return code >= 5
                        } else {
                            return false
                        }

                    } catch (e: Exception) {
                        return false
                    }

                }
        }
    }

}
