package cai.lib.toast.toasts

import android.app.Application
import android.view.View
import cai.lib.toast.ToastManager

class CustomToast constructor(application: Application) : BaseToast(application) {

    private val manager = ToastManager(this, application)

    private var mGravity: Int = 0
    private var mXOffset: Int = 0
    private var mYOffset: Int = 0

    private var mHorizontalMargin = 0f
    private var mVerticalMargin = 0f

    private lateinit var view: View

    override fun show() {
        manager.show()
    }

    override fun cancel() {
        manager.cancel()
    }

    override fun getView(): View? {
        return view
    }

    override fun setView(view: View) {
        this.view = view
        messageView = findMessageView(view)
    }

    override fun setGravity(gravity: Int, xOffset: Int, yOffset: Int) {
        mGravity = gravity
        mXOffset = xOffset
        mYOffset = yOffset
    }

    override fun setMargin(horizontalMargin: Float, verticalMargin: Float) {
        mHorizontalMargin = horizontalMargin
        mVerticalMargin = verticalMargin
    }

    override fun getGravity() = mGravity

    override fun getXOffset() = mXOffset

    override fun getYOffset() = mYOffset

    override fun getHorizontalMargin() = mHorizontalMargin

    override fun getVerticalMargin() = mVerticalMargin

}