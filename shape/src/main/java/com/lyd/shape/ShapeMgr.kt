package com.lyd.shape

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import androidx.core.view.LayoutInflaterCompat
import java.lang.reflect.Field

/**
 * @author Lin
 * @time 2021/11/12 15:56
 *
 * @desc 快速给View设置圆角背景
 *
 */

object ShapeMgr {

    // 圆角值
    // shape_radius
    // shape_radiusBottomLeft
    // shape_radiusBottomRight
    // shape_radiusTopLeft
    // shape_radiusTopRight
    // 背景色
    // shape_backgroundColor
    // 描边
    // shape_strokeWidth
    // shape_strokeColor
    // 渐变
    // shape_gradualColorStart
    // shape_gradualColorEnd
    // 按压效果
    // shape_pressedBackgroundColor
    // shape_pressedStrokeColor
    // 是否显示按压阴影效果
    // shape_showShadow

    /**
     * 绑定需要实现的界面
     *
     * 使用步骤：
     * 在需要设置的xml添加相关的配置(如： app:shape_radius="6dp")
     * 需要使用的Activity中使用bind方法
     * (Activity：可在onCreate/initView方法中使用)
     * fragment中无法使用，如果要使用需要在父Activity中执行bind方法
     *
     */
    @SuppressLint("SoonBlockedPrivateApi")
    fun bind(activity: Activity) {
        val layoutInflater = activity.layoutInflater
        try {
            //在LayoutInflater会通过mFactorySet对setFactory2重复设值进行限制
            //通过反射设值为false，在设置新的LayoutInflater.Factory2
            val field: Field = LayoutInflater::class.java.getDeclaredField("mFactorySet")
            field.isAccessible = true
            field.setBoolean(layoutInflater, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //使用factory2 设置布局加载工程
        val skinLayoutInflaterFactory = ShapeLayoutInflaterFactory()
        LayoutInflaterCompat.setFactory2(layoutInflater, skinLayoutInflaterFactory)
    }
}