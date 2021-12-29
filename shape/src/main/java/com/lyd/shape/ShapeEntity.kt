package com.lyd.shape

/**
 * @author Lin
 * @time 2021/11/11 18:53
 *
 * @desc 保存Shape的属性
 */
data class ShapeEntity(
    /** 圆角 */
    var radius: Float = 0F,
    var radiusBottomLeft: Float = 0F,
    var radiusBottomRight: Float = 0F,
    var radiusTopLeft: Float = 0F,
    var radiusTopRight: Float = 0F,
    /** 背景颜色 */
    var backgroundColor: Int = 0,   //正常背景颜色
    /** 描边 */
    var strokeColor: Int = 0,
    var strokeWidth: Float = 0F,
    /** 渐变 */
    var gradualColorStart: Int = 0, //开始颜色
    var gradualColorEnd: Int = 0,   //结束颜色
    /** 按压效果 */
    var pressedBackgroundColor: Int = 0,  //按压时背景颜色
    var pressedStrokeColor: Int = 0,  //按压时描边颜色
    /** 是否显示阴影效果 */
    var isShowShadow:Boolean = false
)