package cai.lib.toast.interceptor

import android.widget.Toast

/**
 * 空文本拦截器
 */
class EmptyTextInterceptor : ToastInterceptor {
    override fun intercept(toast: Toast, text: CharSequence?): Boolean = text.isNullOrEmpty()
}