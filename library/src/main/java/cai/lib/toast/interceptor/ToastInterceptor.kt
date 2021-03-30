package cai.lib.toast.interceptor

import android.widget.Toast

interface ToastInterceptor {
    /**
     * 根据显示的文本决定是否拦截该 Toast
     */
    fun intercept(toast: Toast, text: CharSequence?): Boolean
}