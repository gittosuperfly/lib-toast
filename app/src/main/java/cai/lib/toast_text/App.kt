package cai.lib.toast_text

import android.app.Application
import android.content.Context
import cai.lib.toast.ToastUtils

class App : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        ToastUtils.init(this)
    }

    override fun onCreate() {
        super.onCreate()
        ToastUtils.init(this)
    }
}