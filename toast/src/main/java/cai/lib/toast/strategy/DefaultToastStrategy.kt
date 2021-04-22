package cai.lib.toast.strategy

import android.app.Application
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Toast
import cai.lib.toast.Utils
import cai.lib.toast.toasts.BaseToast
import cai.lib.toast.toasts.SafeToast
import cai.lib.toast.strategy.ToastStrategy.Companion.LONG_DURATION_TIMEOUT
import cai.lib.toast.strategy.ToastStrategy.Companion.SHORT_DURATION_TIMEOUT
import cai.lib.toast.toasts.CustomToast
import java.util.*
import java.util.concurrent.ArrayBlockingQueue

class DefaultToastStrategy : Handler(Looper.getMainLooper()), ToastStrategy {

    companion object {
        const val DELAY_TIMEOUT = 0L
        const val MAX_TOAST_CAPACITY = 5

        const val TYPE_SHOW: Int = 1
        const val TYPE_CONTINUE: Int = 2
        const val TYPE_CANCEL: Int = 3
    }

    @Volatile
    private var queue: Queue<CharSequence?> = getToastQueue()

    @Volatile
    private var isOnShow = false

    private lateinit var toast: Toast


    override fun create(application: Application): Toast {
        val toast: Toast
        // 初始化吐司
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                // 适配 Android 11 无法使用自定义 Toast 的问题
                // 官方文档：https://developer.android.google.cn/preview/features/toasts
                toast = CustomToast(application)
            }
            Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1 -> {
                // 处理 Android 7.1 上 Toast 在主线程被阻塞后会导致报错的问题
                toast = SafeToast(application)
            }
            else -> {
                val check =  // 对比不同版本的 NMS 的源码发现这个问题在 Android 9.0 已经被谷歌修复了
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ||  // 判断当前应用是否有通知栏权限，如果关闭会导致弹 Toast 无法显示
                            Utils.isNotificationsEnabled(application) || "xiaomi" == Build.MANUFACTURER.toLowerCase(
                        Locale.ROOT
                    )
                toast = if (check) {
                    // 检查通过，返回正常类型的 Toast 即可
                    BaseToast(application)
                } else {
                    // 修复关闭通知栏权限后 Toast 不显示的问题
                    CustomToast(application)
                }
            }
        }
        return toast
    }

    override fun bind(toast: Toast) {
        this.toast = toast
    }

    override fun show(text: CharSequence?) {
        if (queue.isEmpty() || !queue.contains(text)) {
            if (!queue.offer(text)) {
                queue.poll()
                queue.offer(text)
            }
        }
        if (!isOnShow) {
            isOnShow = true
            sendEmptyMessageDelayed(TYPE_SHOW, DELAY_TIMEOUT)
        }
    }

    override fun cancel() {
        if (isOnShow) {
            isOnShow = false
            sendEmptyMessage(TYPE_CANCEL)
        }
    }

    override fun handleMessage(msg: Message) {
        when (msg.what) {
            TYPE_SHOW -> {
                // 返回队列头部的元素，如果队列为空，则返回null
                val text: CharSequence? = queue.peek()
                if (text != null) {
                    toast.setText(text)
                    toast.show()
                    // 等这个 Toast 显示完后再继续显示，要加上一些延迟
                    // 不然在某些手机上 Toast 可能会来不及消失就要进行显示，这样是显示不出来的
                    sendEmptyMessageDelayed(
                        TYPE_CONTINUE, (getToastDuration(text) + DELAY_TIMEOUT)
                    )
                } else {
                    isOnShow = false
                }
            }
            TYPE_CONTINUE -> {
                queue.poll()
                if (!queue.isEmpty()) {
                    sendEmptyMessage(TYPE_SHOW)
                } else {
                    isOnShow = false
                }
            }
            TYPE_CANCEL -> {
                isOnShow = false
                queue.clear()
                toast.cancel()
            }
        }
    }

    private fun getToastQueue(): Queue<CharSequence?> {
        return ArrayBlockingQueue(MAX_TOAST_CAPACITY)
    }

    /**
     * 根据文本来获取吐司的显示时长
     */
    private fun getToastDuration(text: CharSequence): Int {
        // 如果显示的文字超过了 20 个字符就显示长吐司，否则显示短吐司
        return if (text.length > 20) LONG_DURATION_TIMEOUT.toInt() else SHORT_DURATION_TIMEOUT.toInt()
    }

}