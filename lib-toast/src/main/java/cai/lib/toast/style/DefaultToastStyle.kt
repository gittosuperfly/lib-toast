package cai.lib.toast.style

import cai.lib.toast.style.ToastStyle.Companion.dp2px
import cai.lib.toast.style.ToastStyle.Companion.sp2px

open class DefaultToastStyle : ToastStyle {
    override val backgroundColor: Int get() = -0xcccccd
    override val textColor: Int get() = -0x1c1c1d
    override val textSize: Float get() = sp2px(12f).toFloat()
    override val maxLines: Int get() = 5
    override val paddingStart: Int get() = dp2px(16f)
    override val paddingTop: Int get() = dp2px(14f)
    override val paddingEnd: Int get() = paddingStart
    override val paddingBottom: Int get() = paddingTop
    override val yOffset: Int get() = dp2px(200f)
    override val z: Int get() = 0
    override val cornerRadius: Int get() = dp2px(6f)
    override val gravity: Int get() = dp2px(6f)
    override val xOffset: Int get() = 0
}