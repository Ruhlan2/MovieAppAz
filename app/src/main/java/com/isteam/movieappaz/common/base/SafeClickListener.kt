package com.isteam.movieappaz.common.base

import android.os.SystemClock
import android.view.View

class SafeClickListener(
    private val defaultInterval: Int = 500,
    private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {

    override fun onClick(v: View) {
        if (canClickable(defaultInterval)) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeCLick(v)
    }


    companion object {
        fun canClickable(defaultInterval: Int = 500) =
            SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval

        private var lastTimeClicked: Long = 0
    }
}

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}