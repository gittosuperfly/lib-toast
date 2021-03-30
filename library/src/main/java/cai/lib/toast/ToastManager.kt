package cai.lib.toast

import android.app.Application
import android.content.Context
import android.graphics.PixelFormat
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.WindowManager
import android.widget.Toast
import cai.lib.toast.strategy.ToastStrategy

class ToastManager(
    private val toast: Toast,
    private val application: Application
) : Handler(Looper.getMainLooper()) {

    var isShow = false
    var lifecycle = ToastActivityLifecycle.register(this, application)

    override fun handleMessage(msg: Message) {
        cancel()
    }

    fun show() {
        val params = WindowManager.LayoutParams()
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        params.format = PixelFormat.TRANSLUCENT
        params.windowAnimations = android.R.style.Animation_Toast
        params.flags = (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        params.packageName = application.packageName

        // 重新初始化位置
        params.gravity = toast.gravity
        params.x = toast.xOffset
        params.y = toast.yOffset
        params.verticalMargin = toast.verticalMargin
        params.horizontalMargin = toast.horizontalMargin

        try {
            val activity = lifecycle.currentActivity.get()
            if (activity != null && !activity.isFinishing) {
                val manager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                manager.addView(toast.view, params)
            }
            // 添加一个移除吐司的任务
            sendEmptyMessageDelayed(
                hashCode(),
                if (toast.duration == Toast.LENGTH_LONG) ToastStrategy.LONG_DURATION_TIMEOUT
                else ToastStrategy.SHORT_DURATION_TIMEOUT
            )
            isShow = true
        } catch (ignored: IllegalStateException) {
        } catch (ignored: WindowManager.BadTokenException) {
        }
    }


    fun cancel() {
        removeMessages(hashCode())
        if (isShow) {
            try {
                val activity = lifecycle.currentActivity.get()
                val manager = activity?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                manager.removeViewImmediate(toast.view)
            } catch (ignored: IllegalArgumentException) {

            }
            isShow = false
        }
    }

}