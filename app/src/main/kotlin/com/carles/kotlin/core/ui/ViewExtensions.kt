package com.carles.kotlin.core.ui

import android.content.res.Resources
import androidx.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.carles.kotlin.R
import com.google.android.material.appbar.MaterialToolbar

fun ViewGroup.inflate(@LayoutRes layoutRes: Int) = LayoutInflater.from(context).inflate(layoutRes, this, false)

inline fun AppCompatActivity.consume(f: () -> Unit): Boolean {
    f()
    return true
}

fun AppCompatActivity.getStrings(ids: List<Int>) = ids.map { getString(it) }.toTypedArray()

fun AppCompatActivity.initDefaultToolbar(): MaterialToolbar {
    val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
    setSupportActionBar(toolbar)
    supportActionBar?.apply {
        setDisplayHomeAsUpEnabled(true)
        setDisplayShowHomeEnabled(true)
    }
    toolbar.setNavigationOnClickListener { onBackPressed() }
    return toolbar
}

fun Int.toPx() = this / Resources.getSystem().displayMetrics.density