package cai.lib.toast

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.Context
import android.content.res.Resources.NotFoundException
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import cai.lib.toast.interceptor.EmptyTextInterceptor

import cai.lib.toast.interceptor.ToastInterceptor
import cai.lib.toast.strategy.DefaultToastStrategy
import cai.lib.toast.strategy.ToastStrategy
import cai.lib.toast.style.DefaultToastStyle
import cai.lib.toast.style.ToastStyle


/**
 * Toast 工具类
 */
@SuppressWarnings("unchecked")
object ToastUtils {

    private var sInterceptor: ToastInterceptor? = null

    private var sStrategy: ToastStrategy? = null

    var toast: Toast? = null
        private set

    @JvmOverloads
    fun init(application: Application?, style: ToastStyle = DefaultToastStyle()) {
        if (application == null) {
            throw NullPointerException("ToastUtils.init(application) -> this application is null")
        }
        ToastStyle.application = application

        if (sInterceptor == null) {
            setToastInterceptor(EmptyTextInterceptor())
        }

        if (sStrategy == null) {
            setToastStrategy(DefaultToastStrategy())
        }

        setToast(sStrategy!!.create(application))

        setView(createTextView(application, style))

        setGravity(style.gravity, style.xOffset, style.yOffset)
    }

    @JvmStatic
    fun show(obj: Any?) {
        show(obj?.toString() ?: "null")
    }

    @JvmStatic
    fun show(id: Int) {
        checkToastState()
        try {
            show(context.resources.getText(id))
        } catch (ignored: NotFoundException) {
            show(id.toString())
        }
    }

    @JvmStatic
    @Synchronized
    fun show(text: CharSequence?) {
        checkToastState()
        if (sInterceptor!!.intercept(toast!!, text)) {
            return
        }
        sStrategy?.show(text)
    }

    @Synchronized
    fun cancel() {
        checkToastState()
        sStrategy?.cancel()
    }

    @SuppressLint("ObsoleteSdkInt")
    @JvmStatic
    fun setGravity(gravity: Int, xOffset: Int, yOffset: Int) {
        var temp = gravity
        checkToastState()
        // 适配 RTL布局
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            temp = Gravity.getAbsoluteGravity(gravity, context.resources.configuration.layoutDirection)
        }
        toast!!.setGravity(temp, xOffset, yOffset)
    }

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <V : View> getView(): V {
        checkToastState()
        return toast!!.view!! as V
    }

    @JvmStatic
    fun setView(id: Int) {
        checkToastState()
        setView(View.inflate(context, id, null))
    }

    @JvmStatic
    fun setView(view: View?) {
        checkToastState()
        // 当前必须用 Application 的上下文创建的 View，否则可能会导致内存泄露
        val context = view?.context
        require(!(context is Activity || context is Service)) { "The view must be initialized using the context of the application" }

        // 如果吐司已经创建，就重新初始化吐司
        if (toast != null) {
            // 取消原有吐司的显示
            toast!!.cancel()
            toast!!.view = view
        }
    }

    @JvmStatic
    fun initStyle(style: ToastStyle?) {
        // 如果吐司已经创建，就重新初始化吐司
        if (toast != null && style != null) {
            // 取消原有吐司的显示
            toast!!.cancel()
            toast!!.view = createTextView(context, style)
            toast!!.setGravity(style.gravity, style.xOffset, style.yOffset)
        }
    }

    @JvmStatic
    fun setToastStrategy(strategy: ToastStrategy?) {
        sStrategy = strategy
        if (toast != null) {
            sStrategy?.bind(toast!!)
        }
    }

    @JvmStatic
    fun setToastInterceptor(interceptor: ToastInterceptor?) {
        sInterceptor = interceptor
    }

    @JvmStatic
    fun setToast(toast: Toast?) {
        if (ToastUtils.toast != null && toast?.view == null) {
            // 移花接木
            toast!!.view = ToastUtils.toast!!.view
            toast.setGravity(
                ToastUtils.toast!!.gravity,
                ToastUtils.toast!!.xOffset,
                ToastUtils.toast!!.yOffset
            )
            toast.setMargin(ToastUtils.toast!!.horizontalMargin, ToastUtils.toast!!.verticalMargin)
        }
        ToastUtils.toast = toast
        sStrategy?.bind(ToastUtils.toast!!)
    }

    /**
     * 检查toast状态，如果未初始化请先调用[ToastUtils.init]
     */
    private fun checkToastState() {
        // 吐司工具类还没有被初始化，必须要先调用init方法进行初始化
        checkNotNull(toast) { "ToastUtils has not been initialized" }
    }

    private fun createTextView(context: Context?, style: ToastStyle): TextView {
        val textView = TextView(context)
        textView.id = R.id.message
        textView.setTextColor(style.textColor)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, style.textSize)

        // 适配布局反方向特性
        textView.setPaddingRelative(
            style.paddingStart,
            style.paddingTop,
            style.paddingEnd,
            style.paddingBottom
        )
        textView.layoutParams =
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        val drawable = GradientDrawable()
        drawable.setColor(style.backgroundColor)
        drawable.cornerRadius = style.cornerRadius.toFloat()
        textView.background = drawable
        textView.z = style.z.toFloat()
        if (style.maxLines > 0) {
            textView.maxLines = style.maxLines
        }
        return textView
    }

    private val context: Context
        get() {
            checkToastState()
            return toast!!.view!!.context
        }
}