package cai.lib.toast

import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import java.lang.reflect.InvocationTargetException


internal object Utils {

    @JvmStatic
    @SuppressLint("ObsoleteSdkInt")
    fun isNotificationsEnabled(context: Context): Boolean {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                context.getSystemService(NotificationManager::class.java).areNotificationsEnabled()
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> {
                val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
                try {
                    val method = appOps.javaClass.getMethod(
                        "checkOpNoThrow", Integer.TYPE, Integer.TYPE,
                        String::class.java
                    )
                    val field = appOps.javaClass.getDeclaredField("OP_POST_NOTIFICATION")
                    val value = field[Int::class.java] as Int
                    method.invoke(
                        appOps,
                        value,
                        context.applicationInfo.uid,
                        context.packageName
                    ) as Int == AppOpsManager.MODE_ALLOWED
                } catch (ignored: NoSuchMethodException) {
                    true
                } catch (ignored: NoSuchFieldException) {
                    true
                } catch (ignored: InvocationTargetException) {
                    true
                } catch (ignored: IllegalAccessException) {
                    true
                } catch (ignored: RuntimeException) {
                    true
                }
            }
            else -> {
                true
            }
        }
    }
}