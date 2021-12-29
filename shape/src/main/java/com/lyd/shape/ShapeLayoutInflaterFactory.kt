package com.lyd.shape

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import java.lang.reflect.Constructor


class ShapeLayoutInflaterFactory : LayoutInflater.Factory2 {

    //记录对应VIEW的构造函数
    private val mConstructorSignature = arrayOf(Context::class.java, AttributeSet::class.java)

    private val mConstructorMap = HashMap<String, Constructor<out View?>>()

    private val shapeAttribute: ShapeAttribute = ShapeAttribute()

    private val mClassPrefixList = arrayOf(
        "android.widget.",
        "android.webkit.",
        "android.app.",
        "android.view."
    )

    override fun onCreateView(
        parent: View?,
        //View的名称，如果是系统View返回的是View的名称(如TextView)，第三方返回的是View的包名+名称(如com.itop.MyView)
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        //创建系统自由的View
        var view = createSDKView(name, context, attrs)
        if (null == view) {
            //创建自定义View或者第三方View
            view = createView(name, context, attrs)
        }
        if (null != view) {
            //开始检查View的属性名称
            shapeAttribute.look(view, attrs)
        }
        return view
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        //Factory1回调的方法
        return null
    }

    /**
     * 创建系统自带的View，如TextView。
     * 因为通过反射创建View，不获取View的包名+名称无法通过反射创建
     *
     */
    private fun createSDKView(name: String, context: Context, attrs: AttributeSet): View? {
        //如果包含 . 则不是SDK中的view 可能是自定义view包括support库中的View
        if (-1 != name.indexOf('.')) {
            return null
        }
        //不包含就要在解析的 节点 name前，拼上： android.widget. 等尝试去反射
        for (i in mClassPrefixList.indices) {
            //因为通过反射创建View，不获取View的包名+名称无法通过反射创建
            //尝试通过包名和类名创建控件
            val view = createView(mClassPrefixList[i] + name, context, attrs)
            if (view != null) {
                return view
            }
        }
        return null
    }

    /**
     * 通过反射创建View
     */
    private fun createView(name: String, context: Context, attrs: AttributeSet): View? {
        val constructor: Constructor<out View?>? = findConstructor(context, name)
        try {
            return constructor?.newInstance(context, attrs)
        } catch (e: Exception) {
        }
        return null
    }

    /**
     * 查找类
     */
    private fun findConstructor(context: Context, name: String): Constructor<out View?>? {
        var constructor: Constructor<out View?>? = mConstructorMap[name]
        if (constructor == null) {
            try {
                val clazz = context.classLoader.loadClass(name).asSubclass(
                    View::class.java
                )
                constructor = clazz.getConstructor(*mConstructorSignature)
                mConstructorMap[name] = constructor
            } catch (e: java.lang.Exception) {
            }
        }
        return constructor
    }
}