package cai.lib.toast_text

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import cai.lib.toast.ToastUtils

class MainActivity : AppCompatActivity() {

    var number = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.helloTv).setOnClickListener {
            ToastUtils.show(number++)
        }
    }
}