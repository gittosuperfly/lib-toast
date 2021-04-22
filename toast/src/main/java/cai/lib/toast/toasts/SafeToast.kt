package cai.lib.toast.toasts

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Handler
import android.widget.Toast
import cai.lib.toast.SafeHandler
import java.lang.reflect.Field


@TargetApi(Build.VERSION_CODES.KITKAT)
class SafeToast constructor(context: Context) : BaseToast(context) {
    init {
        try {
            //获取 mTN
            val field = Toast::class.java.getDeclaredField("mTN")
            field.isAccessible = true
            val mTN = field.get(this)

            //获取 mTN 中的 mHandler 字段对象
            val handlerField: Field = field.type.getDeclaredField("mHandler")
            handlerField.isAccessible = true
            val handler = handlerField[mTN] as Handler

            //偷梁换柱
            handlerField[mTN] = SafeHandler(handler)

        } catch (ignored: IllegalAccessException) {
        } catch (ignored: NoSuchFieldException) {
        }
    }
}