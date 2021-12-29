package com.lyd.shape

import android.animation.AnimatorInflater
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View

class ShapeAttribute {

    fun look(view: View, attrs: AttributeSet) {
        val entity = getShapeInfo(view.context, attrs) ?: return
        val drawableNormal = GradientDrawable()  //正常
        var drawablePressed: GradientDrawable? = null //按下
        //判断是否创建按下效果
        if (entity.pressedBackgroundColor != 0 || entity.pressedStrokeColor != 0) {
            drawablePressed = GradientDrawable()
        }
        entity.apply {
            //背景色/渐变(渐变不支持按下效果)
            if (gradualColorStart != 0 && gradualColorEnd != 0) {
                //正常渐变色
                val colors = intArrayOf(gradualColorStart, gradualColorEnd)
                drawableNormal.orientation = GradientDrawable.Orientation.LEFT_RIGHT
                drawableNormal.setColors(colors)
            } else {
                //正常颜色
                drawableNormal.setColor(backgroundColor)
                drawablePressed?.setColor(pressedBackgroundColor)
            }
            //设置圆角
            if (radius == 0F) {
                if (radiusBottomLeft != 0f || radiusBottomRight != 0f || radiusTopLeft != 0f || radiusTopRight != 0f) {
                    //遵循左上，右上，右下，左下
                    val radii = floatArrayOf(
                        radiusTopLeft, radiusTopLeft,
                        radiusTopRight, radiusTopRight,
                        radiusBottomRight, radiusBottomRight,
                        radiusBottomLeft, radiusBottomLeft
                    )
                    drawableNormal.cornerRadii = radii
                    drawablePressed?.cornerRadii = radii
                }
            } else {
                drawableNormal.cornerRadius = radius
                drawablePressed?.cornerRadius = radius
            }
            //描边
            if (strokeWidth != 0F && strokeColor != 0) {
                drawableNormal.setStroke(strokeWidth.toInt(), strokeColor)
                drawablePressed?.setStroke(strokeWidth.toInt(), pressedStrokeColor)
            }
        }

        //设置背景
        if (drawablePressed != null) { //有按下效果
            val stateListDrawable = StateListDrawable()
            stateListDrawable.addState(intArrayOf(android.R.attr.state_pressed), drawablePressed)
            stateListDrawable.addState(intArrayOf(), drawableNormal)
            view.background = stateListDrawable
        } else { //没有按下效果
            view.background = drawableNormal
        }

        //设置点击动画效果
        //因为使用代码进行setBackground的时候，Button的默认点击效果会消失，所以再次为button添加动画效果 5.0上有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && entity.isShowShadow) {
            view.stateListAnimator = AnimatorInflater.loadStateListAnimator(
                view.context,
                R.animator.common_selector_bg_button_animator
            )
        }

        view.elevation = view.elevation //重新设置阴影
    }

    /**
     * 获取XML中配置好的信息，并保存到ShapeEntity中。
     * @return 如果返回为空表示没有设置
     */
    private fun getShapeInfo(context: Context, attrs: AttributeSet): ShapeEntity? {
        val isHave = checkNameHaveShapeToAttrs(attrs)
        if (!isHave) {
            return null
        }
        val shape = ShapeEntity()
        val typed = context.obtainStyledAttributes(attrs, R.styleable.Shape, 0, 0)
        shape.apply {
            /** 圆角 */
            radius = typed.getDimension(R.styleable.Shape_shape_radius, 0F)
            radiusTopLeft = typed.getDimension(R.styleable.Shape_shape_radiusTopLeft, 0F)
            radiusTopRight = typed.getDimension(R.styleable.Shape_shape_radiusTopRight, 0F)
            radiusBottomLeft = typed.getDimension(R.styleable.Shape_shape_radiusBottomLeft, 0F)
            radiusBottomRight = typed.getDimension(R.styleable.Shape_shape_radiusBottomRight, 0F)
            /** 背景 */
            backgroundColor = typed.getColor(R.styleable.Shape_shape_backgroundColor, 0)
            /** 描边 */
            strokeWidth = typed.getDimension(R.styleable.Shape_shape_strokeWidth, 0F)
            strokeColor = typed.getColor(R.styleable.Shape_shape_strokeColor, 0)
            /** 渐变 */
            gradualColorStart = typed.getColor(R.styleable.Shape_shape_gradualColorStart, 0)
            gradualColorEnd = typed.getColor(R.styleable.Shape_shape_gradualColorEnd, 0)
            /** 按压效果 */
            pressedBackgroundColor =
                typed.getColor(R.styleable.Shape_shape_pressedBackgroundColor, 0) //按压时背景颜色
            pressedStrokeColor =
                typed.getColor(R.styleable.Shape_shape_pressedStrokeColor, 0)  //按压时描边颜色
            /** 阴影 */
            isShowShadow = typed.getBoolean(R.styleable.Shape_shape_showShadow, false)
        }
        typed.recycle()
        return shape
    }

    /**
     * 判断AttributeSet中是否存在AttributeName以shape_开头
     */
    private fun checkNameHaveShapeToAttrs(attrs: AttributeSet): Boolean {
        for (i in 0 until attrs.attributeCount) {
            val name = attrs.getAttributeName(i)
            val index = name?.indexOf("shape_") ?: -1 //字符串为空或者找不到返回-1
            if (index >= 0) {
                return true
            }
        }
        return false
    }


}