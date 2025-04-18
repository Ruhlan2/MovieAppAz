package com.isteam.movieappaz.common.utils

import android.content.Context
import androidx.annotation.StringRes

class ManualResourceProvider(
    private val context: Context
) {
    fun getString(@StringRes stringResId: Int): String {
        return context.getString(stringResId)
    }
}