package cai.lib.toast.strategy

import android.app.Application
import android.widget.Toast

interface ToastStrategy {
    /**
     * 创建 Toast
     */
    fun create(application: Application): Toast

    /**
     * 绑定 Toast
     */
    fun bind(toast: Toast)

    /**
     * 显示 Toast
     */
    fun show(text: CharSequence?)

    /**
     * 取消 Toast
     */
    fun cancel()

    companion object {
        const val SHORT_DURATION_TIMEOUT = 2000L
        const val LONG_DURATION_TIMEOUT = 3500L
    }
}