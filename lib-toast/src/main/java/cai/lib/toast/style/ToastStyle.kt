package cai.lib.toast.style

import android.app.Application
import android.util.TypedValue

interface ToastStyle {
    /**
     * 吐司的重心
     */
    val gravity: Int

    /**
     * X轴偏移
     */
    val xOffset: Int

    /**
     * Y轴偏移
     */
    val yOffset: Int

    /**
     * 吐司 Z 轴坐标
     */
    val z: Int

    /**
     * 圆角大小
     */
    val cornerRadius: Int

    /**
     * 背景颜色
     */
    val backgroundColor: Int

    /**
     * 文本颜色
     */
    val textColor: Int

    /**
     * 文本大小
     */
    val textSize: Float

    /**
     * 最大行数
     */
    val maxLines: Int

    /**
     * 开始内边距
     */
    val paddingStart: Int

    /**
     * 顶部内边距
     */
    val paddingTop: Int

    /**
     * 结束内边距
     */
    val paddingEnd: Int

    /**
     * 底部内边距
     */
    val paddingBottom: Int

    companion object {
        var application: Application? = null

        fun dp2px(dpValue: Float): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dpValue,
                application?.resources?.displayMetrics
            ).toInt()
        }

        fun sp2px(spValue: Float): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                spValue,
                application?.resources?.displayMetrics
            ).toInt()
        }
    }
}