package cai.lib.toast.toasts

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.forEach

/**
 * 基础toast类
 */
open class BaseToast constructor(context: Context) : Toast(context) {

    lateinit var messageView: TextView

    override fun setView(view: View) {
        super.setView(view)
        findMessageView(view)
    }

    override fun setText(s: CharSequence?) {
        messageView.text = s
    }

    companion object {

        fun findMessageView(view: View): TextView {
            if (view is TextView) {
                return view
            } else if (view.findViewById<View>(android.R.id.message) is TextView) {
                return view.findViewById(android.R.id.message)
            } else if (view is ViewGroup) {
                val textView: TextView? = findTextView(view)
                if (textView != null) {
                    return textView
                }
            }
            throw IllegalArgumentException("The layout must contain a TextView")
        }

        private fun findTextView(viewGroup: ViewGroup): TextView? {
            viewGroup.forEach {
                if (it is TextView) {
                    return it
                } else if (it is ViewGroup) {
                    val view = findTextView(it)
                    if (view != null) {
                        return view
                    }
                }
            }
            return null
        }
    }
}