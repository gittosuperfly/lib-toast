package cai.lib.toast

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.WindowManager

/**
 * 处理Toast 7.1弹出可能崩溃的问题
 *
 * 目前发现在 Android 7.1 主线程被阻塞之后弹吐司会导致崩溃，可使用 Thread.sleep(5000) 进行复现
 * 查看源码得知 Google 已经在 Android 8.0 已经修复了此问题
 * 主线程阻塞之后 Toast 也会被阻塞，Toast 因为超时导致 Window Token 失效
 */
class SafeHandler(private val mHandler: Handler) : Handler(Looper.getMainLooper()) {
    override fun handleMessage(msg: Message) {
        try {
            mHandler.handleMessage(msg)
        } catch (ignored: WindowManager.BadTokenException) {
        } catch (ignored: IllegalStateException) {
        }
    }
}