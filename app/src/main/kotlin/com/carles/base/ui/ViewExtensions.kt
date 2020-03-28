package com.carles.base.ui

import android.app.Activity
import android.content.res.Resources
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.carles.base.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun ViewGroup.inflate(
    @LayoutRes
    layoutRes: Int
) = LayoutInflater.from(context).inflate(layoutRes, this, false)

inline fun AppCompatActivity.consume(f: () -> Unit): Boolean {
    f()
    return true
}

fun AppCompatActivity.getStrings(ids: List<Int>) = ids.map { getString(it) }.toTypedArray()

fun Int.toPx() = this / Resources.getSystem().displayMetrics.density

fun View.setDebounceClickListener(action: () -> Unit, debounceTime: Long = 2000L) {
    var lastClickTime: Long = 0
    this.setOnClickListener(object : View.OnClickListener {
        override fun onClick(v: View?) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            lastClickTime = SystemClock.elapsedRealtime()
            action()
        }
    })
}

fun Activity.showError(message: String?, onRetry: (() -> Unit)? = null) {
    val alertDialogBuilder = MaterialAlertDialogBuilder(this).setMessage(message)
    if (onRetry != null) {
        alertDialogBuilder.setCancelable(false).setPositiveButton(R.string.error_retry) { _, _ -> onRetry() }
    }
    alertDialogBuilder.create().show()
}