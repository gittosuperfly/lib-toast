package cai.lib.toast

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.lang.ref.WeakReference

class ToastActivityLifecycle(private val toastManager: ToastManager) :
    Application.ActivityLifecycleCallbacks {

    var currentActivity: WeakReference<Activity?> = WeakReference(null)

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        currentActivity = WeakReference(activity)
    }

    override fun onActivityStarted(activity: Activity) {
        currentActivity = WeakReference(activity)
        currentActivity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = WeakReference(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        if (toastManager.isShow) {
            toastManager.cancel()
        }
    }

    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {
        currentActivity.clear()
    }

    companion object {
        fun register(toast: ToastManager, application: Application): ToastActivityLifecycle {
            val activityLifecycle = ToastActivityLifecycle(toast)
            application.registerActivityLifecycleCallbacks(activityLifecycle)
            return activityLifecycle
        }
    }
}