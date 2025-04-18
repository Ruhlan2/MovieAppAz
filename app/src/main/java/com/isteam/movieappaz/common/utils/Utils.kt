package com.isteam.movieappaz.common.utils

import java.util.Calendar

fun getCurrentTime(): String {
    val rightNow = Calendar.getInstance()
    val currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY)
    val currentMinute = rightNow.get(Calendar.MINUTE)
    return "$currentHourIn24Format:$currentMinute"
}
